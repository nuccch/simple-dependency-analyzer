package org.chench.extra.simple.dependency.analyzer.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.bean.Edge;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;
import org.chench.extra.simple.dependency.analyzer.service.IOHandler;

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
                        boolean cycleDependency = checkCycleDependency(path);
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

    /**
     * 检查是否存在循环依赖 TODO：算法实现是存在缺陷的，需要完善
     * @param path
     * @return
     */
    private boolean checkCycleDependency(List<Edge> path) {
        if (path.isEmpty()) {
            return false;
        }
        if (path.size() == 1) {
            return false;
        }

        Edge first = path.get(0);
//        Edge last = path.get(path.size() - 1);
//        boolean cycle = first.getStart().equals(last.getEnd());
//        if (!cycle) {
//            return false;
//        }

        List<Edge> cyclePath = new ArrayList<>();
        findCyclePath(1, first, path, cyclePath);
        if (cyclePath.isEmpty()) {
            return false;
        }
        if (cyclePath.get(cyclePath.size() - 1).getEnd().equals(first.getStart())) {
            // 快速检查
            return true;
        }
        for (Edge edge : cyclePath) {
            // 遍历检查
            if (edge.getEnd().equals(first.getStart())) {
                return true;
            }
        }
        for (Edge edge : path) {
            // 遍历检查
            if (edge.getEnd().equals(first.getStart())) {
                return true;
            }
        }
        return false;
    }

    private void findCyclePath(int index, Edge first, List<Edge> path, List<Edge> cyclePath) {
        for (int i = index; i < path.size(); i++) {
            Edge list = path.get(i);
            if (first.getEnd().equals(list.getStart())) {
                cyclePath.add(list);
                findCyclePath(i+1, list, path, cyclePath);
            }
        }
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