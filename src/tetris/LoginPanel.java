
package tetris;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JPasswordField confirmPasswordField;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Consumer<Integer> loginAction;
    private UserDAO userDAO = new UserDAO();

    public LoginPanel() {

        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createRegisterPanel();

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "LOGIN");
    }

    public void setLoginAction(Consumer<Integer> action) {
        loginAction = action;
    }

    private void createLoginPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("PLAYER LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your username and password."
                );

                return;
            }

            try {

                boolean loggedIn = userDAO.loginUser(username, password);

                if (loggedIn) {

                    int userId = userDAO.getUserId(username);

                    JOptionPane.showMessageDialog(
                            this,
                            "Welcome, " + username + "!"
                    );

                    if (loginAction != null) {
                        loginAction.accept(userId);
                    }
                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Incorrect username or password."
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Database error: " + ex.getMessage()
                );
            }
        });

        registerButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "REGISTER");
        });

        mainPanel.add(panel, "LOGIN");
    }

    private void createRegisterPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        registerUsernameField = new JTextField(15);
        registerPasswordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(registerUsernameField);

        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(registerPasswordField);

        inputPanel.add(new JLabel("Confirm Password:"));
        inputPanel.add(confirmPasswordField);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> {

            String username = registerUsernameField.getText().trim();

            String password = new String(
                    registerPasswordField.getPassword()
            );

            String confirmPassword = new String(
                    confirmPasswordField.getPassword()
            );

            if (username.isEmpty() || password.isEmpty()
                    || confirmPassword.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields."
                );

                return;
            }

            if (!password.equals(confirmPassword)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Passwords do not match."
                );

                return;
            }

            if (password.length() < 8) {

                JOptionPane.showMessageDialog(
                        this,
                        "Password must be at least 8 characters."
                );

                return;
            }

            try {

                boolean registered = userDAO.registerUser(username, password);

                if (registered) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Account created successfully! You can now log in."
                    );

                    // Clear registration fields
                    registerUsernameField.setText("");
                    registerPasswordField.setText("");
                    confirmPasswordField.setText("");

                    // Return to login screen
                    cardLayout.show(mainPanel, "LOGIN");

                    // Put the new username in the login field
                    usernameField.setText(username);

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Username already exists. Please choose another."
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Database error: " + ex.getMessage()
                );
            }
        });

        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "LOGIN");
        });

        mainPanel.add(panel, "REGISTER");
    }
}
