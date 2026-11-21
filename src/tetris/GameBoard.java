package tetris;

import java.awt.*;

public class GameBoard {
    private final int COLS = 10;
    private final int ROWS = 20;

    // false = empty cell
    // true = occupied cell
    private Color[][] cells = new Color[ROWS][COLS];

    public boolean isValidPosition(int[][] shape, int x, int y) {

        for (int row = 0; row < shape.length; row++) {

            for (int col = 0; col < shape[row].length; col++) {

                // Ignore empty cells in the piece
                if (shape[row][col] == 0) {
                    continue;
                }

                int boardX = x + col;
                int boardY = y + row;

                // Check left and right walls
                if (boardX < 0 || boardX >= COLS) {
                    return false;
                }

                // Check bottom wall
                if (boardY >= ROWS) {
                    return false;
                }

                // Check existing blocks
                // Negative Y is allowed because pieces can
                // start partially above the visible board.
                if (boardY >= 0 && cells[boardY][boardX]!= null) {
                    return false;
                }

            }
        }

        return true;
    }
    
    public void lockPiece(int[][] shape, int x, int y, Color color) {

        for (int row = 0; row < shape.length; row++) {

            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1) {

                    int boardX = x + col;
                    int boardY = y + row;

                    if (boardY >= 0) {
                        cells[boardY][boardX] = color;
                    }

                }
            }
        }
    }
    
    public boolean isAboveBoard(int[][] shape, int x, int y) {

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1 && y + row < 0) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public Color getCellColor(int row, int col) {
        return cells[row][col];
    }

    public int clearFullLines() {

        int cleared = 0;

        // Start checking from the bottom row
        for (int row = ROWS - 1; row >= 0; row--) {

            boolean full = true;

            // Check whether every cell in this row is occupied
            for (int col = 0; col < COLS; col++) {

                if (cells[row][col] == null) {
                    full = false;
                    break;
                }

            }

            // If the row is full, remove it
            if (full) {

                cleared++;

                // Move every row above it down
                for (int r = row; r > 0; r--) {

                    for (int col = 0; col < COLS; col++) {
                        cells[r][col] = cells[r - 1][col];
                    }

                }

                // Empty the top row
                for (int col = 0; col < COLS; col++) {
                    cells[0][col] = null;
                }

                // Check this same row again
                row++;
            }
        }

        return cleared;
    }
}