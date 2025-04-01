package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.exception.GameException;
import cleancode.minesweeper.tobe.io.sign.CellSignProvider;
import cleancode.minesweeper.tobe.position.CellPosition;
import java.util.List;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler {

//    private final CellSignFinder cellSignFinder = new CellSignFinder();

    @Override
    public void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void showBoard(GameBoard gameBoard) {
        String columnAlphabets = generateColumnAlphabets(gameBoard.getColumnSize());

        System.out.println("    " + columnAlphabets);
        for (int row = 0; row < gameBoard.getRowSize(); row++) {
            System.out.printf("%2d  ", row + 1);
            for (int column = 0; column < gameBoard.getColumnSize(); column++) {
                CellPosition cellPosition = CellPosition.of(row, column);

                CellSnapshot cellSnapshot = gameBoard.getSnapshot(cellPosition);
//                String cellSign = cellSignFinder.findCellSignFrom(cellSnapshot);
                String cellSign = CellSignProvider.findCellSignFrom(cellSnapshot);
                System.out.print(cellSign + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String generateColumnAlphabets(int columnSize) {
        List<String> alphabet = IntStream.range(0, columnSize)
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .toList();
        return String.join(" ", alphabet);
    }

    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    @Override
    public void showWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    @Override
    public void showInputCoordinateComment() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }

    @Override
    public void showInputUserActionComment() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    @Override
    public void showExceptionMessage(GameException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void showSimpleExceptionMessage(String message) {
        System.out.println(message);
    }
}
