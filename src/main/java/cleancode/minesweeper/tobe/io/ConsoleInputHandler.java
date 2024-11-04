package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.BoardIndexConverter;
import cleancode.minesweeper.tobe.position.CellPosition;
import java.util.Scanner;

public class ConsoleInputHandler implements InputHandler {

    public static final Scanner SCANNER = new Scanner(System.in);

    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

    @Override
    public String getUserInput() {
        return SCANNER.nextLine();
    }

    @Override
    public CellPosition getCellPositionFromUserInput() {
        String userInput = SCANNER.nextLine();

        char columInput = userInput.charAt(0);
        String rowInput = String.valueOf(userInput.charAt(1));

        int columnIndex = boardIndexConverter.convertColumnFrom(columInput);
        int rowIndex = boardIndexConverter.convertRowFrom(rowInput);

        return CellPosition.of(rowIndex, columnIndex);
    }
}
