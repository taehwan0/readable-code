package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;
import java.util.Arrays;

public enum CellSignProvider implements CellSignProvidable {
    EMPTY(CellSnapshotStatus.EMPTY) {
        @Override
        public String provide(CellSnapshot cellSnapshot) {
            return EMPTY_SIGN;
        }
    },
    FLAG(CellSnapshotStatus.FLAG) {
        @Override
        public String provide(CellSnapshot cellSnapshot) {
            return FLAG_SIGN;
        }
    },
    LAND_MINE(CellSnapshotStatus.LAND_MINE) {
        @Override
        public String provide(CellSnapshot cellSnapshot) {
            return LAND_MIND_SIGN;
        }
    },
    NUMBER(CellSnapshotStatus.NUMBER) {
        @Override
        public String provide(CellSnapshot cellSnapshot) {
            return String.valueOf(cellSnapshot.getNearByLandmineCount());
        }
    },
    UNCHECKED(CellSnapshotStatus.UNCHECKED) {
        @Override
        public String provide(CellSnapshot cellSnapshot) {
            return UNCHECKED_SIGN;
        }
    },
    ;

    private static final String EMPTY_SIGN = "■";
    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MIND_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";

    private final CellSnapshotStatus status;

    CellSignProvider(CellSnapshotStatus status) {
        this.status = status;
    }

    public static String findCellSignFrom(CellSnapshot cellSnapshot) {
        CellSignProvidable cellSignProvidable = findBy(cellSnapshot);
        return cellSignProvidable.provide(cellSnapshot);
    }

    private static CellSignProvidable findBy(CellSnapshot cellSnapshot) {
        return Arrays.stream(values())
                .filter(p -> p.supports(cellSnapshot))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Undefined Cell"));
    }

    @Override
    public boolean supports(CellSnapshot cellSnapshot) {
        return cellSnapshot.isSameStatus(this.status);
    }
}
