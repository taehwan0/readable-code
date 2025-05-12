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

        while (gameBoard.isInProgress()) {
            try {
                outputHandler.showBoard(gameBoard);

                CellPosition cellPosition = getCellInputFromUser();
                UserAction userAction = getActionInputFromUser();

                actOnCell(cellPosition, userAction);
            } catch (GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleExceptionMessage("프로그램에 문제가 생겼습니다.");
            }
        }

        outputHandler.showBoard(gameBoard);

        if (gameBoard.isWinStatus()) {
            outputHandler.showWinningComment();
        }

        if (gameBoard.isLoseStatus()) {
            outputHandler.showGameLosingComment();
        }
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showInputCoordinateComment();
        CellPosition cellPosition = inputHandler.getCellPositionFromUserInput();

        if (gameBoard.isInvalidCellPosition(cellPosition)) {
            throw new IllegalArgumentException("잘못된 좌표 입력");
        }

        return cellPosition;
    }

    private UserAction getActionInputFromUser() {
        outputHandler.showInputUserActionComment();
        return inputHandler.getUserActionFromUser();
    }

    private void actOnCell(CellPosition cellPosition, UserAction userAction) {

        if (isFlagAction(userAction)) {
            gameBoard.flagAt(cellPosition);
            return;
        }
        if (isOpenCellAction(userAction)) {
            gameBoard.openAt(cellPosition);
        }
        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private boolean isFlagAction(UserAction userAction) {
        return UserAction.FLAG == userAction;
    }

    private boolean isOpenCellAction(UserAction userAction) {
        return UserAction.OPEN == userAction;
    }
}
