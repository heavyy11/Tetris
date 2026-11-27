package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private Tetromino piece;
    private Tetromino heldPiece = null;
    private boolean hasHeldThisTurn = false;
    private Tetromino nextPiece;
    
    private GameBoard board = new GameBoard();
    private BoardPanel boardPanel;
    private StatsPanel statsPanel;
    private NextPanel nextPanel;
    
    private Timer fallTimer;
    
    private boolean gameOver = false;
    
    private int score = 0;

    public GamePanel() {

        setLayout(new FlowLayout(FlowLayout.CENTER));
        boardPanel = new BoardPanel(board);
        statsPanel = new StatsPanel();
        nextPanel = new NextPanel();
        add(statsPanel);
        add(boardPanel);
        add(nextPanel);
//        boardPanel.setPiece(piece);
        piece = PieceFactory.createRandomPiece();
        nextPiece = PieceFactory.createRandomPiece();
        
        bindKey("LEFT", "moveLeft", () -> movePiece(-1, 0));
        bindKey("RIGHT", "moveRight", () -> movePiece(1, 0));
        bindKey("DOWN", "moveDown", () -> movePiece(0, 1));
        bindKey("UP", "rotateCW", () -> rotatePiece(true));
        bindKey("Z", "rotateCCW", () -> rotatePiece(false));
        bindKey("SPACE", "hardDrop", () -> hardDrop());
        bindKey("C", "hold", () -> holdPiece());
        
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
                    
                    hasHeldThisTurn = false;
                    spawnNextPiece();

                    // Check if the new piece overlaps existing blocks
                    if (!board.isValidPosition(piece.getShape(), piece.getX(), piece.getY())) {
                        gameOver = true;
                        fallTimer.stop();
                        System.out.println("GAME OVER!");
                    }
                }

            }

            updateBoardPanel();
        });

        fallTimer.start();  
    }
    
    private void updateBoardPanel() {

        boardPanel.setPiece(piece);
        boardPanel.setGhostY(getGhostY());
        boardPanel.setGameOver(gameOver);
        statsPanel.setScore(score);
        statsPanel.setHeldPiece(heldPiece);
        nextPanel.setPiece(nextPiece);

        boardPanel.repaint();
        statsPanel.repaint();
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

                updateBoardPanel();

            }

        });
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
        
        hasHeldThisTurn = false;
        spawnNextPiece();

        if (!board.isValidPosition(
                piece.getShape(),
                piece.getX(),
                piece.getY())) {

            gameOver = true;
            fallTimer.stop();
            System.out.println("GAME OVER!");
        }

        updateBoardPanel();
    }
    
    private void holdPiece() {

        if (gameOver || hasHeldThisTurn) {
            return;
        }

        // If the hold area is empty
        if (heldPiece == null) {

            heldPiece = piece;
            spawnNextPiece();

        } else {

            // Swap the current piece with the held piece
            Tetromino temp = piece;

            piece = heldPiece;
            heldPiece = temp;

        }

        // Reset the incoming piece
        piece.setPosition(3, -2);
        piece.setRotation(0);

        hasHeldThisTurn = true;

        // Check whether the incoming piece can spawn
        if (!board.isValidPosition(
                piece.getShape(),
                piece.getX(),
                piece.getY())) {

            gameOver = true;
            fallTimer.stop();
            System.out.println("GAME OVER!");
        }

        updateBoardPanel();
    }
    
    private void spawnNextPiece() {

        piece = nextPiece;
        nextPiece = PieceFactory.createRandomPiece();

        piece.setPosition(3, -2);
        piece.setRotation(0);

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
    
}