package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COLUMN_SIZE = 10;
    public static final int LAND_MINE_COUNT = 10;

    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MIND_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";

    public static final Scanner SCANNER = new Scanner(System.in);

    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];
    private static final Integer[][] LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
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

            String cellInput = getCellInputFromUser();
            String userActionInput = getActionInputFromUser();

            actOnCell(cellInput, userActionInput);
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColumnIndex = getSelectedColumnIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (isFlagAction(userActionInput)) {
            BOARD[selectedRowIndex][selectedColumnIndex] = FLAG_SIGN;
            checkGameIsOver();
            return;
        }
        if (isOpenCellAction(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
                BOARD[selectedRowIndex][selectedColumnIndex] = LAND_MIND_SIGN;
                setGameStatusToLose();
                return;
            }
            open(selectedRowIndex, selectedColumnIndex);
            checkGameIsOver();
            return;
        }
        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private static void setGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColumnIndex) {
        return LAND_MINES[selectedRowIndex][selectedColumnIndex];
    }

    private static boolean isOpenCellAction(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean isFlagAction(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        return convertRowFrom(cellInput.charAt(1));
    }

    private static int getSelectedColumnIndex(String cellInput) {
        return convertColumnFrom(cellInput.charAt(0));
    }

    private static String getActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
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
        return Arrays.stream(BOARD)
                       .flatMap(Arrays::stream)
                       .noneMatch(cell -> cell.equals(CLOSED_CELL_SIGN));
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
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int column = 0; column < BOARD_COLUMN_SIZE; column++) {
                System.out.print(BOARD[row][column] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        // board에 UI를 입력하는 과정
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int column = 0; column < BOARD_COLUMN_SIZE; column++) {
                BOARD[row][column] = CLOSED_CELL_SIGN;
            }
        }

        // board에 10개의 mine을 심는 과정, 중복이 될 수 있을 것으로 보임
        // 총 10개 이하의 mine이 생성될 것으로 유추됨
        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COLUMN_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }

        // 모든 칸을 돌면서
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int column = 0; column < BOARD_COLUMN_SIZE; column++) {
                int count = 0;
                // 근처에 mine이 있는지 확인하여 count를 늘려주는 과정
                if (!isLandMineCell(row, column)) { // mine이 아닌 칸에서만 실행한다.
                    if (row - 1 >= 0 && column - 1 >= 0 && isLandMineCell(row - 1, column - 1)) {
                        count++;
                    }
                    if (row - 1 >= 0 && isLandMineCell(row - 1, column)) {
                        count++;
                    }
                    if (row - 1 >= 0 && column + 1 < BOARD_COLUMN_SIZE && isLandMineCell(row - 1, column + 1)) {
                        count++;
                    }
                    if (column - 1 >= 0 && isLandMineCell(row, column - 1)) {
                        count++;
                    }
                    if (column + 1 < BOARD_COLUMN_SIZE && isLandMineCell(row, column + 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && column - 1 >= 0 && isLandMineCell(row + 1, column - 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, column)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && column + 1 < BOARD_COLUMN_SIZE && isLandMineCell(row + 1, column + 1)) {
                        count++;
                    }
                    LAND_MINE_COUNTS[row][column] = count;
                    continue;
                }
                LAND_MINE_COUNTS[row][column] = 0;
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COLUMN_SIZE) {
            return;
        }
        if (!BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        if (LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = OPENED_CELL_SIGN;
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
