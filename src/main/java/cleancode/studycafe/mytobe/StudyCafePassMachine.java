package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.exception.AppException;
import cleancode.studycafe.mytobe.io.InputHandler;
import cleancode.studycafe.mytobe.io.OutputHandler;
import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
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
            StudyCafePass selectedPass = inputHandler.getSelectPass(studyCafePasses);

            if (studyCafePassType == StudyCafePassType.FIXED) {
                Optional<StudyCafeLockerPass> lockerPass = studyCafePassSelector.getLockerPassByStudyCafePass(selectedPass);

                boolean lockerSelection = false;
                if (lockerPass.isPresent()) {
                    outputHandler.askLockerPass(lockerPass.get());
                    lockerSelection = inputHandler.getLockerSelection();
                }

                if (lockerSelection) {
                    outputHandler.showPassOrderSummary(selectedPass, lockerPass.get());
                } else {
                    outputHandler.showPassOrderSummary(selectedPass, null);
                }

            }
            outputHandler.showPassOrderSummary(selectedPass, null);
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }
}
