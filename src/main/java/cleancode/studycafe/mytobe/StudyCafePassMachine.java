package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.exception.AppException;
import cleancode.studycafe.mytobe.io.StudyCafeFileHandler;
import cleancode.studycafe.mytobe.io.StudyCafeIOHandler;
import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
import cleancode.studycafe.mytobe.model.StudyCafeOrder;
import cleancode.studycafe.mytobe.model.StudyCafePass;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final StudyCafeIOHandler ioHandler;
    private final PassSelector passSelector;

    public StudyCafePassMachine(StudyCafeIOHandler ioHandler, StudyCafeFileHandler studyCafeFileHandler) {
        this.ioHandler = ioHandler;
        this.passSelector = new PassSelector(studyCafeFileHandler.readStudyCafePasses(), studyCafeFileHandler.readLockerPasses());
    }

    public void run() {
        try {
            ioHandler.showWelcomeMessage();
            ioHandler.showAnnouncement();

            ioHandler.askPassTypeSelection();
            StudyCafePassType studyCafePassType = ioHandler.getPassTypeSelectingUserAction();

            List<StudyCafePass> studyCafePasses = passSelector.selectStudyPassesByType(studyCafePassType);
            StudyCafePass studyCafePass = ioHandler.showPassListForSelection(studyCafePasses);
            StudyCafeLockerPass studyCafeLockerPass = selectLockerPassByStudyPassType(studyCafePass);

            StudyCafeOrder studyCafeOrder = StudyCafeOrder.of(studyCafePass, studyCafeLockerPass);

            ioHandler.showPassOrderSummary(studyCafeOrder);
        } catch (AppException e) {
            ioHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafeLockerPass selectLockerPassByStudyPassType(StudyCafePass selectedStudyCafePass) {
        if (selectedStudyCafePass.cannotUseLockerPass()) {
            return null;
        }

        Optional<StudyCafeLockerPass> lockerPassCandidate = passSelector.selectMatchingLockerPass(selectedStudyCafePass);

        if (lockerPassCandidate.isPresent()) {
            StudyCafeLockerPass lockerPass = lockerPassCandidate.get();
            boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);

            if (isLockerSelected) {
                return lockerPass;
            }
        }

        return null;
    }
}
