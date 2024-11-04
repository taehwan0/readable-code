package cleancode.minesweeper.tobe.position;

import java.util.List;
import java.util.Objects;

public class RelativePosition {

    public static final List<RelativePosition> SURROUNDED_POSITIONS = List.of(
            RelativePosition.of(-1, -1),
            RelativePosition.of(-1, 0),
            RelativePosition.of(-1, 1),
            RelativePosition.of(0, -1),
            RelativePosition.of(0, 1),
            RelativePosition.of(1, -1),
            RelativePosition.of(1, 0),
            RelativePosition.of(1, 1)
    );

    private final int deltaRow;
    private final int deltaColumn;

    private RelativePosition(int deltaRow, int deltaColumn) {
        this.deltaRow = deltaRow;
        this.deltaColumn = deltaColumn;
    }

    public static RelativePosition of(int deltaRow, int deltaColumn) {
        return new RelativePosition(deltaRow, deltaColumn);
    }

    public int getDeltaRow() {
        return deltaRow;
    }

    public int getDeltaColumn() {
        return deltaColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelativePosition that = (RelativePosition) o;
        return deltaRow == that.deltaRow && deltaColumn == that.deltaColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deltaRow, deltaColumn);
    }
}
