package org.chench.extra.simple.dependency.analyzer.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 通用常量定义
 * @author chench
 * @date 2024.04.19
 */
public abstract class CommonConstant {
    public static final String PATH_TMP = "tmp";

    public static final String COLOR_RED = "red";
    public static final String COLOR_BLACK = "black";

    /** 需要忽略的目录名 */
    public static final List<String> IGNORE_DIR_NAMES = Arrays.asList(new String[]{
            ".settings",
            ".idea",
            ".git",
            "target",
            "bin",
            "src",
            "resources",
            "sql",
            "doc",
            "log",
            "logs"
    });
}