package org.chench.extra.simple.dependency.analyzer.service;

import java.util.List;
import java.util.Map;

/**
 * @author chench
 * @date 2024.04.19
 */
public interface Parser {
    /**
     * 解析项目名
     * @param path
     * @return
     */

    String parseProjectName(String path);

    /**
     * 解析指定路径下的模块集合
     * @param path
     * @return
     */
    Map<String, String> parseModule(String path);

    /**
     * 解析模块的依赖关系集合
     * @param modules
     * @return
     */
    Map<String, List<String>> parseDependency(Map<String, String> modules);

    /**
     * 设置需要忽略的目录名列表
     * @param ignore
     */
    void setExtraIgnoreDirs(String... ignore);
}