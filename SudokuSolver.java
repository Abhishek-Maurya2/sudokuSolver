public class SudokuSolver {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
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
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            gui.updateBoard(board);
                            try {
                                Thread.sleep(100); // to visualize the process
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (solveSudokuWithGUI(board, gui)) {
                                return true;
                            }
                            board[row][col] = 0;
                            gui.updateBoard(board);
                            try {
                                Thread.sleep(100);
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
