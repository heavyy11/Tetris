package tetris;

public class Main {

    public static void main(String[] args) {
       javax.swing.SwingUtilities.invokeLater(() -> {
            TetrisFrame frame = new TetrisFrame();
            frame.setVisible(true);
        });
    }
    
}
