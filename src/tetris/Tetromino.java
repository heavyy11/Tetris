package tetris;

import java.awt.Color;

public abstract class Tetromino {
    private int x;
    private int y;
    
    public Tetromino(){
        x = 3;
        y = 0;
    }
    
    public abstract int[][] getShape();
    
    public abstract Color getColor();
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void moveLeft(){
        x--;
    }
    
    public void moveRight(){
        x++;
    }
    
    public void moveDown(){
        y--;
    }
}
