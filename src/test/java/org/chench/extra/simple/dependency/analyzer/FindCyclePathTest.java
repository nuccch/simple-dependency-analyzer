package org.chench.extra.simple.dependency.analyzer;

import org.chench.extra.simple.dependency.analyzer.bean.CalculateEdge;
import org.chench.extra.simple.dependency.analyzer.bean.Edge;
import org.chench.extra.simple.dependency.analyzer.util.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 检查是否存在循环依赖路径
 * @author chench
 * @date 2024.04.21
 */
public class FindCyclePathTest {
    List<Edge> path1 = new ArrayList<>();
    List<Edge> path2 = new ArrayList<>();
    List<Edge> path3 = new ArrayList<>();

    public FindCyclePathTest() {
        path1.add(new Edge("test-sofarpc-boot-api", "test-java-core"));
        path1.add(new Edge("test-java-core", "test-java-logback"));
        path1.add(new Edge("test-java-core", "test-sofarpc-boot-consumer"));
        path1.add(new Edge("test-sofarpc-boot-consumer", "test-java-logback"));
        path1.add(new Edge("test-sofarpc-boot-consumer", "test-sofarpc-boot-api"));
        path1.add(new Edge("test-java-core", "test-java-rocketmq"));
        path1.add(new Edge("test-java-rocketmq", "test-java-logback"));

        path2.add(new Edge("test-sofarpc-boot-provider", "test-java-logback"));
        path2.add(new Edge("test-sofarpc-boot-provider", "test-sofarpc-boot-api"));
        path2.add(new Edge("test-sofarpc-boot-api", "test-java-core"));
        path2.add(new Edge("test-java-core", "test-java-logback"));
        path2.add(new Edge("test-java-core", "test-sofarpc-boot-consumer"));
        path2.add(new Edge("test-sofarpc-boot-consumer", "test-java-logback"));
        path2.add(new Edge("test-sofarpc-boot-consumer", "test-sofarpc-boot-api"));
        path2.add(new Edge("test-java-core", "test-java-rocketmq"));
        path2.add(new Edge("test-java-rocketmq", "test-java-logback"));

        path3.add(new Edge("test-java-core", "test-java-rocketmq"));
        path3.add(new Edge("test-java-core", "test-sofarpc-boot-consumer"));
        path3.add(new Edge("test-sofarpc-boot-consumer", "test-java-logback"));
        path3.add(new Edge("test-sofarpc-boot-consumer", "test-sofarpc-boot-api"));
        path3.add(new Edge("test-sofarpc-boot-api", "test-java-core"));
        path3.add(new Edge("test-java-core", "test-java-rocketmq"));
        path3.add(new Edge("test-java-rocketmq", "test-java-logback"));
    }

    public static void main(String[] args) {
        FindCyclePathTest test = new FindCyclePathTest();
        boolean existCycle = test.existCyclePath(AppUtil.wrapCalculateEdge(test.path1));
        System.out.println(existCycle);

        existCycle = test.existCyclePath(AppUtil.wrapCalculateEdge(test.path2));
        System.out.println(existCycle);

        existCycle = test.existCyclePath(AppUtil.wrapCalculateEdge(test.path3));
        System.out.println(existCycle);
    }


    Stack<Edge> stack = new Stack<>();
    private boolean existCyclePath(List<CalculateEdge> path) {
        stack.clear();
        Edge first = path.get(0);
        stack.push(first);
        while (true) {
            if (stack.isEmpty()) {
                break;
            }

            Edge edge = stack.peek();
            Edge next = findNext(edge.getEnd(), path);
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

    private Edge findNext(String lastEnd, List<CalculateEdge> path) {
        for (int i = 1; i < path.size(); i++) {
            CalculateEdge edge = path.get(i);
            if (!edge.isChecked() && edge.getStart().equals(lastEnd)) {
                edge.setChecked(true);
                return edge;
            }
        }
        return null;
    }

    private boolean allEdgeChecked(List<CalculateEdge> path) {
        for (int i = 1; i < path.size(); i++) {
            if (!path.get(i).isChecked()) {
                return false;
            }
        }
        return true;
    }
}