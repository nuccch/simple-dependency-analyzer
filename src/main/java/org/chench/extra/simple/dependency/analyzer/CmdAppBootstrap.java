package org.chench.extra.simple.dependency.analyzer;

import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;

/**
 * 默认执行入口
 */
public class CmdAppBootstrap {
    public static void main(String[] args) {
        // 项目路径
        String dir = args[0];
        // 需要忽略的目录名，以英文逗号分隔
        String[] ignores = null;
        if (args.length > 1) {
            ignores = args[1].split(CommonConstant.SPLIT_CHAR);
        }
        AppExecutor appExecutor = new AppExecutor();
        String output = appExecutor.execute(dir, ignores);
        System.out.println(output);
    }
}
