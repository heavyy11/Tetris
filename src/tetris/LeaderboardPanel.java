
package tetris;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private ScoreDAO scoreDAO = new ScoreDAO();

    private JTable leaderboardTable;
    private DefaultTableModel tableModel;

    public LeaderboardPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        String[] columns = {"Rank", "Username", "Highest Score"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaderboardTable = new JTable(tableModel);

        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardTable.setRowHeight(30);

        leaderboardTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        leaderboardTable.setBackground(Color.DARK_GRAY);
        leaderboardTable.setForeground(Color.WHITE);
        leaderboardTable.getTableHeader().setBackground(Color.GRAY);
        leaderboardTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);

        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> loadLeaderboard());

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        loadLeaderboard();
    }

    private void loadLeaderboard() {

        try {

            tableModel.setRowCount(0);

            List<Object[]> leaderboard = scoreDAO.getLeaderboard();

            int rank = 1;

            for (Object[] player : leaderboard) {

                String username = (String) player[0];
                int highScore = (int) player[1];

                tableModel.addRow(new Object[]{
                    rank,
                    username,
                    highScore
                });

                rank++;
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load leaderboard."
            );
        }
    }
}
