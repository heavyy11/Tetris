
package tetris;

import java.sql.*;
import java.util.*;

public class ScoreDAO {

    public void saveScore(int userId, int score)
            throws SQLException {

        String sql = "INSERT INTO scores (user_id, score) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, score);

            statement.executeUpdate();
        }
    }
    
    public List<Object[]> getLeaderboard() throws SQLException {

        List<Object[]> leaderboard = new ArrayList<>();

        String sql
                = "SELECT users.username, scores.score AS score "
                + "FROM scores "
                + "INNER JOIN users ON scores.user_id = users.user_id "
                + "ORDER BY scores.score DESC, scores.date_played ASC "
                + "LIMIT 10";
        
//        String sql
//                = "SELECT users.username, MAX(scores.score) AS high_score "
//                + "FROM scores "
//                + "INNER JOIN users ON scores.user_id = users.user_id "
//                + "GROUP BY users.user_id, users.username "
//                + "ORDER BY high_score DESC "
//                + "LIMIT 10";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                String username = result.getString("username");
                int score = result.getInt("score");

                leaderboard.add(new Object[]{username, score});
            }
        }

        return leaderboard;
    }
}
