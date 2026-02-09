package cleancode.studycafe.mytobe.model;

public class StudyCafeLockerPass implements Pass {

    private final StudyCafePassType passType;
    private final int duration;
    private final int price;

    private StudyCafeLockerPass(StudyCafePassType passType, int duration, int price) {
        this.passType = passType;
        this.duration = duration;
        this.price = price;
    }

    public static StudyCafeLockerPass of(StudyCafePassType passType, int duration, int price) {
        return new StudyCafeLockerPass(passType, duration, price);
    }

    @Override
    public StudyCafePassType getPassType() {
        return passType;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public boolean isSameTypeAndDuration(StudyCafePass studyCafePass) {
        return this.passType == studyCafePass.getPassType() && this.duration == studyCafePass.getDuration();
    }
}
