package org.chench.extra.simple.dependency.analyzer.service;

/**
 * @author chench
 * @date 2024.04.19
 */
public interface Executor {
    default String execute(){
        return null;
    };
    String execute(String cmd);
    String execute(String input, String output);
}