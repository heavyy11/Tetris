package tetris;

import java.awt.Color;

public abstract class Tetromino {
    private int x;
    private int y;
    private int rotation;
    
    public Tetromino(){
        x = 3;
        y = 0;
        rotation = 0;
    }
    
    public abstract int[][] getBaseShape();
    
    public abstract Color getColor();
    
    public int[][] getShape(){
        
        int[][] shape = getBaseShape();
        
        if (this instanceof OPiece) {
            return shape;
        }
        
        for(int r = 0; r < rotation; r++){
            
            int[][] rotated = new int[4][4];
            
            if(this instanceof IPiece){
            
                for(int row = 0; row < 4; row++){

                    for(int col = 0; col < 4; col++){

                        rotated[col][3 - row] = shape[row][col];

                    }
                }
                
            } else {
                
                for(int row = 0; row < 3; row++){

                    for(int col = 0; col < 3; col++){

                        rotated[col][2 - row] = shape[row][col];

                    }
                }
                
            }
            
            shape = rotated;
        }
        
        return shape;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void rotate(){
        rotation = (rotation + 1) % 4;
    }
    
    public void moveLeft(){
        x--;
    }
    
    public void moveRight(){
        x++;
    }
    
    public void moveDown(){
        y++;
    }
}
