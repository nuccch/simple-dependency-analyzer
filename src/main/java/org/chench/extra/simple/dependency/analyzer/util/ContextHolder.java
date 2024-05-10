package org.chench.extra.simple.dependency.analyzer.util;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chench
 * @date 2024.04.22
 */
public abstract class ContextHolder {
    private static List<String> orderedModules = new ArrayList<>();
    private static Map<String, Integer> moduleMap = new HashMap<>();
    private static String projectName = CommonConstant.NAME_DEFAULT;

    public static void setModules(List<String> orderedModules) {
        ContextHolder.orderedModules.clear();
        ContextHolder.orderedModules.addAll(orderedModules);
        ContextHolder.moduleMap.clear();
        for (int i = 0; i < orderedModules.size(); i++) {
            ContextHolder.moduleMap.put(orderedModules.get(i), (i+1));
        }
    }

    public static List<String> getOrderedModules() {
        return ContextHolder.orderedModules;
    }

    public static Integer getModuleOrder(String name) {
        Integer number = ContextHolder.moduleMap.get(name);
        return number == null ? 0 : number;
    }

    public static void setProjectName(String projectName) {
        if (StringUtils.isNotBlank(projectName)) {
            ContextHolder.projectName = projectName;
        }
    }

    public static String getBuildOrderFileName() {
        return new StringBuilder().append(projectName).append(".txt")
                .toString();
    }

}
