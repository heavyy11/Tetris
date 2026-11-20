package tetris;

public class GameBoard {
    private final int COLS = 10;
    private final int ROWS = 20;

    // false = empty cell
    // true = occupied cell
    private boolean[][] cells = new boolean[ROWS][COLS];

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
                if (boardY >= 0 && cells[boardY][boardX]) {
                    return false;
                }

            }
        }

        return true;
    }
}
