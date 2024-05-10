package org.chench.extra.simple.dependency.analyzer.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.dao.ProjectDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chench
 * @date 2024.05.08
 */
public class ProjectServiceImpl {
    ProjectDao projectDao = new ProjectDao();
    ExecutorService threadPool = Executors.newFixedThreadPool(1);

    /**
     * 保存路径
     * @param path
     */
    public void savePath(String path) {
        this.threadPool.submit(() -> {
            projectDao.createTable();
            String oldPath = projectDao.queryPath();
            if (StringUtils.isNotBlank(oldPath)) {
                projectDao.updatePath(path);
            } else {
                projectDao.insertPath(path);
            }
            System.out.println(String.format("save success! path: %s", path));
        });
    }

    /**
     * 查询路径
     * @return
     */
    public String queryPath() {
        return this.projectDao.queryPath();
    }
}