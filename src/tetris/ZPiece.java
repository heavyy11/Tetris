package tetris;

import java.awt.Color;

public class ZPiece extends Tetromino{

    @Override
    public int[][] getShape() {
        return new int[][]{
                {1, 1, 0, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }
    
}
