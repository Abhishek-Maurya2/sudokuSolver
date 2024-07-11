import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverVisualizer {
    private static final int SIZE = 9;
    private static JTextField[][] cells = new JTextField[SIZE][SIZE];
    private static JFrame frame;
    private static JButton startButton;
    private static JButton resetButton;
    private static int[][] initialBoard;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Sudoku Solver Visualizer");
            frame.setLayout(new BorderLayout());
            JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
            // Initialize Sudoku board
            int[][] board = {
                    { 5, 3, 0, 6, 7, 8, 0, 0, 0 },
                    { 6, 7, 0, 1, 9, 5, 0, 4, 0 },
                    { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
                    { 8, 0, 9, 0, 6, 0, 0, 0, 3 },
                    { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
                    { 7, 0, 0, 0, 2, 0, 8, 0, 6 },
                    { 9, 6, 0, 0, 0, 0, 2, 8, 0 },
                    { 0, 0, 0, 4, 1, 9, 0, 0, 5 },
                    { 0, 0, 0, 0, 8, 0, 0, 7, 9 }
            };
            // Store initial board state
            initialBoard = new int[SIZE][SIZE];
            for (int row = 0; row < SIZE; row++) {
                System.arraycopy(board[row], 0, initialBoard[row], 0, SIZE);
            }
            // Create JTextFields for each cell
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    JTextField textField = new JTextField();
                    textField.setHorizontalAlignment(JTextField.CENTER);
                    textField.setFont(new Font("Arial", Font.BOLD, 20));
                    if (board[row][col] == 0) {
                        textField.setBackground(Color.GRAY); // Color for empty cells
                    } else {
                        textField.setText(String.valueOf(board[row][col]));
                        textField.setEditable(false);
                    }
                    cells[row][col] = textField;
                    gridPanel.add(textField);
                }
            }

            frame.add(gridPanel, BorderLayout.CENTER);

            // Create and add the start button
            startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(() -> solveSudoku(board)).start(); // Call solver function in a new thread
                }
            });

            // Create and add the reset button
            resetButton = new JButton("Reset");
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetBoard(board); // Reset the board to the initial state
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(startButton);
            buttonPanel.add(resetButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static boolean solveSudoku(int[][] board) {
        // Backtracking algorithm to solve Sudoku
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) { // Find empty cell
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            cells[row][col].setText(String.valueOf(num));
                            cells[row][col].setBackground(Color.WHITE);

                            // Visualize solving process
                            try {
                                Thread.sleep(80); // Add delay for visualization
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            frame.repaint(); // Refresh frame to show updates

                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = 0; // Backtrack
                                cells[row][col].setText("");
                                cells[row][col].setBackground(Color.RED); // Highlight backtracking cells in red

                                // Visualize backtracking process
                                try {
                                    Thread.sleep(80); // Add delay for visualization
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                frame.repaint(); // Refresh frame to show updates
                            }
                        }
                    }
                    return false; // No valid number found for this cell
                }
            }
        }
        return true; // Sudoku solved
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check if num can be placed at board[row][col] without violating Sudoku rules
        return !usedInRow(board, row, num) &&
                !usedInCol(board, col, num) &&
                !usedInBox(board, row - row % 3, col - col % 3, num);
    }

    private static boolean usedInRow(int[][] board, int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInCol(int[][] board, int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInBox(int[][] board, int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void resetBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = initialBoard[row][col];
                if (initialBoard[row][col] == 0) {
                    cells[row][col].setText("");
                    cells[row][col].setBackground(Color.GRAY);
                    cells[row][col].setEditable(true);
                } else {
                    cells[row][col].setText(String.valueOf(initialBoard[row][col]));
                    cells[row][col].setBackground(Color.WHITE);
                    cells[row][col].setEditable(false);
                }
            }
        }
        frame.repaint();
    }
}
