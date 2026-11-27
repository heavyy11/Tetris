
package tetris;

import javax.swing.*;
import java.awt.*;

public class NavigationBar extends JPanel {

    private JButton pauseButton;
    private JButton restartButton;
    private JButton leaderboardButton;

    public NavigationBar() {

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        pauseButton = new JButton("Pause");
        restartButton = new JButton("Restart");
        leaderboardButton = new JButton("Leaderboard");
        
        pauseButton.setFocusable(false);
        restartButton.setFocusable(false);
        leaderboardButton.setFocusable(false);

        add(pauseButton);
        add(restartButton);
        add(leaderboardButton);
    }
    
    public void setRestartAction(Runnable action) {
        restartButton.addActionListener(e -> {
            action.run();
        });
    }
    
    public void setPauseAction(Runnable action) {
        pauseButton.addActionListener(e -> {
            action.run();
        });
    }

    public void setPauseText(String text) {
        pauseButton.setText(text);
    }
    
    public void setLeaderboardAction(Runnable action) {
        leaderboardButton.addActionListener(e -> {
            action.run();
        });
    }
}
