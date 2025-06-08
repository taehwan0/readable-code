package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.board.cell.Cell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.Cells;
import cleancode.minesweeper.tobe.minesweeper.board.cell.EmptyCell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.LandMineCell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.NumberCell;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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

	private void initializeGameStatus() {
		gameStatus = GameStatus.IN_PROGRESS;
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition position : allPositions) {
			updateCellAtPosition(position, new EmptyCell());
		}
	}

	private void initializeLandmineCells(List<CellPosition> landminePositions) {
		for (CellPosition position : landminePositions) {
			updateCellAtPosition(position, new LandMineCell());
		}
	}

	private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
		for (CellPosition candidatePosition : numberPositionCandidates) {
			int count = countNearByLandMines(candidatePosition);

			if (count != 0) {
				updateCellAtPosition(candidatePosition, new NumberCell(count));
			}
		}
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();

		checkGameIsOver();
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

	private void openOneCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	private void openSurroundedCell(CellPosition cellPosition) {
		Deque<CellPosition> stack = new LinkedList<>();
		stack.push(cellPosition);

		while (!stack.isEmpty()) {
			openAndPushCellAt(stack);
		}
	}

	private void openAndPushCellAt(Deque<CellPosition> stack) {
		CellPosition currentCellPosition = stack.pop();

		if (isOpenedCell(currentCellPosition)) {
			return;
		}

		if (isLandMineCell(currentCellPosition)) {
			return;
		}

		openOneCellAt(currentCellPosition);

		if (findCell(currentCellPosition).hasLandMineCount()) {
			return;
		}

		int rowIndex = currentCellPosition.getRowIndex();
		int columnIndex = currentCellPosition.getColumnIndex();

		calculateSurroundedPositions(currentCellPosition, rowIndex, columnIndex).forEach(stack::push);
	}

	private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowIndex, int columnIndex) {
		return RelativePosition.SURROUNDED_POSITIONS
				.stream()
				.filter(cellPosition::canCalculatePositionBy)
				.map(cellPosition::calculatePositionBy)
				.filter(position -> cellPosition.isRowIndexLessThan(rowIndex))
				.filter(position -> cellPosition.isColumnIndexLessThan(columnIndex))
				.toList();
	}

	private void updateCellAtPosition(CellPosition position, Cell cell) {
		board[position.getRowIndex()][position.getColumnIndex()] = cell;
	}

	private void checkGameIsOver() {
		if (isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = GameStatus.WIN;
	}

	private void changeGameStatusToLose() {
		gameStatus = GameStatus.LOSE;
	}

	public boolean isInProgress() {
		return gameStatus == GameStatus.IN_PROGRESS;
	}

	public boolean isWinStatus() {
		return gameStatus == GameStatus.WIN;
	}

	public boolean isLoseStatus() {
		return gameStatus == GameStatus.LOSE;
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColumnIndexMoreThanOrEqual(columnSize);
	}

	private boolean isLandMineCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColumnIndex()].isLandMineCell();
	}

	private boolean isOpenedCell(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isOpened();
	}

	private boolean isAllCellChecked() {
		return Cells.from(board)
				.isAllChecked();
	}

	public CellSnapshot getSnapshot(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.getSnapshot();
	}

	public Cell findCell(CellPosition cellPosition) {
		return this.board[cellPosition.getRowIndex()][cellPosition.getColumnIndex()];
	}

	public int countNearByLandMines(CellPosition cellPosition) {
		int rowIndex = cellPosition.getRowIndex();
		int columnIndex = cellPosition.getColumnIndex();

		return (int) calculateSurroundedPositions(cellPosition, rowIndex, columnIndex)
				.stream()
				.filter(this::isLandMineCell)
				.count();
	}

	public int getRowSize() {
		return rowSize;
	}

	public int getColumnSize() {
		return columnSize;
	}
}