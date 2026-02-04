package cleancode.studycafe.mytobe.model;

import cleancode.studycafe.mytobe.exception.AppException;
import java.util.Optional;

public class StudyCafeOrder {

    private final StudyCafePass studyCafePass;
    private final StudyCafeLockerPass studyCafeLockerPass;

    private StudyCafeOrder(StudyCafePass studyCafePass, StudyCafeLockerPass studyCafeLockerPass) {
        if (studyCafePass == null) {
            throw new AppException("StudyCafeLockerPass cannot be null");
        }

        this.studyCafePass = studyCafePass;
        this.studyCafeLockerPass = studyCafeLockerPass;
    }

    public static StudyCafeOrder of(StudyCafePass studyCafePass, StudyCafeLockerPass studyCafeLockerPass) {
        return new StudyCafeOrder(studyCafePass, studyCafeLockerPass);
    }

    public StudyCafePass getStudyCafePass() {
        return studyCafePass;
    }

    public Optional<StudyCafeLockerPass> getStudyCafeLockerPass() {
        return Optional.ofNullable(studyCafeLockerPass);
    }

    public int getDiscountPrice() {
        return (int) (studyCafePass.getPrice() * studyCafePass.getDiscountRate());
    }

    public int getTotalPrice() {
        return studyCafePass.getPrice()
                - getDiscountPrice()
                + getStudyCafeLockerPass().map(StudyCafeLockerPass::getPrice).orElse(0);
    }
}
