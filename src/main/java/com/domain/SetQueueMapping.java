package com.domain;

import com.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetQueueMapping {

    public boolean setMapping(long publisherId, String consumerQueueName, String eventType) {
        String query = "INSERT INTO queue_mapping (publisher_id, consumer_queuename, event_type) VALUES (?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE consumer_queuename = VALUES(consumer_queuename), event_type = VALUES(event_type)";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, publisherId);
            stmt.setString(2, consumerQueueName);
            stmt.setString(3, eventType);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
