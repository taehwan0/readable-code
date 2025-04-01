package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;
import java.util.List;

public class CellSignFinder {

    private final List<CellSignProvidable> CELL_SIGN_PROVIDERS = List.of(
            new EmptyCellSignProvider(),
            new FlagCellSignProvider(),
            new UncheckedCellSignProvider(),
            new NumberCellSignProvider(),
            new LandMineCellSignProvider()
    );

    public String findCellSignFrom(CellSnapshot cellSnapshot) {
        CellSnapshotStatus status = cellSnapshot.getStatus();

        return this.CELL_SIGN_PROVIDERS.stream()
                .filter(provider -> provider.supports(cellSnapshot))
                .findFirst()
                .map(provider -> provider.provide(cellSnapshot))
                .orElseThrow(() -> new IllegalStateException("Unexpected cell snapshot status: " + status));
    }
}
