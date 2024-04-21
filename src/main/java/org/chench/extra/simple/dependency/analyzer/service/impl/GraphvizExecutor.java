package org.chench.extra.simple.dependency.analyzer.service.impl;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.chench.extra.java.util.SimpleOSUtil;
import org.chench.extra.simple.dependency.analyzer.service.Executor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author chench
 * @date 2024.04.19
 */
public class GraphvizExecutor implements Executor {
    @Override
    public String execute(String cmd) {
        try {
            //接收正常结果流
            ByteArrayOutputStream ioStream = new ByteArrayOutputStream();
            //接收异常结果流
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            CommandLine commandLine = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor.Builder().get();
            PumpStreamHandler streamHandler = new PumpStreamHandler(ioStream, errStream);
            exec.setStreamHandler(streamHandler);
            int code = exec.execute(commandLine);
            System.out.println("退出代码: " + code);
            System.out.println(ioStream.toString("GBK"));
            System.out.println(errStream.toString("GBK"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String execute(String input, String output) {
        String cmd = new StringBuilder()
                .append(resolveGraphvizPath()).append(" ")
                .append("-Tpng ").append(input).append(" ")
                .append("-o ").append(output)
                .toString();
        return execute(cmd);
    }

    private String resolveGraphvizPath() {
        String pathEnv = SimpleOSUtil.getEnv("PATH");
        String[] paths = pathEnv.split(File.pathSeparator);
        for (String path : paths) {
            if (path.contains("Graphviz")) {
                String exeName = SimpleOSUtil.isWindows() ? "dot.exe" : "dot";
                String graphvizPath = String.format("%s%s%s", path, File.separatorChar, exeName);
                File graphvizExeFile = new File(graphvizPath);
                if (graphvizExeFile.exists() && graphvizExeFile.isFile()) {
                    return graphvizPath;
                }
            }
        }
        return null;
    }
}