package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.exception.AppException;
import cleancode.studycafe.mytobe.io.InputHandler;
import cleancode.studycafe.mytobe.io.OutputHandler;
import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
import cleancode.studycafe.mytobe.model.StudyCafeOrder;
import cleancode.studycafe.mytobe.model.StudyCafePass;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private final StudyCafePassSelector studyCafePassSelector = new StudyCafePassSelector();

    public StudyCafePassMachine(InputHandler inputHandler, OutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();

            outputHandler.askPassTypeSelection();
            StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

            List<StudyCafePass> studyCafePasses = studyCafePassSelector.getPassesByType(studyCafePassType);
            outputHandler.showPassListForSelection(studyCafePasses);
            StudyCafePass studyCafePass = inputHandler.getSelectPass(studyCafePasses);
            StudyCafeLockerPass studyCafeLockerPass = selectLockerPass(studyCafePass);

            StudyCafeOrder studyCafeOrder = StudyCafeOrder.of(studyCafePass, studyCafeLockerPass);

            outputHandler.showPassOrderSummary(studyCafeOrder);
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafeLockerPass selectLockerPass(StudyCafePass selectedStudyCafePass) {
        if (selectedStudyCafePass.getPassType() != StudyCafePassType.FIXED) {
            return null;
        }

        Optional<StudyCafeLockerPass> lockerPassCandidate = studyCafePassSelector.getLockerPassByStudyCafePass(selectedStudyCafePass);

        if (lockerPassCandidate.isPresent()) {
            StudyCafeLockerPass lockerPass = lockerPassCandidate.get();
            outputHandler.askLockerPass(lockerPass);
            boolean isLockerSelected = inputHandler.getLockerSelection();

            if (isLockerSelected) {
                return lockerPass;
            }
        }

        return null;
    }
}
