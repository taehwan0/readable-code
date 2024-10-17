package cleancode.minesweeper.tobe.gamelevel;

public class Middle implements GameLevel {

    @Override
    public int getLandMineCount() {
        return 14;
    }

    @Override
    public int getColumnSize() {
        return 18;
    }

    @Override
    public int getRowSize() {
        return 40;
    }
}
