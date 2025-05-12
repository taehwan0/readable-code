package cleancode.minesweeper.tobe.minesweeper.gamelevel;

public class VeryBeginner implements GameLevel {

    @Override
    public int getRowSize() {
        return 4;
    }

    @Override
    public int getColumnSize() {
        return 5;
    }

    @Override
    public int getLandMineCount() {
        return 2;
    }
}
