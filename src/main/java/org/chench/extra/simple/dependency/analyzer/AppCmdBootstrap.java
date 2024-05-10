package org.chench.extra.simple.dependency.analyzer;

import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;
import org.chench.extra.simple.dependency.analyzer.service.impl.AppAnalyzer;

/**
 * 命令行启动，如：java -jar simple-dependency-analyzer.jar <project_path> [ignore_dir_names, e.g: mac,boar]
 * @author chench
 * @date 2024.05.10
 */
public class AppCmdBootstrap {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ");
            System.out.println("java -jar simple-dependency-analyzer.java <path> [ignores,...]");
            return;
        }
        // 项目路径
        String dir = args[0];
        // 需要忽略的目录名，以英文逗号分隔
        String[] ignores = null;
        if (args.length > 1) {
            ignores = args[1].split(CommonConstant.SPLIT_CHAR);
        }
        AppAnalyzer appAnalyzer = new AppAnalyzer();
        String output = appAnalyzer.analyze(dir, ignores);
        System.out.println(output);
    }
}
