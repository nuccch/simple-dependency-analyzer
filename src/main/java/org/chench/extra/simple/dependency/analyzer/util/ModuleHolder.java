package org.chench.extra.simple.dependency.analyzer.util;

import org.chench.extra.simple.dependency.analyzer.bean.CalculateModule;

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

}
