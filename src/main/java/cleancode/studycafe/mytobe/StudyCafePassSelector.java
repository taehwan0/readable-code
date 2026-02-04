package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.io.StudyCafeFileHandler;
import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
import cleancode.studycafe.mytobe.model.StudyCafePass;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import java.util.List;
import java.util.Optional;

public class StudyCafePassSelector {

    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
    private final List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePasses();

    /**
     * StudyCafePassType에 해당하는 StudyCafePass를 반환한다.
     *
     * @param studyCafePassType StudyCafePassType
     * @return StudyCafePassType에 해당하는 StudyCafePass
     */
    public List<StudyCafePass> getPassesByType(StudyCafePassType studyCafePassType) {
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
    public Optional<StudyCafeLockerPass> getLockerPassByStudyCafePass(StudyCafePass studyCafePass) {
        StudyCafeLockerPass studyCafeLockerPass = studyCafeFileHandler.readLockerPasses().stream()
                .filter(option ->
                        option.getPassType() == studyCafePass.getPassType()
                                && option.getDuration() == studyCafePass.getDuration()
                )
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(studyCafeLockerPass);
    }
}
