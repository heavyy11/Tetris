package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private final int CELL_SIZE = 30;
    private final int COLS = 10;
    private final int ROWS = 20;

    private final int BOARD_X = 60;
    private final int BOARD_Y = 25;

    private Tetromino piece;

    public GamePanel() {

        setBackground(Color.BLACK);

        piece = new OPiece();
        
        bindKey("LEFT", "moveLeft", () -> piece.moveLeft());
        bindKey("RIGHT", "moveRight", () -> piece.moveRight());
        bindKey("DOWN", "moveDown", () -> piece.moveDown());
        bindKey("UP", "rotate", () -> piece.rotate());
    }
    
    private void bindKey(String key, String name, Runnable action){
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(key), name
        );

        getActionMap().put(name, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                action.run();

                repaint();

            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Draw the board
        g.setColor(Color.DARK_GRAY);

        for (int row = 0; row < ROWS; row++) {

            for (int col = 0; col < COLS; col++) {

                int x = BOARD_X + col * CELL_SIZE;
                int y = BOARD_Y + row * CELL_SIZE;

                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            }
        }

        // Draw the current Tetris piece
        int[][] shape = piece.getShape();

        g.setColor(piece.getColor());

        for (int row = 0; row < shape.length; row++) {

            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1) {

                    int x = BOARD_X + (piece.getX() + col) * CELL_SIZE;
                    int y = BOARD_Y + (piece.getY() + row) * CELL_SIZE;

                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                    g.setColor(piece.getColor());

                }

            }
        }

    }

}