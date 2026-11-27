
package tetris;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    private JLabel holdLabel;
    private JLabel scoreLabel;
    private JLabel scoreValue;

    private PiecePreviewPanel holdPreview;

    public StatsPanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(150, 600));

        // HOLD label
        holdLabel = new JLabel("HOLD");

        holdLabel.setForeground(Color.WHITE);
        holdLabel.setFont(new Font("Arial", Font.BOLD, 20));
        holdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hold piece preview
        holdPreview = new PiecePreviewPanel();

        // SCORE label
        scoreLabel = new JLabel("SCORE");
        scoreValue = new JLabel("0");

        scoreLabel.setForeground(Color.WHITE);
        scoreValue.setForeground(Color.WHITE);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreValue.setFont(new Font("Arial", Font.BOLD, 20));

        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        add(Box.createVerticalStrut(50));

        add(holdLabel);
        add(Box.createVerticalStrut(10));
        add(holdPreview);

        add(Box.createVerticalStrut(50));

        add(scoreLabel);
        add(Box.createVerticalStrut(10));
        add(scoreValue);
    }

    public void setScore(int score) {
        scoreValue.setText(String.valueOf(score));
    }

    public void setHeldPiece(Tetromino piece) {
        holdPreview.setPiece(piece);
    }

    // Panel responsible for drawing the held piece
    private class PiecePreviewPanel extends JPanel {

        private Tetromino piece;

        public PiecePreviewPanel() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(130, 110));
            setMaximumSize(new Dimension(130, 110));
        }

        public void setPiece(Tetromino piece) {
            this.piece = piece;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (piece == null) {
                return;
            }

            int[][] shape = piece.getBaseShape();

            int cellSize = 20;

            int startX = (getWidth() - 4 * cellSize) / 2;
            int startY = (getHeight() - 4 * cellSize) / 2;

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
}
