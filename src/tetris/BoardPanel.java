package tetris;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel{
    private final int CELL_SIZE = 30;
    private final int COLS = 10;
    private final int ROWS = 20;
    
    private Tetromino piece;
    private GameBoard board;

    private boolean gameOver = false;
    private int score = 0;
    
    public BoardPanel(GameBoard board) {

        this.board = board;

        setBackground(Color.BLACK);
    }
    
    public void setPiece(Tetromino piece) {
        this.piece = piece;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    private int getGhostY() {

        int ghostY = piece.getY();

        while (board.isValidPosition(
                piece.getShape(),
                piece.getX(),
                ghostY + 1)) {

            ghostY++;
        }

        return ghostY;
    }
    
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int BOARD_X = (getWidth() - (COLS * CELL_SIZE)) / 2;
        int BOARD_Y = (getHeight() - (ROWS * CELL_SIZE)) / 2;

        // Draw the board
        for (int row = 0; row < ROWS; row++) {

            for (int col = 0; col < COLS; col++) {

                int x = BOARD_X + col * CELL_SIZE;
                int y = BOARD_Y + row * CELL_SIZE;

                Color cellColor = board.getCellColor(row, col);
                if (cellColor != null) {
                    g.setColor(cellColor);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        int scoreX = BOARD_X - 120;
        int scoreY = BOARD_Y + (ROWS * CELL_SIZE) - 40;

        g.drawString("SCORE", scoreX, scoreY);
        g.drawString(String.valueOf(score), scoreX, scoreY + 30);

        if (!gameOver) {

            // Draw the current Tetris piece
            int[][] shape = piece.getShape();

            g.setColor(piece.getColor());

            for (int row = 0; row < shape.length; row++) {

                for (int col = 0; col < shape[row].length; col++) {

                    if (shape[row][col] == 1) {

                        int x = BOARD_X + (piece.getX() + col) * CELL_SIZE;
                        int y = BOARD_Y + (piece.getY() + row) * CELL_SIZE;

                        if (y >= BOARD_Y) {
                            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                            g.setColor(Color.WHITE);
                            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                        }

                        g.setColor(piece.getColor());

                    }

                }
            }
        }

        int ghostY = getGhostY();

        g.setColor(new Color(
                piece.getColor().getRed(),
                piece.getColor().getGreen(),
                piece.getColor().getBlue(),
                70
        ));

        for (int row = 0; row < piece.getShape().length; row++) {

            for (int col = 0; col < piece.getShape()[row].length; col++) {

                if (piece.getShape()[row][col] == 1) {

                    int x = BOARD_X + (piece.getX() + col) * CELL_SIZE;
                    int y = BOARD_Y + (ghostY + row) * CELL_SIZE;

                    if (y >= BOARD_Y) {
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                        g.setColor(Color.WHITE);
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }

                    g.setColor(new Color(
                            piece.getColor().getRed(),
                            piece.getColor().getGreen(),
                            piece.getColor().getBlue(),
                            70
                    ));
                }
            }
        }

        if (gameOver) {

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 35));

            String message = "GAME OVER";

            FontMetrics fm = g.getFontMetrics();

            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;

            g.drawString(message, x, y);
        }
    }
}
