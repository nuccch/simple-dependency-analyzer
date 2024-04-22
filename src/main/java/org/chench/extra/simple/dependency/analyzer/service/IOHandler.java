package org.chench.extra.simple.dependency.analyzer.service;

import org.chench.extra.simple.dependency.analyzer.bean.CalculateModule;

import java.util.List;
import java.util.Map;

/**
 * @author chench
 * @date 2024.04.19
 */
public interface IOHandler {
    List<CalculateModule> buildModuleWeight(Map<String, String> modules, Map<String, List<String>> dependencies);
    String buildInput(Map<String, String> modules, Map<String, List<String>> dependencies, String name);
    String buildOutput(String name);
}