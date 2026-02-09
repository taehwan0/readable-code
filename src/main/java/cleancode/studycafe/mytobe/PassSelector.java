package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
import cleancode.studycafe.mytobe.model.StudyCafeLockerPasses;
import cleancode.studycafe.mytobe.model.StudyCafePass;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import cleancode.studycafe.mytobe.model.StudyCafePasses;
import java.util.List;
import java.util.Optional;

public class PassSelector {

    private final StudyCafePasses studyCafePasses;
    private final StudyCafeLockerPasses studyCafeLockerPasses;

    public PassSelector(StudyCafePasses studyCafePasses, StudyCafeLockerPasses studyCafeLockerPasses) {
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
        return studyCafePasses.findByPass(studyCafePassType);
    }

    /**
     * 고른 StudyCafePass에 해당하는 LockerPass를 반환한다.
     *
     * @param studyCafePass 선택한 StudyCafePass
     * @return 선택한 StudyCafePass에 해당하는 LockerPass
     */
    public Optional<StudyCafeLockerPass> selectMatchingLockerPass(StudyCafePass studyCafePass) {
        return studyCafeLockerPasses.findMatchingLockerPass(studyCafePass);
    }
}
