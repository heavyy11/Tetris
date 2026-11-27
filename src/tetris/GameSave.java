package tetris;

public class GameSave {

    public int score;
    public int[][] boardData;

    public String currentPieceType;
    public int currentX;
    public int currentY;
    public int currentRotation;

    public String nextPieceType;
    public String heldPieceType;

    public boolean hasHeldThisTurn;
}
