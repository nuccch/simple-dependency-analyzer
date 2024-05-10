package org.chench.extra.simple.dependency.analyzer.util;

import org.chench.extra.simple.dependency.analyzer.bean.CalculateEdge;
import org.chench.extra.simple.dependency.analyzer.bean.Edge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chench
 * @date 2024.04.21
 */
public abstract class AppUtil {
    public static List<CalculateEdge> wrapCalculateEdge(List<Edge> edges) {
        return edges.stream().map(edge -> new CalculateEdge(edge.getStart(), edge.getEnd()))
                .collect(Collectors.toList());
    }

    /**
     * 保存构建顺序文件
     * @param file
     * @throws IOException
     */
    public static void saveBuildOrderFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<String> modules = ModuleHolder.getOrderedModules();
            // 一次输出模块名称即为构建顺序
            for (int i = 0; i < modules.size(); i++) {
                writer.write(modules.get(i) + "\n");
            }
            writer.flush();
        }
    }
}