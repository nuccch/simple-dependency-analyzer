package org.chench.extra.simple.dependency.analyzer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author chench
 * @date 2024.05.08
 */
public interface SQLiteDao {
    String JDBC_DRIVER_CLASS = "org.sqlite.JDBC";
    String JDBC_URL = "jdbc:sqlite:simple_dependency_analyzer.db";

    void createTable();

    default Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }
}