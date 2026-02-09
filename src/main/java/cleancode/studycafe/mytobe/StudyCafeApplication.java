package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.io.StudyCafeFileHandler;
import cleancode.studycafe.mytobe.io.StudyCafeIOHandler;

public class StudyCafeApplication {

    public static void main(String[] args) {
        StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
        StudyCafeFileHandler fileHandler = new StudyCafeFileHandler();

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(
                ioHandler,
                fileHandler
        );

        studyCafePassMachine.run();
    }

}
