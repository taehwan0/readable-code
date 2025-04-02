package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.exception.GameException;
import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;

public class MineSweeper implements GameInitializable, GameRunnable {

    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public MineSweeper(GameConfig gameConfig) {
        this.gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler = gameConfig.getInputHandler();
        this.outputHandler = gameConfig.getOutputHandler();
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

                CellPosition cellPosition = getCellInputFromUser();
                UserAction userAction = getActionInputFromUser();

                actOnCell(cellPosition, userAction);
            } catch (GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleExceptionMessage("프로그램에 문제가 생겼습니다.");
            }
        }
    }

    private void actOnCell(CellPosition cellPosition, UserAction userAction) {

        if (isFlagAction(userAction)) {
            gameBoard.flagAt(cellPosition);
            checkGameIsOver();
            return;
        }
        if (isOpenCellAction(userAction)) {
            if (gameBoard.isLandMineCell(cellPosition)) {
                gameBoard.findCell(cellPosition).open();
                setGameStatusToLose();
                return;
            }
            gameBoard.openSurroundedCell(cellPosition);
            checkGameIsOver();
            return;
        }
        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showInputCoordinateComment();
        CellPosition cellPosition = inputHandler.getCellPositionFromUserInput();

        if (gameBoard.isInvalidCellPosition(cellPosition)) {
            throw new IllegalArgumentException("잘못된 좌표 입력");
        }

        return cellPosition;
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


    private boolean isOpenCellAction(UserAction userAction) {
        return UserAction.OPEN == userAction;
    }

    private boolean isFlagAction(UserAction userAction) {
        return UserAction.FLAG == userAction;
    }

    private UserAction getActionInputFromUser() {
        outputHandler.showInputUserActionComment();
        return inputHandler.getUserActionFromUser();
    }
}
