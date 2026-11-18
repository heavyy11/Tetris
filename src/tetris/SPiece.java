package tetris;

import java.awt.Color;

public class SPiece extends Tetromino{

    @Override
    public int[][] getShape() {
        return new int[][]{
                {0, 1, 1, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }
    
}
