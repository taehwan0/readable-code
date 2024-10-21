package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.exception.GameException;

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
