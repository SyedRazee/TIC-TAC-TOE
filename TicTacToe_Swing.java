import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe_Swing extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true; // X starts
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private int xScore = 0, oScore = 0, drawScore = 0;
    private boolean gameOver = false;

    public TicTacToe_Swing() {
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top panel: title and status
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel title = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(title);

        statusLabel = new JLabel("Turn: X", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center: game board
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font btnFont = new Font("SansSerif", Font.BOLD, 36);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton b = new JButton("");
                b.setFont(btnFont);
                b.setFocusPainted(false);
                final int rr = r, cc = c;
                b.addActionListener(e -> handleMove(rr, cc));
                buttons[r][c] = b;
                boardPanel.add(b);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Bottom: controls and score
        JPanel bottom = new JPanel(new BorderLayout());

        // Score label
        scoreLabel = new JLabel(getScoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bottom.add(scoreLabel, BorderLayout.NORTH);

        // Buttons: Restart and Reset Scores
        JPanel controls = new JPanel(new FlowLayout());
        JButton restartBtn = new JButton("Restart Board");
        restartBtn.addActionListener(e -> resetBoard());
        controls.add(restartBtn);

        JButton resetScores = new JButton("Reset Scores");
        resetScores.addActionListener(e -> resetScores());
        controls.add(resetScores);

        bottom.add(controls, BorderLayout.CENTER);

        // Helpful hint
        JLabel hint = new JLabel("Click any square to play. X goes first.", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        bottom.add(hint, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
    }

    private void handleMove(int r, int c) {
        if (gameOver) return;
        JButton b = buttons[r][c];
        if (!b.getText().isEmpty()) return; // already played

        b.setText(xTurn ? "X" : "O");
        b.setForeground(xTurn ? Color.BLUE : new Color(0x8B0000));

        if (checkWin()) {
            gameOver = true;
            String winner = xTurn ? "X" : "O";
            statusLabel.setText("Winner: " + winner);
            if (xTurn) xScore++; else oScore++;
            scoreLabel.setText(getScoreText());
            highlightWinningLine();
            return;
        }

        if (isBoardFull()) {
            gameOver = true;
            statusLabel.setText("It's a Draw!");
            drawScore++;
            scoreLabel.setText(getScoreText());
            return;
        }

        xTurn = !xTurn;
        statusLabel.setText("Turn: " + (xTurn ? "X" : "O"));
    }

    private boolean isBoardFull() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (buttons[r][c].getText().isEmpty()) return false;
        return true;
    }

    // Returns true if current player (the one who just played) has won
    private boolean checkWin() {
        String sym = xTurn ? "X" : "O";
        // rows
        for (int r = 0; r < 3; r++) {
            if (sym.equals(buttons[r][0].getText()) && sym.equals(buttons[r][1].getText()) && sym.equals(buttons[r][2].getText())) {
                buttons[r][0].putClientProperty("win", true);
                buttons[r][1].putClientProperty("win", true);
                buttons[r][2].putClientProperty("win", true);
                return true;
            }
        }
        // cols
        for (int c = 0; c < 3; c++) {
            if (sym.equals(buttons[0][c].getText()) && sym.equals(buttons[1][c].getText()) && sym.equals(buttons[2][c].getText())) {
                buttons[0][c].putClientProperty("win", true);
                buttons[1][c].putClientProperty("win", true);
                buttons[2][c].putClientProperty("win", true);
                return true;
            }
        }
        // diag
        if (sym.equals(buttons[0][0].getText()) && sym.equals(buttons[1][1].getText()) && sym.equals(buttons[2][2].getText())) {
            buttons[0][0].putClientProperty("win", true);
            buttons[1][1].putClientProperty("win", true);
            buttons[2][2].putClientProperty("win", true);
            return true;
        }
        if (sym.equals(buttons[0][2].getText()) && sym.equals(buttons[1][1].getText()) && sym.equals(buttons[2][0].getText())) {
            buttons[0][2].putClientProperty("win", true);
            buttons[1][1].putClientProperty("win", true);
            buttons[2][0].putClientProperty("win", true);
            return true;
        }
        return false;
    }

    private void highlightWinningLine() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Object win = buttons[r][c].getClientProperty("win");
                if (win != null && (Boolean) win) {
                    buttons[r][c].setBackground(Color.YELLOW);
                } else {
                    buttons[r][c].setBackground(null);
                }
            }
        }
    }

    private void resetBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                buttons[r][c].setText("");
                buttons[r][c].setBackground(null);
                buttons[r][c].putClientProperty("win", null);
            }
        }
        gameOver = false;
        xTurn = true;
        statusLabel.setText("Turn: X");
    }

    private void resetScores() {
        xScore = 0; oScore = 0; drawScore = 0;
        scoreLabel.setText(getScoreText());
        resetBoard();
    }

    private String getScoreText() {
        return String.format("Scores — X: %d   O: %d   Draws: %d", xScore, oScore, drawScore);
    }

    public static void main(String[] args) {
        // Ensure GUI is created on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            TicTacToe_Swing ttt = new TicTacToe_Swing();
            ttt.setVisible(true);
        });
    }
}
