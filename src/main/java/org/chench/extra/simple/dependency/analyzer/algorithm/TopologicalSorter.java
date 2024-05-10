package org.chench.extra.simple.dependency.analyzer.algorithm;

import java.util.*;

/**
 * 拓扑排序
 *
 * @author chench
 * @date 2024.04.25
 */
public class TopologicalSorter {
    public static void main(String[] args) {
        TopologicalSorter sorter = new TopologicalSorter();

        String[] modules = new String[]{"m0", "m1", "m2", "m3", "m4"};
        String[][] dependencies = new String[][] {
                {"m1", "m0"}, {"m2", "m0"},
                {"m3", "m1"}, {"m3", "m2"},
                {"m4", "m0"}, {"m3", "m4"}
        };
        String[] seq = sorter.findOrder(modules, dependencies);
        System.out.println(Arrays.asList(seq));
    }

    /**
     * 使用拓扑排序算法找出依赖顺序
     * @param modules
     * @param dependencies
     * @return
     */
    public String[] findOrder(String[] modules, String[][] dependencies) {
        int num = modules.length; // 顶点数
        Map<String, List<String>> dependencyMap = new HashMap<>(); // 记录指向关系
        Map<String, Integer> inDegreeMap = new HashMap<>(num); // 记录每个点的入度
        for (int i = 0; i < dependencies.length; i++) {
            List<String> list = dependencyMap.getOrDefault(dependencies[i][1], new ArrayList<>());
            list.add(dependencies[i][0]);
            dependencyMap.put(dependencies[i][1], list);

            Integer deg = inDegreeMap.getOrDefault(dependencies[i][0], 0);
            inDegreeMap.put(dependencies[i][0], deg+1);
        }

        Queue<String> q = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            if (inDegreeMap.getOrDefault(modules[i], 0) == 0) {
                q.offer(modules[i]);
            }
        }

        int idx = 0;
        String[] seq = new String[num];
        while (!q.isEmpty()) {
            String top = q.poll();
            seq[idx] = top;
            idx++;

            List<String> list = dependencyMap.getOrDefault(top, new ArrayList<>());
            for (int i = 0; i < list.size(); i++) {
                String ch = list.get(i);
                Integer deg = inDegreeMap.get(ch);
                deg--;
                inDegreeMap.put(ch, deg);
                if (deg == 0) {
                    q.offer(ch);
                }
            }
        }
        if (idx < num) {
            return new String[]{};
        }
        return seq;
    }
}
