package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class TetrisFrame extends JFrame{
    public static int width = 1000; 
    public static int height= 700;
    
    public static NavigationBar navigation;
    public static GamePanel app;
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private GameSaveDAO gameSaveDAO = new GameSaveDAO();
    
    private int currentUserId;
    private boolean wasPausedBeforeLeaderboard = false;
    
    
    public TetrisFrame() {

        setTitle("Tetris");
        setLayout(new BorderLayout(0, 0));
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                if (app != null) {
                    app.saveCurrentGame();
                }

                dispose();
                System.exit(0);
            }
        });
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        LoginPanel loginPanel = new LoginPanel();
        
        loginPanel.setLoginAction(userId -> {
            currentUserId = userId;
            System.out.println("Logged in User ID: " + currentUserId);

            checkSavedGame();
        });
        
        mainPanel.add(loginPanel, "LOGIN");

        add(mainPanel);

        // Show login first
        cardLayout.show(mainPanel, "LOGIN");
  
    }
    
    private void startGame(GameSave save) {

        navigation = new NavigationBar();

        app = new GamePanel(currentUserId, save);

        navigation.setRestartAction(() -> {
            app.restartGame();
            navigation.setPauseText("Pause");
        });

        navigation.setPauseAction(() -> {
            app.togglePause();

            if (app.isPaused()) {
                navigation.setPauseText("Resume");
            } else {
                navigation.setPauseText("Pause");
            }
        });

        JPanel gameScreen = new JPanel(new BorderLayout(0, 0));

        gameScreen.add(navigation, BorderLayout.NORTH);
        gameScreen.add(app, BorderLayout.CENTER);
        
        JPanel leaderboardScreen = new JPanel(new BorderLayout());

        LeaderboardPanel leaderboardPanel = new LeaderboardPanel();

        JButton backButton = new JButton("Back to Game");
        backButton.setFocusable(false);
        
        backButton.addActionListener(e -> {

            // Return to the game
            cardLayout.show(mainPanel, "GAME");

            // Resume only if the game wasn't paused before
            if (!wasPausedBeforeLeaderboard) {
                app.togglePause();
                navigation.setPauseText("Pause");
            }

        });
        
        leaderboardScreen.add(leaderboardPanel, BorderLayout.CENTER);
        leaderboardScreen.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(gameScreen, "GAME");
        mainPanel.add(leaderboardScreen, "LEADERBOARD");
        
        navigation.setLeaderboardAction(() -> {

            wasPausedBeforeLeaderboard = app.isPaused();

            // Pause the game if it is currently running
            if (!wasPausedBeforeLeaderboard) {
                app.togglePause();
                navigation.setPauseText("Resume");
            }

            cardLayout.show(mainPanel, "LEADERBOARD");

        });
        

        cardLayout.show(mainPanel, "GAME");

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void checkSavedGame() {

        try {

            GameSave save = gameSaveDAO.loadGame(currentUserId);

            // No unfinished game was found
            if (save == null) {
                startGame(null);
                return;
            }

            // An unfinished game was found
            Object[] options = {"Continue", "New Game"};

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "An unfinished game was found.\nWould you like to continue?",
                    "Saved Game Found",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {

                // Continue the previous game
                startGame(save);

            } else if (choice == 1) {

                // Delete the previous saved game
                gameSaveDAO.deleteSave(currentUserId);

                // Start a fresh game
                startGame(null);

            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load your saved game."
            );
        }
    }
}
