package com.domain;

import com.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authorisation {

    public boolean isUserAuthorised(long userId) {
        String query = "SELECT admin FROM service WHERE id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
