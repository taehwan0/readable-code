package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10];
    private static Integer[][] landMineCounts = new Integer[8][10];
    private static boolean[][] landMines = new boolean[8][10];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        Scanner scanner = new Scanner(System.in);

        initializeGame();

        while (true) {
            displayBoard();
            if (doesUserWinTheGame()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (doesUserLoseTheGame()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellInput = getCellInputFromUser(scanner);
            String userActionInput = getActionInputFromUser(scanner);

            char cellInputColumn = getSelectedColumnIndex(cellInput);
            char cellInputRow = getSelectedRowIndex(cellInput);

            int selectedRowIndex = convertRowFrom(cellInputRow);
            int selectedColumnIndex = convertColumnFrom(cellInputColumn);

            if (isFlagAction(userActionInput)) {
                board[selectedRowIndex][selectedColumnIndex] = "⚑";
                checkGameIsOver();
            } else if (isOpenCellAction(userActionInput)) {
                if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
                    board[selectedRowIndex][selectedColumnIndex] = "☼";
                    setGameStatusToLose();
                    continue;
                } else {
                    open(selectedRowIndex, selectedColumnIndex);
                }
                checkGameIsOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void setGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColumnIndex) {
        return landMines[selectedRowIndex][selectedColumnIndex];
    }

    private static boolean isOpenCellAction(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean isFlagAction(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static char getSelectedRowIndex(String cellInput) {
        return cellInput.charAt(1);
    }

    private static char getSelectedColumnIndex(String cellInput) {
        return cellInput.charAt(0);
    }

    private static String getActionInputFromUser(Scanner scanner) {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return scanner.nextLine();
    }

    private static String getCellInputFromUser(Scanner scanner) {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return scanner.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        checkGameIsOver(isAllOpened);
    }

    private static void checkGameIsOver(boolean isAllOpened) {
        if (isAllOpened) {
            setGameStatusToWin();
        }
    }

    private static void setGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellOpened() {
        boolean isAllOpened = true;
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 10; column++) {
                if (board[row][column].equals("□")) {
                    isAllOpened = false;
                    break;
                }
            }
        }
        return isAllOpened;
    }

    private static int convertRowFrom(char cellInputRow) {
        return Character.getNumericValue(cellInputRow) - 1;
    }

    private static int convertColumnFrom(char cellInputColumn) {
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
            default -> -1;
        };
    }

    private static void displayBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < 8; row++) {
            System.out.printf("%d  ", row + 1);
            for (int column = 0; column < 10; column++) {
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        // board에 UI를 입력하는 과정
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 10; column++) {
                board[row][column] = "□";
            }
        }

        // board에 10개의 mine을 심는 과정, 중복이 될 수 있을 것으로 보임
        // 총 10개 이하의 mine이 생성될 것으로 유추됨
        for (int i = 0; i < 10; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true;
        }

        // 모든 칸을 돌면서
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 10; column++) {
                int count = 0;
                // 근처에 mine이 있는지 확인하여 count를 늘려주는 과정
                if (!isLandMineCell(row, column)) { // mine이 아닌 칸에서만 실행한다.
                    if (row - 1 >= 0 && column - 1 >= 0 && isLandMineCell(row - 1, column - 1)) {
                        count++;
                    }
                    if (row - 1 >= 0 && isLandMineCell(row - 1, column)) {
                        count++;
                    }
                    if (row - 1 >= 0 && column + 1 < 10 && isLandMineCell(row - 1, column + 1)) {
                        count++;
                    }
                    if (column - 1 >= 0 && isLandMineCell(row, column - 1)) {
                        count++;
                    }
                    if (column + 1 < 10 && isLandMineCell(row, column + 1)) {
                        count++;
                    }
                    if (row + 1 < 8 && column - 1 >= 0 && isLandMineCell(row + 1, column - 1)) {
                        count++;
                    }
                    if (row + 1 < 8 && isLandMineCell(row + 1, column)) {
                        count++;
                    }
                    if (row + 1 < 8 && column + 1 < 10 && isLandMineCell(row + 1, column + 1)) {
                        count++;
                    }
                    landMineCounts[row][column] = count;
                    continue;
                }
                landMineCounts[row][column] = 0;
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 10) {
            return;
        }
        if (!board[row][col].equals("□")) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
            return;
        } else {
            board[row][col] = "■";
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
