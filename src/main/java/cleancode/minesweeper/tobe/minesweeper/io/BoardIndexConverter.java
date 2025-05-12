package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.minesweeper.tobe.minesweeper.exception.GameException;

public class BoardIndexConverter {

    private static final char BASE_CHAR_FOR_COL = 'a';

    public int convertRowFrom(String userInput) {
        String rowInput = String.valueOf(userInput.charAt(1));

        int rowIndex = Integer.parseInt(rowInput) - 1;

        if (rowIndex < 0) {
            throw new GameException("잘못된 좌표(행) 입력입니다. (" + rowIndex + ")");
        }

        return rowIndex;
    }

    public int convertColumnFrom(String userInput) {
        char columInput = userInput.charAt(0);

        int colIndex = columInput - BASE_CHAR_FOR_COL;

        if (colIndex < 0) {
            throw new GameException("잘못된 좌표(열) 입력입니다. (" + columInput + ")");
        }

        return colIndex;
    }
}
