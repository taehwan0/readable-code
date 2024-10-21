package cleancode.minesweeper.tobe.cell;

public class LandMineCell extends Cell {

    private static final String LAND_MIND_SIGN = "☼";

    @Override
    public String getSign() {
        if (isOpened) {
            return LAND_MIND_SIGN;
        }
        if (isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }

    @Override
    public boolean isLandMineCell() {
        return true;
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }
}
