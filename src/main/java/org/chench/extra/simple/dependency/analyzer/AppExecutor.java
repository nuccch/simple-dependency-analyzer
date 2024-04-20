package org.chench.extra.simple.dependency.analyzer;

import org.chench.extra.simple.dependency.analyzer.service.Executor;
import org.chench.extra.simple.dependency.analyzer.service.IOHandler;
import org.chench.extra.simple.dependency.analyzer.service.Parser;
import org.chench.extra.simple.dependency.analyzer.service.impl.GraphvizExecutor;
import org.chench.extra.simple.dependency.analyzer.service.impl.GraphvizIOHandler;
import org.chench.extra.simple.dependency.analyzer.service.impl.MavenParser;

import java.util.List;
import java.util.Map;

/**
 * @author chench
 * @date 2024.04.20
 */
public class AppExecutor {
    public String execute(String dir, String... ignore) {
        Parser parser = new MavenParser();
        parser.setExtraIgnoreDirs(ignore);
        Map<String, String> modules = parser.parseModule(dir);
        Map<String, List<String>> dependencies = parser.parseDependency(modules);

        // 打印内部模块之间的依赖关系
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            for (String artifactId : entry.getValue()) {
                if (modules.containsKey(artifactId)) {
                    System.out.println(String.format("%-30s -> %s", entry.getKey(), artifactId));
                }
            }
        }

        String name = "dependency";
        IOHandler handler = new GraphvizIOHandler();
        String input = handler.buildInput(modules, dependencies, name);
        String output = handler.buildOutput(name);

        Executor executor = new GraphvizExecutor();
        executor.execute(input, output);
        return output;
    }
}