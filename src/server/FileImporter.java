package server;

import common.Log;
import common.model.Assessment;
import common.model.Grade;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileImporter {
    public List<Assessment> importAssessment(String filename) throws FileNotFoundException {
        List<Assessment> assessments = new ArrayList<>();
        FileReader fileReader = new FileReader(filename);
        Scanner scanner = new Scanner(fileReader);

        //skip first line
        String firstLine = scanner.nextLine();
        Log.i("first line: " + firstLine);

        while (scanner.hasNextLine()) {
            String[] s = scanner.nextLine().split(",");
            String subject = s[0];
            String assessmentId = s[1];
            String type = s[2];
            String topic = s[3];
            String format = s[4];
            String dueDate = s[5];
            Assessment assessment = new Assessment(assessmentId, subject, type, topic, format, dueDate);
            assessments.add(assessment);
        }
        scanner.close();
        return assessments;
    }


}
