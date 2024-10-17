package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.exception.GameException;

public class BoardIndexConverter {

    private static final char BASE_CHAR_FOR_COL = 'a';

    public int convertRowFrom(String cellInputRow, int rowSize) {
        int rowIndex = Integer.parseInt(cellInputRow) - 1;

        if (rowIndex >= rowSize || rowIndex < 0) {
            throw new GameException("잘못된 좌표(행) 입력입니다. (" + rowIndex + ")");
        }

        return rowIndex;
    }

    public int convertColumnFrom(char cellInputColumn, int columSize) {
        int colIndex = cellInputColumn - BASE_CHAR_FOR_COL;

        if (colIndex >= columSize || colIndex < 0) {
            throw new GameException("잘못된 좌표(열) 입력입니다. (" + cellInputColumn + ")");
        }

        return colIndex;
    }
}
