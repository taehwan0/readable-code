package cleancode.minesweeper.tobe.cell;

import java.util.Objects;

public class CellSnapshot {

    private final CellSnapshotStatus status;
    private final int nearByLandmineCount;

    private CellSnapshot(CellSnapshotStatus status, int nearByLandmineCount) {
        this.status = status;
        this.nearByLandmineCount = nearByLandmineCount;
    }

    public static CellSnapshot of(CellSnapshotStatus status, int nearByLandmineCount) {
        return new CellSnapshot(status, nearByLandmineCount);
    }

    public static CellSnapshot ofEmpty() {
        return new CellSnapshot(CellSnapshotStatus.EMPTY, 0);
    }

    public static CellSnapshot ofFlag() {
        return new CellSnapshot(CellSnapshotStatus.FLAG, 0);
    }

    public static CellSnapshot ofLandMine() {
        return new CellSnapshot(CellSnapshotStatus.LAND_MINE, 0);
    }

    public static CellSnapshot ofNumber(int nearByLandmineCount) {
        return new CellSnapshot(CellSnapshotStatus.NUMBER, nearByLandmineCount);
    }

    public static CellSnapshot ofUnchecked() {
        return new CellSnapshot(CellSnapshotStatus.UNCHECKED, 0);
    }

    public CellSnapshotStatus getStatus() {
        return status;
    }

    public int getNearByLandmineCount() {
        return nearByLandmineCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CellSnapshot that)) {
            return false;
        }

        return nearByLandmineCount == that.nearByLandmineCount && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, nearByLandmineCount);
    }

    public boolean isSameStatus(CellSnapshotStatus cellSnapshotStatus) {
        return this.status == cellSnapshotStatus;
    }
}
