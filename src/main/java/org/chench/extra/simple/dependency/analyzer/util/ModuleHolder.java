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
public abstract class ModuleHolder {
    private static List<String> orderedModules = new ArrayList<>();
    private static Map<String, Integer> moduleMap = new HashMap<>();
    private static String projectName = CommonConstant.NAME_DEFAULT;

    public static void setModules(List<String> orderedModules) {
        ModuleHolder.orderedModules.clear();
        ModuleHolder.orderedModules.addAll(orderedModules);
        ModuleHolder.moduleMap.clear();
        for (int i = 0; i < orderedModules.size(); i++) {
            ModuleHolder.moduleMap.put(orderedModules.get(i), (i+1));
        }
    }

    public static List<String> getOrderedModules() {
        return ModuleHolder.orderedModules;
    }

    public static Integer getModuleOrder(String name) {
        Integer number = ModuleHolder.moduleMap.get(name);
        return number == null ? 0 : number;
    }

    public static void setProjectName(String projectName) {
        if (StringUtils.isNotBlank(projectName)) {
            ModuleHolder.projectName = projectName;
        }
    }

    public static String getBuildOrderFileName() {
        return new StringBuilder().append(projectName).append(".txt")
                .toString();
    }

}
