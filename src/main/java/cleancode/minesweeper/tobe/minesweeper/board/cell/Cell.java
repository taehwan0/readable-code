package cleancode.minesweeper.tobe.minesweeper.board.cell;

public interface Cell {

    CellSnapshot getSnapshot();

    boolean isLandMineCell();

    boolean hasLandMineCount();

    void flag();

    void open();

    boolean isOpened();

    boolean isChecked();
}
