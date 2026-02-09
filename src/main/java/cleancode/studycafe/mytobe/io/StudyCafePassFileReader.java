package cleancode.studycafe.mytobe.io;

import cleancode.studycafe.mytobe.model.StudyCafePass;
import cleancode.studycafe.mytobe.model.StudyCafePassType;
import cleancode.studycafe.mytobe.model.StudyCafePasses;
import cleancode.studycafe.mytobe.provider.StudyCafePassProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudyCafePassFileReader implements StudyCafePassProvider {

    private static final String PASS_CSV_PATH = "src/main/resources/cleancode/studycafe/pass-list.csv";

    @Override
    public StudyCafePasses getPasses() {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get(PASS_CSV_PATH));
            List<StudyCafePass> studyCafePasses = new ArrayList<>();
            for (String line : lines) {
                String[] values = line.split(",");
                StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
                int duration = Integer.parseInt(values[1]);
                int price = Integer.parseInt(values[2]);
                double discountRate = Double.parseDouble(values[3]);

                StudyCafePass studyCafePass = StudyCafePass.of(studyCafePassType, duration, price,
                        discountRate);
                studyCafePasses.add(studyCafePass);
            }

            return StudyCafePasses.of(studyCafePasses);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }
}
