package cleancode.minesweeper.tobe.cell;

public interface Cell {

    String FLAG_SIGN = "⚑";
    String UNCHECKED_SIGN = "□";

    String getSign();

    boolean isLandMineCell();

    boolean hasLandMineCount();

    void flag();

    void open();

    boolean isOpened();

    boolean isChecked();
}
