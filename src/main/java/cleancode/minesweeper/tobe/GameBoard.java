package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {

    private static final int LAND_MINE_COUNT = 10;

    private final Cell[][] board;
    private final int rowSize;
    private final int columnSize;

    public GameBoard(int rowSize, int columnSize) {
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.board = new Cell[rowSize][columnSize];
    }

    public void initializeGame() {
        // board에 UI를 입력하는 과정
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                board[row][column] = Cell.create();
            }
        }

        // board에 10개의 mine을 심는 과정, 중복이 될 수 있을 것으로 보임
        // 총 10개 이하의 mine이 생성될 것으로 유추됨
        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(columnSize);
            int row = new Random().nextInt(rowSize);
            findCell(row, col).plantMine();
        }

        // 모든 칸을 돌면서
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                if (isLandMineCell(row, column)) {
                    continue;
                }
                int count = countNearByLandMines(row, column);
                findCell(row, column).updateNearByLandMineCount(count);
            }
        }
    }

    public int countNearByLandMines(int row, int column) {
        int count = 0;
        if (row - 1 >= 0 && column - 1 >= 0 && isLandMineCell(row - 1, column - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, column)) {
            count++;
        }
        if (row - 1 >= 0 && column + 1 < rowSize && isLandMineCell(row - 1, column + 1)) {
            count++;
        }
        if (column - 1 >= 0 && isLandMineCell(row, column - 1)) {
            count++;
        }
        if (column + 1 < columnSize && isLandMineCell(row, column + 1)) {
            count++;
        }
        if (row + 1 < rowSize && column - 1 >= 0 && isLandMineCell(row + 1, column - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMineCell(row + 1, column)) {
            count++;
        }
        if (row + 1 < rowSize && column + 1 < columnSize && isLandMineCell(row + 1, column + 1)) {
            count++;
        }
        return count;
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColumnIndex) {
        return board[selectedRowIndex][selectedColumnIndex].isLandMineCell();
    }

    public String getSign(int rowIndex, int columnIndex) {
        Cell cell = findCell(rowIndex, columnIndex);
        return cell.getSign();
    }

    public Cell findCell(int rowIndex, int columnIndex) {
        return this.board[rowIndex][columnIndex];
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void flagCell(int rowIndex, int columnIndex) {
        Cell cell = findCell(rowIndex, columnIndex);
        cell.flag();
    }

    public boolean isOpenedCell(int rowIndex, int columnIndex) {
        Cell cell = findCell(rowIndex, columnIndex);
        return cell.isOpened();
    }

    public void openCell(int rowIndex, int columnIndex) {
        Cell cell = findCell(rowIndex, columnIndex);
        cell.open();
    }

    public void openSurroundedCell(int row, int col) {
        if (row < 0 || row >= rowSize || col < 0 || col >= columnSize) {
            return;
        }

        if (isOpenedCell(row, col)) {
            return;
        }

        if (isLandMineCell(row, col)) {
            return;
        }

        openCell(row, col);

        if (findCell(row, col).hasLandMineCount()) {
            return;
        }

        openSurroundedCell(row - 1, col - 1);
        openSurroundedCell(row - 1, col);
        openSurroundedCell(row - 1, col + 1);
        openSurroundedCell(row, col - 1);
        openSurroundedCell(row, col + 1);
        openSurroundedCell(row + 1, col - 1);
        openSurroundedCell(row + 1, col);
        openSurroundedCell(row + 1, col + 1);
    }

    public boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }
}
