import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class SudokuSolver {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private boolean isSafe(int[][] board, int row, int col, int num, SudokuGUI gui) {
        Set<Point> rowHighlights = new HashSet<>();
        Set<Point> colHighlights = new HashSet<>();
        Set<Point> subgridHighlights = new HashSet<>();

        for (int x = 0; x < SIZE; x++) {
            rowHighlights.add(new Point(row, x));
            colHighlights.add(new Point(x, col));
            subgridHighlights.add(new Point(row - row % SUBGRID_SIZE + x / SUBGRID_SIZE,
                    col - col % SUBGRID_SIZE + x % SUBGRID_SIZE));
            gui.setHighlights(rowHighlights, colHighlights, subgridHighlights);
            try {
                Thread.sleep(5); // to visualize the process
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (board[row][x] == num || board[x][col] == num ||
                    board[row - row % SUBGRID_SIZE + x / SUBGRID_SIZE][col - col % SUBGRID_SIZE
                            + x % SUBGRID_SIZE] == num) {
                return false;
            }
        }
        return true;
    }

    public boolean solveSudokuWithGUI(int[][] board, SudokuGUI gui) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(board, row, col, num, gui)) {
                            board[row][col] = num;
                            gui.updateBoard(board);
                            try {
                                Thread.sleep(80); // to visualize the process
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (solveSudokuWithGUI(board, gui)) {
                                return true;
                            }
                            board[row][col] = 0;
                            gui.updateBoard(board);
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
