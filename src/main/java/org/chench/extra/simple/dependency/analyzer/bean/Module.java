package org.chench.extra.simple.dependency.analyzer.bean;

/**
 * @author chench
 * @date 2024.04.19
 */
public class Module {
    /** 模块名 */
    private String name;
    /** 模块pom文件绝对路径 */
    private String pom;

    public Module() {

    }

    public Module(String name, String pom) {
        this.name = name;
        this.pom = pom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPom() {
        return pom;
    }

    public void setPom(String pom) {
        this.pom = pom;
    }
}