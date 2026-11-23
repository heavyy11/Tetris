package tetris;

import javax.swing.*;
import java.awt.*;

public class TetrisFrame extends JFrame{
    public static int width = 1000; 
    public static int height= 700;
    
    //public static NavigationBar navigation;
    public static GamePanel app;
    
    public TetrisFrame() {

        setTitle("Tetris");
        setLayout(new BorderLayout(0, 0));
        setSize(width, height);
        
        //navigation= new NavigationBar(); 
        app       = new GamePanel(); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(false);
        
        //add(navigation, BorderLayout.NORTH);
        add(app, BorderLayout.CENTER);
    }
}
