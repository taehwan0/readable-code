package cleancode.studycafe.mytobe;

import cleancode.studycafe.mytobe.io.InputHandler;
import cleancode.studycafe.mytobe.io.OutputHandler;

public class StudyCafeApplication {

	public static void main(String[] args) {
		InputHandler inputHandler = new InputHandler();
		OutputHandler outputHandler = new OutputHandler();
		StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(
				inputHandler,
				outputHandler
		);

		studyCafePassMachine.run();
	}

}
