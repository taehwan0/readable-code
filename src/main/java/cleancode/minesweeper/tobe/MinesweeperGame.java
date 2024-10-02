package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10];
    private static Integer[][] landMineCounts = new Integer[8][10];
    private static boolean[][] landMines = new boolean[8][10];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Scanner scanner = new Scanner(System.in);

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
                if (!landMines[row][column]) { // mine이 아닌 칸에서만 실행한다.
                    if (row - 1 >= 0 && column - 1 >= 0 && landMines[row - 1][column - 1]) {
                        count++;
                    }
                    if (row - 1 >= 0 && landMines[row - 1][column]) {
                        count++;
                    }
                    if (row - 1 >= 0 && column + 1 < 10 && landMines[row - 1][column + 1]) {
                        count++;
                    }
                    if (column - 1 >= 0 && landMines[row][column - 1]) {
                        count++;
                    }
                    if (column + 1 < 10 && landMines[row][column + 1]) {
                        count++;
                    }
                    if (row + 1 < 8 && column - 1 >= 0 && landMines[row + 1][column - 1]) {
                        count++;
                    }
                    if (row + 1 < 8 && landMines[row + 1][column]) {
                        count++;
                    }
                    if (row + 1 < 8 && column + 1 < 10 && landMines[row + 1][column + 1]) {
                        count++;
                    }
                    landMineCounts[row][column] = count;
                    continue;
                }
                landMineCounts[row][column] = 0;
            }
        }
        while (true) {
            System.out.println("   a b c d e f g h i j");
            for (int row = 0; row < 8; row++) {
                System.out.printf("%d  ", row + 1);
                for (int column = 0; column < 10; column++) {
                    System.out.print(board[row][column] + " ");
                }
                System.out.println();
            }
            if (gameStatus == 1) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (gameStatus == -1) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }
            System.out.println();
            System.out.println("선택할 좌표를 입력하세요. (예: a1)");
            String cellInput = scanner.nextLine();
            System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
            String userActionInput = scanner.nextLine();
            char cellInputColumn = cellInput.charAt(0);
            char cellInputRow = cellInput.charAt(1);
            int selectedColumnIndex;
            switch (cellInputColumn) {
                case 'a':
                    selectedColumnIndex = 0;
                    break;
                case 'b':
                    selectedColumnIndex = 1;
                    break;
                case 'c':
                    selectedColumnIndex = 2;
                    break;
                case 'd':
                    selectedColumnIndex = 3;
                    break;
                case 'e':
                    selectedColumnIndex = 4;
                    break;
                case 'f':
                    selectedColumnIndex = 5;
                    break;
                case 'g':
                    selectedColumnIndex = 6;
                    break;
                case 'h':
                    selectedColumnIndex = 7;
                    break;
                case 'i':
                    selectedColumnIndex = 8;
                    break;
                case 'j':
                    selectedColumnIndex = 9;
                    break;
                default:
                    selectedColumnIndex = -1;
                    break;
            }
            int selectedRowIndex = Character.getNumericValue(cellInputRow) - 1;
            if (userActionInput.equals("2")) {
                board[selectedRowIndex][selectedColumnIndex] = "⚑";
                boolean isAllOpened = true;
                for (int row = 0; row < 8; row++) {
                    for (int column = 0; column < 10; column++) {
                        if (board[row][column].equals("□")) {
                            isAllOpened = false;
                        }
                    }
                }
                if (isAllOpened) {
                    gameStatus = 1;
                }
            } else if (userActionInput.equals("1")) {
                if (landMines[selectedRowIndex][selectedColumnIndex]) {
                    board[selectedRowIndex][selectedColumnIndex] = "☼";
                    gameStatus = -1;
                    continue;
                } else {
                    open(selectedRowIndex, selectedColumnIndex);
                }
                boolean isAllOpened = true;
                for (int row = 0; row < 8; row++) {
                    for (int column = 0; column < 10; column++) {
                        if (board[row][column].equals("□")) {
                            isAllOpened = false;
                        }
                    }
                }
                if (isAllOpened) {
                    gameStatus = 1;
                }
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 10) {
            return;
        }
        if (!board[row][col].equals("□")) {
            return;
        }
        if (landMines[row][col]) {
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
