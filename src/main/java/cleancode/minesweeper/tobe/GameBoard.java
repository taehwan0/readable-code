package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.Cells;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
import cleancode.minesweeper.tobe.position.RelativePosition;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard {

    private final int landMineCount;
    private final int rowSize;
    private final int columnSize;
    private final Cell[][] board;
    private GameStatus gameStatus;

    public GameBoard(GameLevel gameLevel) {
        this.rowSize = gameLevel.getRowSize();
        this.columnSize = gameLevel.getColumnSize();
        this.landMineCount = gameLevel.getLandMineCount();
        this.board = new Cell[rowSize][columnSize];
        initializeGameStatus();
    }

    public void initializeGame() {
        initializeGameStatus();
        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landminePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandmineCells(landminePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landminePositions);
        initializeNumberCells(numberPositionCandidates);
    }

    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearByLandMines(candidatePosition);

            if (count != 0) {
                updateCellAtPosition(candidatePosition, new NumberCell(count));
            }
        }
    }

    private void initializeLandmineCells(List<CellPosition> landminePositions) {
        for (CellPosition position : landminePositions) {
            updateCellAtPosition(position, new LandMineCell());
        }
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAtPosition(position, new EmptyCell());
        }
    }

    private void updateCellAtPosition(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColumnIndex()] = cell;
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

        checkGameIsOver();
    }

    private void checkGameIsOver() {
        if (isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }

    public boolean isOpenedCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isOpened();
    }

    public void openOneCellAt(CellPosition cellPosition) {
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

        openOneCellAt(cellPosition);

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
        return Cells.from(board)
                .isAllChecked();
    }

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColumnIndexMoreThanOrEqual(columnSize);
    }

    public void openAt(CellPosition cellPosition) {
        if (isLandMineCell(cellPosition)) {
            openOneCellAt(cellPosition);
            changeGameStatusToLose();
            return;
        }

        openSurroundedCell(cellPosition);
        checkGameIsOver();
    }

    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }
}
