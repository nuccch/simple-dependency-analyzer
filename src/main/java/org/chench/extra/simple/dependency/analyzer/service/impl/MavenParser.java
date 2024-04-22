package org.chench.extra.simple.dependency.analyzer.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;
import org.chench.extra.simple.dependency.analyzer.constant.XmlConstant;
import org.chench.extra.simple.dependency.analyzer.service.Parser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chench
 * @date 2024.04.19
 */
public class MavenParser implements Parser {
    private List<String> ignoreDirList = null;

    @Override
    public Map<String, String> parseModule(String path) {
        List<File> pomFiles = findPomFiles(path);
        Map<String, String> pomMap = new HashMap<>();
        for (File pom : pomFiles) {
            String name = parseModuleName(pom);
            if (StringUtils.isNotBlank(name)) {
                pomMap.put(name, pom.getAbsolutePath());
            }
        }
        return pomMap;
    }

    /**
     * 解析模块的依赖关系
     * @param modules
     * @return
     */
    public Map<String, List<String>> parseDependency(Map<String, String> modules) {
        Map<String, List<String>> dependencies = new HashMap<>();
        for (Map.Entry<String, String> entry : modules.entrySet()) {
            List<String> dependencyList = parseDependency(entry.getValue());
            if (Objects.isNull(dependencyList)) {
                continue;
            }
            // 过滤出外部依赖模块
            List<String> externalModules = filterExternalModule(modules, dependencyList);
            dependencyList.removeAll(externalModules);
            dependencies.put(entry.getKey(), dependencyList);
        }
        return dependencies;
    }

    @Override
    public void setExtraIgnoreDirs(String... ignore) {
        List<String> ignoreDirNames = new ArrayList<>();
        ignoreDirNames.addAll(CommonConstant.IGNORE_DIR_NAMES);
        if (!Objects.isNull(ignore) && ignore.length > 0) {
            ignoreDirNames.addAll(Arrays.asList(ignore));
        }
        this.ignoreDirList = ignoreDirNames;
    }

    /**
     * 解析模块的依赖关系
     * @param pomPath
     * @return
     */
    private List<String> parseDependency(String pomPath) {
        List<String> list = new ArrayList<>();
        Element root = parseXmlRoot(pomPath);
        Element dependencies = root.element(XmlConstant.TARGET_DEPENDENCIES);
        if (Objects.isNull(dependencies)) {
            return null;
        }
        List<Element> dependencyList = dependencies.elements(XmlConstant.TARGET_DEPENDENCY);
        for (Element element : dependencyList) {
            String artifactId = element.elementText(XmlConstant.TARGET_ARTIFACT_ID);
            list.add(artifactId);
        }
        return list;
    }

    /**
     * 过滤非自定义模块列表
     * @param modules
     * @param dependencyList
     * @return
     */
    private List<String> filterExternalModule(Map<String, String> modules, List<String> dependencyList) {
        Map<String, String> internalModuleMap = new HashMap<>();
        for (Map.Entry<String, String> entry : modules.entrySet()) {
            internalModuleMap.put(entry.getKey(), entry.getValue());
        }
        return dependencyList.stream().filter(key -> !internalModuleMap.containsKey(key)).collect(Collectors.toList());
    }

    /**
     * 从pom.xml文件中解析模块名称
     * @param pom
     * @return
     */
    private String parseModuleName(File pom) {
        return parseXmlRoot(pom).element(XmlConstant.TARGET_ARTIFACT_ID).getTextTrim();
    }

    /**
     * 解析XML根节点
     * @param xml
     * @return
     */
    private Element parseXmlRoot(String xml) {
        return parseXmlRoot(new File(xml));
    }

    /**
     * 解析XML根节点
     * @param xml
     * @return
     */
    private Element parseXmlRoot(File xml) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(xml);
            return document.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查找指定路径下的所有pom.xml文件
     * @param path
     * @return
     */
    private List<File> findPomFiles(String path) {
        List<File> poms = new ArrayList<>();
        File root = new File(path);
        File[] files = root.listFiles();
        for (File f : files) {
            if (f.isFile() && XmlConstant.TARGET_POM_NAME.equals(f.getName())) {
                poms.add(f);
            } else if (f.isDirectory() && !isIgnoreDir(f.getName())) {
                poms.addAll(findPomFiles(f.getAbsolutePath()));
            }
        }
        return poms;
    }

    /**
     * 根据名称判断目录是否要被忽略
     * @param dirName
     * @return
     */
    private boolean isIgnoreDir(String dirName) {
        return this.ignoreDirList.contains(dirName);
    }
}