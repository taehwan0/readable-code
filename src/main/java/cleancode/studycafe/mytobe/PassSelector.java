package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
import cleancode.studycafe.mytobe.model.StudyCafePass;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import java.util.List;
import java.util.Optional;

public class PassSelector {

    private final List<StudyCafePass> studyCafePasses;
    private final List<StudyCafeLockerPass> studyCafeLockerPasses;

    public PassSelector(List<StudyCafePass> studyCafePasses, List<StudyCafeLockerPass> studyCafeLockerPasses) {
        this.studyCafePasses = studyCafePasses;
        this.studyCafeLockerPasses = studyCafeLockerPasses;
    }

    /**
     * StudyCafePassType에 해당하는 StudyCafePass를 반환한다.
     *
     * @param studyCafePassType StudyCafePassType
     * @return StudyCafePassType에 해당하는 StudyCafePass
     */
    public List<StudyCafePass> selectStudyPassesByType(StudyCafePassType studyCafePassType) {
        return studyCafePasses.stream()
                .filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
                .toList();
    }

    /**
     * 고른 StudyCafePass에 해당하는 LockerPass를 반환한다.
     *
     * @param studyCafePass 선택한 StudyCafePass
     * @return 선택한 StudyCafePass에 해당하는 LockerPass
     */
    public Optional<StudyCafeLockerPass> selectMatchingLockerPass(StudyCafePass studyCafePass) {
        StudyCafeLockerPass studyCafeLockerPass = studyCafeLockerPasses.stream()
                .filter(option -> option.isSameTypeAndDuration(studyCafePass))
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(studyCafeLockerPass);
    }
}
