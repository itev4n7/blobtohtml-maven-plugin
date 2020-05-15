package com.example;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class);

    public static void dropDBTable(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            LOGGER.info("try to drop table");
            stmt.execute("drop table savedPhotos;");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static int getRows(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            LOGGER.info("try to get rows");
            ResultSet resultSet = stmt.executeQuery("select count(*) from savedPhotos;");
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return 0;
    }
}
