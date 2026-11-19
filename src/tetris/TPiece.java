package tetris;

import java.awt.Color;

public class TPiece extends Tetromino{

    @Override
    public int[][] getBaseShape() {
        return new int[][]{
                {0, 1, 0, 0},
                {1, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
    }

    @Override
    public Color getColor() {
        return new Color(168, 85, 212);
    }
    
}
