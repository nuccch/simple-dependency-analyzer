package org.chench.extra.simple.dependency.analyzer.service;

import java.util.List;
import java.util.Map;

/**
 * @author chench
 * @date 2024.04.19
 */
public interface IOHandler {
    /**
     * 构建模块依赖顺序列表
     * @param modules
     * @param dependencies
     * @return
     */
    List<String> buildOrder(Map<String, String> modules, Map<String, List<String>> dependencies);

    /**
     * 构建Graphviz的配置文件
     * @param modules
     * @param dependencies
     * @param name
     * @return
     */
    String buildInput(Map<String, String> modules, Map<String, List<String>> dependencies, String name);

    /**
     * 构建Graphviz输出文件文件
     * @param name
     * @return
     */
    String buildOutput(String name);
}