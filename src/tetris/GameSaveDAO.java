package tetris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameSaveDAO {

    // Convert the board's integer array into a String
    private String encodeBoard(int[][] boardData) {

        StringBuilder data = new StringBuilder();

        for (int row = 0; row < boardData.length; row++) {

            for (int col = 0; col < boardData[row].length; col++) {

                data.append(boardData[row][col]);

                if (row != boardData.length - 1 ||
                    col != boardData[row].length - 1) {

                    data.append(",");
                }
            }
        }

        return data.toString();
    }

    // Convert the saved String back into a 2D array
    private int[][] decodeBoard(String data) {

        String[] values = data.split(",");

        int[][] boardData = new int[20][10];

        int index = 0;

        for (int row = 0; row < 20; row++) {

            for (int col = 0; col < 10; col++) {

                boardData[row][col] = Integer.parseInt(values[index]);
                index++;
            }
        }

        return boardData;
    }

    // Save or update a player's game
    public void saveGame(
            int userId,
            int score,
            int[][] boardData,
            Tetromino currentPiece,
            Tetromino nextPiece,
            Tetromino heldPiece,
            boolean hasHeldThisTurn
    ) throws SQLException {

        String sql = """
            INSERT INTO saved_games (
                user_id,
                score,
                board_data,
                current_piece_type,
                current_x,
                current_y,
                current_rotation,
                next_piece_type,
                held_piece_type,
                has_held_this_turn
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

            ON DUPLICATE KEY UPDATE
                score = VALUES(score),
                board_data = VALUES(board_data),
                current_piece_type = VALUES(current_piece_type),
                current_x = VALUES(current_x),
                current_y = VALUES(current_y),
                current_rotation = VALUES(current_rotation),
                next_piece_type = VALUES(next_piece_type),
                held_piece_type = VALUES(held_piece_type),
                has_held_this_turn = VALUES(has_held_this_turn)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, score);
            statement.setString(3, encodeBoard(boardData));

            statement.setString(
                    4, PieceFactory.getPieceType(currentPiece));

            statement.setInt(5, currentPiece.getX());
            statement.setInt(6, currentPiece.getY());
            statement.setInt(7, currentPiece.getRotation());

            statement.setString(
                    8, PieceFactory.getPieceType(nextPiece));

            if (heldPiece == null) {
                statement.setNull(9, java.sql.Types.VARCHAR);
            } else {
                statement.setString(
                        9, PieceFactory.getPieceType(heldPiece));
            }

            statement.setBoolean(10, hasHeldThisTurn);

            statement.executeUpdate();
        }
    }

    // Retrieve a player's saved game
    public GameSave loadGame(int userId) throws SQLException {

        String sql = "SELECT * FROM saved_games WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    GameSave save = new GameSave();

                    save.score = result.getInt("score");

                    save.boardData = decodeBoard(
                            result.getString("board_data"));

                    save.currentPieceType =
                            result.getString("current_piece_type");

                    save.currentX = result.getInt("current_x");
                    save.currentY = result.getInt("current_y");
                    save.currentRotation =
                            result.getInt("current_rotation");

                    save.nextPieceType =
                            result.getString("next_piece_type");

                    save.heldPieceType =
                            result.getString("held_piece_type");

                    save.hasHeldThisTurn =
                            result.getBoolean("has_held_this_turn");

                    return save;
                }
            }
        }

        return null;
    }

    // Delete a player's saved game
    public void deleteSave(int userId) throws SQLException {

        String sql = "DELETE FROM saved_games WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            statement.executeUpdate();
        }
    }
}