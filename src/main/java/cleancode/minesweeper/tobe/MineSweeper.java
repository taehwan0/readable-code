package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.exception.GameException;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class MineSweeper {

    private final GameBoard gameBoard;
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public MineSweeper(GameLevel gameLevel) {
        this.gameBoard = new GameBoard(gameLevel);
    }

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
        String rowInput = cellInput.substring(1);
        return boardIndexConverter.convertRowFrom(rowInput, gameBoard.getRowSize());
    }

    private int getSelectedColumnIndex(String cellInput) {
        return boardIndexConverter.convertColumnFrom(cellInput.charAt(0), gameBoard.getColumnSize());
    }

    private String getActionInputFromUser() {
        consoleOutputHandler.printInputUserActionComment();
        return consoleInputHandler.getUserInput();
    }
}
