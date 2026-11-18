package tetris;

import javax.swing.JFrame;

public class TetrisFrame extends JFrame{
    public TetrisFrame() {

        setTitle("Tetris");
        setSize(600, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(false);
        
        add(new GamePanel());

    }
}
