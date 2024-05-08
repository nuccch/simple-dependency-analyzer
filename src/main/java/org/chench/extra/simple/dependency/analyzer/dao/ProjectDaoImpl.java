package org.chench.extra.simple.dependency.analyzer.dao;

import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author chench
 * @date 2024.05.08
 */
public class ProjectDaoImpl implements SQLiteDao {
    static {
        try {
            // 加载SQLite JDBC驱动
            Class.forName(JDBC_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        String sql = new StringBuilder()
                .append("CREATE TABLE PROJECT (")
                .append("ID INT PRIMARY KEY     NOT NULL,")
                .append("PATH           TEXT    NOT NULL)")
                .toString();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            if ((e instanceof SQLiteException) && (e.getMessage().contains("already exists"))) {
                // do nothing
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void insertPath(String path) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String sql = new StringBuilder()
                    .append("INSERT INTO PROJECT (ID,PATH) VALUES (1, ")
                    .append("'").append(path).append("'").append(");")
                    .toString();
            conn.setAutoCommit(false);
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePath(String path) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String sql = new StringBuilder()
                    .append("UPDATE PROJECT set PATH = ")
                    .append("'").append(path).append("'")
                    .append(" where ID=1;")
                    .toString();
            conn.setAutoCommit(false);
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String queryPath() {
        String sql = new StringBuilder()
                .append("SELECT PATH FROM PROJECT WHERE ID = 1;")
                .toString();
        String path = null;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                path = rs.getString("path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return path;
    }
}