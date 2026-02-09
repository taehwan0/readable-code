package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.io.StudyCafeIOHandler;
import cleancode.studycafe.mytobe.io.StudyCafeLockerPassFileReader;
import cleancode.studycafe.mytobe.io.StudyCafePassFileReader;
import cleancode.studycafe.mytobe.provider.StudyCafeLockerPassProvider;
import cleancode.studycafe.mytobe.provider.StudyCafePassProvider;

public class StudyCafeApplication {

    public static void main(String[] args) {
        StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
        StudyCafePassProvider passProvider = new StudyCafePassFileReader();
        StudyCafeLockerPassProvider lockerPassProvider = new StudyCafeLockerPassFileReader();

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(
                ioHandler,
                passProvider,
                lockerPassProvider
        );

        studyCafePassMachine.run();
    }

}
