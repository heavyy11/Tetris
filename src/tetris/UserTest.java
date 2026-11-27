
package tetris;

import java.sql.SQLException;

public class UserTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        try {

            boolean registered = userDAO.registerUser(
                    "player1",
                    "tetris123"
            );

            if (registered) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Username already exists!");
            }

            boolean loggedIn = userDAO.loginUser(
                    "player1",
                    "tetris123"
            );

            if (loggedIn) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }

        } catch (SQLException e) {

            System.out.println("Database error!");
            e.printStackTrace();
        }
    }
}
