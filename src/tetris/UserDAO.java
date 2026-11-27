
package tetris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    // Register a new user
    public boolean registerUser(String username, String password)
            throws SQLException {

        String hashedPassword = BCrypt.hashpw(
                password,
                BCrypt.gensalt(12)
        );

        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            statement.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {

            // Username already exists
            return false;
        }
    }

    // Check login credentials
    public boolean loginUser(String username, String password)
            throws SQLException {

        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    String storedHash = result.getString("password_hash");

                    return BCrypt.checkpw(password, storedHash);
                }
            }
        }

        return false;
    }
    
    public int getUserId(String username) throws SQLException {

        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt("user_id");
                }
            }
        }

        return -1;
    }
}
