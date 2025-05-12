package cleancode.minesweeper.tobe.minesweeper.board.position;

import cleancode.minesweeper.tobe.minesweeper.board.cell.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CellPositions {

    private final List<CellPosition> positions;

    private CellPositions(List<CellPosition> positions) {
        this.positions = positions;
    }

    public static CellPositions of(List<CellPosition> positions) {
        return new CellPositions(positions);
    }

    public static CellPositions from(Cell[][] board) {
        ArrayList<CellPosition> cellPositions = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                CellPosition cellPosition = CellPosition.of(row, column);
                cellPositions.add(cellPosition);
            }
        }

        return CellPositions.of(cellPositions);
    }

    public List<CellPosition> extractRandomPositions(int count) {
        ArrayList<CellPosition> cellPositions = new ArrayList<>(positions);

        Collections.shuffle(cellPositions);

        return cellPositions.subList(0, count);
    }

    public List<CellPosition> subtract(List<CellPosition> positionListToSubtract) {
        ArrayList<CellPosition> cellPositions = new ArrayList<>(positions);
        CellPositions positionsToSubtract = CellPositions.of(positionListToSubtract);

        return cellPositions.stream()
                .filter(positionsToSubtract::doesNotContain)
                .toList();
    }

    public boolean doesNotContain(CellPosition cellPosition) {
        return !positions.contains(cellPosition);
    }

    public List<CellPosition> getPositions() {
        return new ArrayList<>(positions);
    }
}
