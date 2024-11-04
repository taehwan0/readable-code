package cleancode.minesweeper.tobe.cell;

public class NumberCell implements Cell {

    private final int nearByLandMineCount;
    private final CellState cellState = CellState.initialize();

    public NumberCell(int count) {
        this.nearByLandMineCount = count;
    }

    @Override
    public String getSign() {
        if (cellState.isOpened()) {
            return String.valueOf(nearByLandMineCount);
        }
        if (cellState.isFlagged()) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }

    @Override
    public boolean isLandMineCell() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return true;
    }

    @Override
    public void flag() {
        cellState.flag();
    }

    @Override
    public void open() {
        cellState.open();
    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }

    @Override
    public boolean isChecked() {
        return cellState.isChecked();
    }
}
