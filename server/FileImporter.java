package server;

import common.Log;
import common.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileImporter {
    ////////////////////////////////////////////////////////////////////////
    //Read data from file -> Assessment.csv
    ////////////////////////////////////////////////////////////////////////
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
    ////////////////////////////////////////////////////////////////////////
    //Read data from file -> Grade.csv
    ////////////////////////////////////////////////////////////////////////
    public List<Grade> importGrade(String filename) throws FileNotFoundException {
        List<Grade> grades = new ArrayList<>();
        FileReader fileReader = new FileReader(filename);
        Scanner scanner = new Scanner(fileReader);
        //skip first line
        String firstLine = scanner.nextLine();
        Log.i("first line: " + firstLine);

        while (scanner.hasNextLine()) {
            String[] s = scanner.nextLine().split(",");
            String degree = s[0];
            String knowledge = s[1];
            String skill = s[2];
            Grade grade = new Grade(degree, knowledge, skill);
            grades.add(grade);
        }

        scanner.close();
        return grades;
    }
    ////////////////////////////////////////////////////////////////////////
    //Read data from file -> Student.csv
    ////////////////////////////////////////////////////////////////////////
    public List<Student> importStudent(String filename) throws FileNotFoundException {
        List<Student> students = new ArrayList<>();
        FileReader fileReader = new FileReader(filename);
        Scanner scanner = new Scanner(fileReader);
        //skip first line
        String firstLine = scanner.nextLine();
        Log.i("first line: " + firstLine);

        while (scanner.hasNextLine()) {
            String[] s = scanner.nextLine().split(",");
            String fullName = s[0];
            int yearLevel = Integer.parseInt(s[1]);
            Student student = new Student(fullName, yearLevel);
            students.add(student);
        }

        scanner.close();
        return students;
    }
    ////////////////////////////////////////////////////////////////////////
    //Read data from file -> Subject.csv
    ////////////////////////////////////////////////////////////////////////
    public List<Subject> importSubject(String filename) throws FileNotFoundException {
        List<Subject> subjects = new ArrayList<>();
        FileReader fileReader = new FileReader(filename);
        Scanner scanner = new Scanner(fileReader);
        //skip first line
        String firstLine = scanner.nextLine();
        Log.i("first line: " + firstLine);

        while (scanner.hasNextLine()) {
            String[] s = scanner.nextLine().split(",");
            String subjectName = s[0];
            Subject subject = new Subject(subjectName);
            subjects.add(subject);
        }

        scanner.close();
        return subjects;
    }
    ////////////////////////////////////////////////////////////////////////
    //Read data from file -> Admin.csv
    ////////////////////////////////////////////////////////////////////////
    public List<Admin> importAdmin(String filename) throws FileNotFoundException {
        List<Admin> admins = new ArrayList<>();
        FileReader fileReader = new FileReader(filename);
        Scanner scanner = new Scanner(fileReader);
        //skip first line
        String firstLine = scanner.nextLine();
        Log.i("first line: " + firstLine);

        while (scanner.hasNextLine()) {
            String[] s = scanner.nextLine().split(",");
            String adminName = s[0];
            Admin admin = new Admin(adminName);
            admins.add(admin);
        }

        scanner.close();
        return admins;
    }

}
