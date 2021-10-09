package com.redsoft.testproj;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Init {
    public void createDb() {
        try {
            String sql = new String(Files.readAllBytes(Paths.get("db__init.sql")));
            try (Connection db = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "masterkey")) {
                Statement stmt = db.createStatement();
                stmt.execute("CREATE DATABASE testprojdb");
            }
            try (Connection db = DriverManager.getConnection("jdbc:postgresql://localhost/testprojdb", "postgres", "masterkey")) {
                Statement stmt = db.createStatement();
                stmt.execute(sql);
            }
        } catch (Exception ex) {

        }
    }
}
