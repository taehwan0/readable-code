package cleancode.studycafe.mytobe.model;

import java.util.List;
import java.util.Optional;

public class StudyCafeLockerPasses {

    private final List<StudyCafeLockerPass> passes;

    private StudyCafeLockerPasses(List<StudyCafeLockerPass> passes) {
        this.passes = passes;
    }

    public static StudyCafeLockerPasses of(List<StudyCafeLockerPass> passes) {
        return new StudyCafeLockerPasses(passes);
    }

    public Optional<StudyCafeLockerPass> findMatchingLockerPass(StudyCafePass studyCafePass) {
        StudyCafeLockerPass studyCafeLockerPass = passes.stream()
                .filter(option -> option.isSameTypeAndDuration(studyCafePass))
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(studyCafeLockerPass);
    }
}
