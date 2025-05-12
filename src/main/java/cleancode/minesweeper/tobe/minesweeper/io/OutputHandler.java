package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.exception.GameException;

public interface OutputHandler {

    void showGameStartComments();

    void showBoard(GameBoard gameBoard);

    void showGameLosingComment();

    void showWinningComment();

    void showInputCoordinateComment();

    void showInputUserActionComment();

    void showExceptionMessage(GameException e);

    void showSimpleExceptionMessage(String message);
}
