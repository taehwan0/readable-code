package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.exception.GameException;
import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class MineSweeper implements GameInitializable, GameRunnable {

    private final GameBoard gameBoard;
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public MineSweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        this.gameBoard = new GameBoard(gameLevel);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments();

        while (true) {
            try {
                outputHandler.showBoard(gameBoard);

                if (doesUserWinTheGame()) {
                    outputHandler.showWinningComment();
                    break;
                }

                if (doesUserLoseTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getActionInputFromUser();

                actOnCell(cellInput, userActionInput);
            } catch (GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleExceptionMessage("프로그램에 문제가 생겼습니다.");
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
        outputHandler.showInputCoordinateComment();
        return inputHandler.getUserInput();
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
        outputHandler.showInputUserActionComment();
        return inputHandler.getUserInput();
    }
}
