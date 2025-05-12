package cleancode.minesweeper.tobe.minesweeper.board.position;

import java.util.Objects;

public class CellPosition {

    private final int rowIndex;
    private final int columnIndex;

    private CellPosition(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || columnIndex < 0) {
            throw new IllegalArgumentException("올바르지 않은 좌표 입력입니다.");
        }

        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public static CellPosition of(int rowIndex, int columnIndex) {
        return new CellPosition(rowIndex, columnIndex);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CellPosition that = (CellPosition) o;
        return rowIndex == that.rowIndex && columnIndex == that.columnIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, columnIndex);
    }

    public boolean isRowIndexMoreThanOrEqual(int rowIndex) {
        return this.rowIndex >= rowIndex;
    }

    public boolean isColumnIndexMoreThanOrEqual(int columnIndex) {
        return this.columnIndex >= columnIndex;
    }

    public CellPosition calculatePositionBy(RelativePosition surroundRelativePosition) {
        if (canCalculatePositionBy(surroundRelativePosition)) {
            return CellPosition.of(rowIndex + surroundRelativePosition.getDeltaRow(),
                    columnIndex + surroundRelativePosition.getDeltaColumn());
        }
        throw new IllegalArgumentException("움직일 수 없는 좌표 입력");
    }

    public boolean canCalculatePositionBy(RelativePosition relativePosition) {
        return this.rowIndex + relativePosition.getDeltaRow() >= 0 && this.columnIndex + relativePosition.getDeltaColumn() >= 0;
    }

    public boolean isRowIndexLessThan(int rowIndex) {
        return this.rowIndex < rowIndex;
    }

    public boolean isColumnIndexLessThan(int columnIndex) {
        return this.columnIndex < columnIndex;
    }
}
