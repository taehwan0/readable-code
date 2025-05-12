package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.BoardIndexConverter;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;
import java.util.Scanner;

public class ConsoleInputHandler implements InputHandler {

    public static final Scanner SCANNER = new Scanner(System.in);

    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

    @Override
    public UserAction getUserActionFromUser() {
        String input = SCANNER.nextLine();

        if ("1".equals(input)) {
            return UserAction.OPEN;
        }

        if ("2".equals(input)) {
            return UserAction.FLAG;
        }

        return UserAction.UNKNOWN;

    }

    @Override
    public CellPosition getCellPositionFromUserInput() {
        String userInput = SCANNER.nextLine();

        int rowIndex = boardIndexConverter.convertRowFrom(userInput);
        int columnIndex = boardIndexConverter.convertColumnFrom(userInput);

        return CellPosition.of(rowIndex, columnIndex);
    }
}
