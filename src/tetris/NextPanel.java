
package tetris;

import javax.swing.*;
import java.awt.*;

public class NextPanel extends JPanel {

    private Tetromino piece;

    public NextPanel() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(150, 600));
    }

    public void setPiece(Tetromino piece) {
        this.piece = piece;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Draw NEXT label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        String label = "NEXT";

        FontMetrics fm = g.getFontMetrics();

        int labelX = (getWidth() - fm.stringWidth(label)) / 2;

        g.drawString(label, labelX, 75);

        // Don't draw anything if there is no piece
        if (piece == null) {
            return;
        }

        int[][] shape = piece.getBaseShape();

        int cellSize = 20;

        int startX = (getWidth() - 4 * cellSize) / 2;
        int startY = 90;

        // Draw the next piece
        g.setColor(piece.getColor());

        for (int row = 0; row < shape.length; row++) {

            for (int col = 0; col < shape[row].length; col++) {

                if (shape[row][col] == 1) {

                    int x = startX + col * cellSize;
                    int y = startY + row * cellSize;

                    g.fillRect(x, y, cellSize, cellSize);

                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, cellSize, cellSize);

                    g.setColor(piece.getColor());
                }
            }
        }
    }
}
