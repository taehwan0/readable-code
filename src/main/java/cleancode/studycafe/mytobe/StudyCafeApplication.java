package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.io.InputHandler;
import cleancode.studycafe.mytobe.io.OutputHandler;
import cleancode.studycafe.mytobe.io.StudyCafeFileHandler;

public class StudyCafeApplication {

    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        OutputHandler outputHandler = new OutputHandler();
        StudyCafeFileHandler fileHandler = new StudyCafeFileHandler();

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(
                inputHandler,
                outputHandler,
                fileHandler
        );

        studyCafePassMachine.run();
    }

}
