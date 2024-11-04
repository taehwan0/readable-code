package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.RelativePosition;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard {

    private final int landMineCount;
    private final int rowSize;
    private final int columnSize;
    private final Cell[][] board;

    public GameBoard(GameLevel gameLevel) {
        this.rowSize = gameLevel.getRowSize();
        this.columnSize = gameLevel.getColumnSize();
        this.landMineCount = gameLevel.getLandMineCount();
        this.board = new Cell[rowSize][columnSize];
    }

    public void initializeGame() {
        // board에 UI를 입력하는 과정
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                board[row][column] = new EmptyCell();
            }
        }

        // board에 10개의 mine을 심는 과정, 중복이 될 수 있을 것으로 보임
        // 총 10개 이하의 mine이 생성될 것으로 유추됨
        for (int i = 0; i < landMineCount; i++) {
            int col = new Random().nextInt(columnSize);
            int row = new Random().nextInt(rowSize);
            board[row][col] = new LandMineCell();
        }

        // 모든 칸을 돌면서
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                CellPosition cellPosition = CellPosition.of(row, column);
                if (isLandMineCell(cellPosition)) {
                    continue;
                }
                int count = countNearByLandMines(cellPosition);

                if (count == 0) {
                    continue;
                }

                board[row][column] = new NumberCell(count);
            }
        }
    }

    public int countNearByLandMines(CellPosition cellPosition) {
        int rowIndex = cellPosition.getRowIndex();
        int columnIndex = cellPosition.getColumnIndex();

        return (int) calculateSurroundedPositions(cellPosition, rowIndex, columnIndex)
                .stream()
                .filter(this::isLandMineCell)
                .count();
    }

    public boolean isLandMineCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColumnIndex()].isLandMineCell();
    }

    public String getSign(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSign();
    }

    public Cell findCell(CellPosition cellPosition) {
        return this.board[cellPosition.getRowIndex()][cellPosition.getColumnIndex()];
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();
    }

    public boolean isOpenedCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isOpened();
    }

    public void openCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.open();
    }

    public void openSurroundedCell(CellPosition cellPosition) {
        if (isOpenedCell(cellPosition)) {
            return;
        }

        if (isLandMineCell(cellPosition)) {
            return;
        }

        openCell(cellPosition);

        if (findCell(cellPosition).hasLandMineCount()) {
            return;
        }

        int rowIndex = cellPosition.getRowIndex();
        int columnIndex = cellPosition.getColumnIndex();

        calculateSurroundedPositions(cellPosition, rowIndex, columnIndex).forEach(this::openSurroundedCell);
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowIndex, int columnIndex) {
        return RelativePosition.SURROUNDED_POSITIONS
                .stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> cellPosition.isRowIndexLessThan(rowIndex))
                .filter(position -> cellPosition.isColumnIndexLessThan(columnIndex))
                .collect(Collectors.toList());
    }

    public boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked);
    }

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColumnIndexMoreThanOrEqual(columnSize);
    }
}
