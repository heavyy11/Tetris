package tetris;

import java.awt.Color;

public class OPiece extends Tetromino{

    @Override
    public int[][] getBaseShape() {
        return new int[][]{
                {0, 1, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }
    
}
