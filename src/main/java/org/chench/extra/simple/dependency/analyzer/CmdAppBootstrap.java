package org.chench.extra.simple.dependency.analyzer;

/**
 * 默认执行入口
 */
public class CmdAppBootstrap {
    public static void main(String[] args) {
        // 从命令行获取参数
        String dir = args[0];
        AppExecutor appExecutor = new AppExecutor();
        String output = appExecutor.execute(dir);
        System.out.println(output);
    }
}
