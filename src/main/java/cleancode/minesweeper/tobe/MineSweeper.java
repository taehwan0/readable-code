package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.exception.GameException;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class MineSweeper {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COLUMN_SIZE = 10;

    private final GameBoard gameBoard = new GameBoard(BOARD_ROW_SIZE, BOARD_COLUMN_SIZE);
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public void run() {
        consoleOutputHandler.showGameStartComments();
        gameBoard.initializeGame();

        while (true) {
            try {
                consoleOutputHandler.displayBoard(gameBoard);

                if (doesUserWinTheGame()) {
                    consoleOutputHandler.printWinningComment();
                    break;
                }

                if (doesUserLoseTheGame()) {
                    consoleOutputHandler.printGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getActionInputFromUser();

                actOnCell(cellInput, userActionInput);
            } catch (GameException e) {
                consoleOutputHandler.printExceptionMessage(e);
            } catch (Exception e) {
                consoleOutputHandler.printSimpleExceptionMessage("프로그램에 문제가 생겼습니다.");
            }
        }
    }

    private void actOnCell(String cellInput, String userActionInput) {
        int selectedColumnIndex = getSelectedColumnIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (isFlagAction(userActionInput)) {
            gameBoard.flagCell(selectedRowIndex, selectedColumnIndex);
            checkGameIsOver();
            return;
        }
        if (isOpenCellAction(userActionInput)) {
            if (gameBoard.isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
                gameBoard.findCell(selectedRowIndex, selectedColumnIndex).open();
                setGameStatusToLose();
                return;
            }
            gameBoard.openSurroundedCell(selectedRowIndex, selectedColumnIndex);
            checkGameIsOver();
            return;
        }
        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private String getCellInputFromUser() {
        consoleOutputHandler.printInputCoordinateComment();
        return consoleInputHandler.getUserInput();
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkGameIsOver() {
        if (gameBoard.isAllCellChecked()) {
            setGameStatusToWin();
        }
    }

    private void setGameStatusToWin() {
        gameStatus = 1;
    }

    private void setGameStatusToLose() {
        gameStatus = -1;
    }


    private boolean isOpenCellAction(String userActionInput) {
        return userActionInput.equals("1");
    }

    private boolean isFlagAction(String userActionInput) {
        return userActionInput.equals("2");
    }

    private int getSelectedRowIndex(String cellInput) {
        return convertRowFrom(cellInput.charAt(1));
    }

    private int getSelectedColumnIndex(String cellInput) {
        return convertColumnFrom(cellInput.charAt(0));
    }

    private String getActionInputFromUser() {
        consoleOutputHandler.printInputUserActionComment();
        return consoleInputHandler.getUserInput();
    }

    private int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;

        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new GameException("잘못된 좌표(행) 입력입니다. (" + rowIndex + ")");
        }

        return rowIndex;
    }

    private int convertColumnFrom(char cellInputColumn) {
        return switch (cellInputColumn) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            case 'i' -> 8;
            case 'j' -> 9;
            default -> throw new GameException("잘못된 좌표(열) 입력입니다. (" + cellInputColumn + ")");
        };
    }
}
