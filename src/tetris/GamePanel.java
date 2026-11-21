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
    
    private GameBoard board = new GameBoard();
    
    private Timer fallTimer;
    
    private boolean gameOver = false;
    
    private int score = 0;

    public GamePanel() {

        setBackground(Color.BLACK);

        piece = PieceFactory.createRandomPiece();
        
        bindKey("LEFT", "moveLeft", () -> movePiece(-1, 0));
        bindKey("RIGHT", "moveRight", () -> movePiece(1, 0));
        bindKey("DOWN", "moveDown", () -> movePiece(0, 1));
        bindKey("UP", "rotateCW", () -> rotatePiece(true));
        bindKey("Z", "rotateCCW", () -> rotatePiece(false));
        bindKey("SPACE", "hardDrop", () -> hardDrop());
        
        fallTimer = new Timer(1000, e -> {

            int newY = piece.getY() + 1;

            if (board.isValidPosition(piece.getShape(), piece.getX(), newY)) {

                piece.moveDown();

            } else {

                if (board.isAboveBoard(piece.getShape(), piece.getX(), piece.getY())) {

                    gameOver = true;
                    fallTimer.stop();
                    System.out.println("GAME OVER!");

                } else {

                    board.lockPiece(piece.getShape(), piece.getX(), piece.getY(), piece.getColor());

                    int cleared = board.clearFullLines();

                    updateScore(cleared);
                    
                    System.out.println("Lines cleared: " + cleared);
                    System.out.println("Score: " + score);

                    piece = PieceFactory.createRandomPiece();

                    // Check if the new piece overlaps existing blocks
                    if (!board.isValidPosition(piece.getShape(), piece.getX(), piece.getY())) {
                        gameOver = true;
                        fallTimer.stop();
                        System.out.println("GAME OVER!");
                    }
                }

            }

            repaint();
        });

        fallTimer.start();  
    }
    
    private void updateScore(int cleared) {

        switch (cleared) {

            case 1:
                score += 100;
                break;

            case 2:
                score += 300;
                break;

            case 3:
                score += 500;
                break;

            case 4:
                score += 800;
                break;
        }

        System.out.println("Score: " + score);
    }
    
    private static final int[][][] I_KICKS = {
        // 0 -> R
        {{0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2}},
        // R -> 2
        {{0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1}},
        // 2 -> L
        {{0, 0}, {2, 0}, {-1, 0}, {2, -1}, {-1, 2}},
        // L -> 0
        {{0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1}}
    };
    
    private static final int[][][] JLSTZ_KICKS = {
        // 0 -> R
        {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}},
        // R -> 2
        {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
        // 2 -> L
        {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
        // L -> 0
        {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}
    };
    
    private static final int[][][] I_CCW_KICKS = {
        // 0 -> L
        {{0, 0}, {2, 0}, {-1, 0}, {2, -1}, {-1, 2}},
        // R -> 0
        {{0, 0}, {1, 0}, {-2, 0}, {1, 2}, {-2, -1}},
        // 2 -> R
        {{0, 0}, {-2, 0}, {1, 0}, {-2, 1}, {1, -2}},
        // L -> 2
        {{0, 0}, {-1, 0}, {2, 0}, {-1, -2}, {2, 1}}
    };
    
    private static final int[][][] JLSTZ_CCW_KICKS = {
        // 0 -> L
        {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
        // R -> 0
        {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
        // 2 -> R
        {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}},
        // L -> 2
        {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}
    };
    
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
    
    private void movePiece(int dx, int dy) {
        if (gameOver) return;
        
        int newX = piece.getX() + dx;
        int newY = piece.getY() + dy;

        if (board.isValidPosition(piece.getShape(), newX, newY)) {

            if (dx < 0) {
                piece.moveLeft();
            } else if (dx > 0) {
                piece.moveRight();
            } else if (dy > 0) {
                piece.moveDown();
            }

        }

    }
    
    private void rotatePiece(boolean clockwise) {
        if (gameOver) return;

        // O-piece does not visibly rotate
        if (piece instanceof OPiece) {
            return;
        }

        // Save the current rotation
        int oldRotation = piece.getRotation();
        
        if (clockwise) {
            piece.rotate();
        } else {
            piece.setRotation((oldRotation + 3) % 4);
        }

        // Choose the appropriate kick table
        int[][] kicks;

        if (piece instanceof IPiece) {

            if (clockwise) {
                kicks = I_KICKS[oldRotation];
            } else {
                kicks = I_CCW_KICKS[oldRotation];
            }

        } else {

            if (clockwise) {
                kicks = JLSTZ_KICKS[oldRotation];
            } else {
                kicks = JLSTZ_CCW_KICKS[oldRotation];
            }

        }

        // Try all five kick positions
        for (int i = 0; i < kicks.length; i++) {

            int newX = piece.getX() + kicks[i][0];
            int newY = piece.getY() + kicks[i][1];

            if (board.isValidPosition(piece.getShape(), newX, newY)) {

                piece.setPosition(newX, newY);

                return;
            }
        }

        piece.setRotation(oldRotation);
    }
    
    private void hardDrop() {

        if (gameOver) {
            return;
        }

        int ghostY = getGhostY();

        piece.setPosition(piece.getX(), ghostY);

        board.lockPiece(
                piece.getShape(),
                piece.getX(),
                piece.getY(),
                piece.getColor()
        );

        int cleared = board.clearFullLines();

        updateScore(cleared);

        piece = PieceFactory.createRandomPiece();

        if (!board.isValidPosition(
                piece.getShape(),
                piece.getX(),
                piece.getY())) {

            gameOver = true;
            fallTimer.stop();
            System.out.println("GAME OVER!");
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

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

        g.drawString("SCORE", 400, 100);
        g.drawString(String.valueOf(score), 400, 130);
        
        if (!gameOver) {

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

                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                    g.setColor(Color.WHITE);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

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