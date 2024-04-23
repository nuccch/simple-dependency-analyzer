package org.chench.extra.simple.dependency.analyzer.util;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.bean.CalculateModule;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chench
 * @date 2024.04.22
 */
public abstract class ModuleHolder {
    private static List<CalculateModule> modules = new ArrayList<>();
    private static Map<String, Integer> moduleMap = new HashMap<>();
    private static String projectName = CommonConstant.NAME_DEFAULT;

    public static void setModules(List<CalculateModule> modules) {
        ModuleHolder.modules.clear();
        ModuleHolder.modules.addAll(modules);
        ModuleHolder.moduleMap = modules.stream().collect(Collectors.toMap(m -> m.getName(), m -> m.getNumber()));
    }

    public static List<CalculateModule> getModules() {
        return ModuleHolder.modules;
    }

    public static Integer getModuleNumber(String name) {
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
