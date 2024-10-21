package cleancode.minesweeper.tobe.cell;

public class NumberCell extends Cell {

    private final int nearByLandMineCount;

    public NumberCell(int count) {
        this.nearByLandMineCount = count;
    }

    @Override
    public String getSign() {
        if (isOpened) {
            return String.valueOf(nearByLandMineCount);
        }
        if (isFlagged) {
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
}
