package cleancode.studycafe.mytobe.io;

import cleancode.studycafe.mytobe.model.StudyCafeLockerPass;
import cleancode.studycafe.mytobe.model.StudyCafeLockerPasses;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import cleancode.studycafe.mytobe.provider.StudyCafeLockerPassProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudyCafeLockerPassFileReader implements StudyCafeLockerPassProvider {

    @Override
    public StudyCafeLockerPasses getLockerPasses() {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get("src/main/resources/cleancode/studycafe/locker.csv"));
            List<StudyCafeLockerPass> lockerPasses = new ArrayList<>();
            for (String line : lines) {
                String[] values = line.split(",");
                StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
                int duration = Integer.parseInt(values[1]);
                int price = Integer.parseInt(values[2]);

                StudyCafeLockerPass lockerPass = StudyCafeLockerPass.of(studyCafePassType, duration,
                        price);
                lockerPasses.add(lockerPass);
            }

            return StudyCafeLockerPasses.of(lockerPasses);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }
}
