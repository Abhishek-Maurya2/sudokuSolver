import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;


public class SudokuGUI extends JPanel {
    private static final int SIZE = 9;
    private int[][] board;
    private Set<Point> rowHighlights = new HashSet<>();
    private Set<Point> colHighlights = new HashSet<>();
    private Set<Point> subgridHighlights = new HashSet<>();
    private JButton startButton;

    private void startSolver() {
        SudokuSolver solver = new SudokuSolver();
        new Thread(() -> {
            solver.solveSudokuWithGUI(board, this);
        }).start();
    }

    public SudokuGUI(int[][] board) {
        this.board = board;
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);
        frame.setLayout(new BorderLayout());

        this.setPreferredSize(new Dimension(600, 600));
        frame.add(this, BorderLayout.CENTER);

        startButton = new JButton("Start");
        frame.add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSolver();
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    public void updateBoard(int[][] board) {
        this.board = board;
        repaint(); // Repaint the board to reflect changes
    }

    public void setHighlights(Set<Point> rowHighlights, Set<Point> colHighlights, Set<Point> subgridHighlights) {
        this.rowHighlights = rowHighlights;
        this.colHighlights = colHighlights;
        this.subgridHighlights = subgridHighlights;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / SIZE;
        int height = getHeight() / SIZE;

        // Draw cells with different colors
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (rowHighlights.contains(new Point(row, col))) {
                    g.setColor(Color.RED);
                } else if (colHighlights.contains(new Point(row, col))) {
                    g.setColor(Color.GREEN);
                } else if (subgridHighlights.contains(new Point(row, col))) {
                    g.setColor(Color.BLUE);
                } else if (board[row][col] != 0) {
                    g.setColor(Color.WHITE); // Non-zero cells
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Empty cells
                }
                g.fillRect(col * width, row * height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(col * width, row * height, width, height);

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
                { 5, 3, 0, 0, 7, 0, 9, 0, 0 },
                { 6, 0, 0, 1, 9, 5, 3, 4, 0 },
                { 0, 9, 8, 3, 4, 0, 5, 6, 0 },
                { 8, 5, 9, 7, 6, 0, 0, 0, 3 },
                { 4, 0, 0, 8, 0, 3, 7, 0, 1 },
                { 7, 0, 0, 0, 2, 0, 8, 5, 6 },
                { 0, 6, 1, 0, 0, 0, 0, 8, 0 },
                { 0, 8, 0, 4, 1, 9, 0, 0, 5 },
                { 0, 4, 5, 0, 8, 0, 0, 7, 9 }
        };

        new SudokuGUI(board);
    }
}
