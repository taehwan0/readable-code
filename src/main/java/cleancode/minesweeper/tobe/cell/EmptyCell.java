package cleancode.minesweeper.tobe.cell;

public class EmptyCell extends Cell {

    private static final String EMPTY_SIGN = "■";

    @Override
    public String getSign() {
        if (isOpened) {
            return EMPTY_SIGN;
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
        return false;
    }
}
