import javax.swing.*;
import java.awt.*;

public class SudokuGUI extends JPanel {
    private static final int SIZE = 9;
    private int[][] board;

    public SudokuGUI(int[][] board) {
        this.board = board;
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(this);
        frame.setVisible(true);
    }

    public void updateBoard(int[][] board) {
        this.board = board;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / SIZE;
        int height = getHeight() / SIZE;
        // Fill empty cells with a different color
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(col * width, row * height, width, height);
                }
            }
        }
        // Draw numbers
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(board[row][col]), col * width + width / 2, row * height + height / 2);
                }
            }
        }
        // Draw grid lines
        g.setColor(Color.BLACK);
        for (int i = 0; i <= SIZE; i++) {
            g.drawLine(i * width, 0, i * width, getHeight());
            g.drawLine(0, i * height, getWidth(), i * height);
        }
        // Draw thicker grid lines for 3x3 boxes
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3)); // Set the stroke width to 3
        for (int i = 0; i <= SIZE; i += 3) {
            g2.drawLine(i * width, 0, i * width, getHeight());
            g2.drawLine(0, i * height, getWidth(), i * height);
        }
    }

    public static void main(String[] args) {
        int[][] board = new int[][] {
                { 5, 3, 0, 0, 7, 0, 0, 0, 0 },
                { 6, 0, 0, 1, 9, 5, 3, 4, 0 },
                { 0, 9, 8, 3, 4, 0, 0, 6, 0 },
                { 8, 5, 9, 7, 6, 0, 0, 0, 3 },
                { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
                { 7, 0, 0, 0, 2, 0, 8, 5, 6 },
                { 0, 6, 1, 0, 0, 0, 0, 8, 0 },
                { 0, 8, 0, 4, 1, 9, 0, 0, 5 },
                { 0, 0, 0, 0, 8, 0, 0, 7, 9 }
        };

        SudokuSolver solver = new SudokuSolver();
        SudokuGUI gui = new SudokuGUI(board);

        new Thread(() -> {
            solver.solveSudokuWithGUI(board, gui);
        }).start();
    }
}
