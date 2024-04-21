package org.chench.extra.simple.dependency.analyzer.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.bean.CalculateEdge;
import org.chench.extra.simple.dependency.analyzer.bean.Edge;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;
import org.chench.extra.simple.dependency.analyzer.service.IOHandler;
import org.chench.extra.simple.dependency.analyzer.util.EdgeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author chench
 * @date 2024.04.19
 */
public class GraphvizIOHandler implements IOHandler {
    @Override
    public String buildInput(Map<String, String> modules, Map<String, List<String>> dependencies, String name) {
        try {
            String graphvizInput = String.format("%s%s%s.dot", CommonConstant.PATH_TMP, File.separator, name);
            File configFile = new File(graphvizInput);
            if (!configFile.exists()) {
                if (!configFile.getParentFile().exists()) {
                    configFile.getParentFile().mkdirs();
                }
                configFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(graphvizInput));
            writer.write(String.format("digraph %s{\n", name));
            writer.write(String.format("rankdir = LR\n"));
            writer.write(String.format("splines = spline\n"));
            writer.write(String.format("ratio = 1\n"));
            writer.write(String.format("fixedsize = true\n"));
            writer.write(String.format("dir = forward\n"));
            for (Map.Entry<String, String> entry: modules.entrySet()) {
                List<String> list = dependencies.get(entry.getKey());
                if (Objects.isNull(list) || list.isEmpty()) {
                    continue;
                }
                writer.write(String.format("%s;\n", formatName(entry.getKey())));
            }
            for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
                for (String artifactId : entry.getValue()) {
                    if (modules.containsKey(artifactId)) {
                        // 找出以指定节点为起始点的所有路径
                        List<Edge> path = new ArrayList<>();
                        findAllPath(entry.getKey(), dependencies, path);
                        // 将第一条边替换为当前要处理的依赖
                        path.set(0, new Edge(entry.getKey(), artifactId));
                        boolean cycleDependency = existCyclePath(EdgeUtil.wrapCalculateEdge(path));
                        String color = cycleDependency ? CommonConstant.COLOR_RED : CommonConstant.COLOR_BLACK;
                        System.out.println(path);
                        writer.write(String.format("%s->%s[color=%s];\n", formatName(entry.getKey()), formatName(artifactId), color));
                    }
                }
            }
            writer.write("}");
            writer.close();
            return graphvizInput;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String buildOutput(String name) {
        try {
            String graphvizOutput = String.format("%s%s%s.png", CommonConstant.PATH_TMP, File.separator, name);
            File outputFile = new File(graphvizOutput);
            if (!outputFile.exists()) {
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                outputFile.createNewFile();
            }
            return graphvizOutput;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stack<Edge> stack = new Stack<>();
    private boolean existCyclePath(List<CalculateEdge> path) {
        stack.clear();
        Edge first = path.get(0);
        stack.push(first);
        while (true) {
            if (stack.isEmpty()) {
                break;
            }

            Edge edge = stack.peek();
            Edge next = findNextEdge(edge.getEnd(), path);
            if (next == null) {
                stack.pop();
            }
            if (next != null) {
                stack.push(next);
            }
            if (!stack.isEmpty() &&stack.peek().getEnd().equals(first.getStart())) {
                return true;
            }
        }
        return false;
    }

    private Edge findNextEdge(String lastEnd, List<CalculateEdge> path) {
        for (int i = 1; i < path.size(); i++) {
            CalculateEdge edge = path.get(i);
            if (!edge.isChecked() && edge.getStart().equals(lastEnd)) {
                edge.setChecked(true);
                return edge;
            }
        }
        return null;
    }

    /**
     * 查找以节点开始的所有依赖路径
     * @param key 节点名称
     * @param dependencies 依赖关系集合
     * @param allPath 所有路径列表
     */
    private void findAllPath(String key, Map<String, List<String>> dependencies, List<Edge> allPath) {
        List<String> dependencyList = dependencies.get(key);
        for (String dependency: dependencyList) {
            Edge p = new Edge(key, dependency);
            if (checkPathExists(p, allPath)) {
                continue;
            }
            allPath.add(p);

            List<String> list = dependencies.get(dependency);
            if (Objects.isNull(list) || list.isEmpty()) {
                continue;
            }
            findAllPath(dependency, dependencies, allPath);
        }
    }

    /**
     * 检查路径是否已经存在
     * @param path 路径
     * @param allPath 所有路径列表
     * @return
     */
    private boolean checkPathExists(Edge path, List<Edge> allPath) {
        for (Edge edge : allPath) {
            if (edge.equals(path)) {
                return true;
            }
        }
        return false;
    }

    private String formatName(String name) {
        if (StringUtils.isNotBlank(name)) {
            return name.replaceAll("-", "_")
                    .replaceAll("\\.", "_");
        }
        return name;
    }
}