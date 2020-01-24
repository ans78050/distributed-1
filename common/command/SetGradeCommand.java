package common.command;

import common.model.Grade;
import common.model.GradeAssessment;
import common.model.Student;
import common.model.Subject;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;
import java.util.Scanner;

public class SetGradeCommand extends Command {

    @Override
    public int authorizationLevel() {
        return Command.AUTHORIZATION_LEVEL_ADMIN;
    }

    private int studentId, subjectId, gradeId;
    private String assessmentId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    @Override
    public void setCommandString(String command) {
        String[] s = command.split(":");
        studentId = Integer.parseInt(s[1]);      ///1st input
        subjectId = Integer.parseInt(s[2]);     ///2nd input
        assessmentId = s[3];                    ///3rd input
        gradeId = Integer.parseInt(s[4]);       ///4th input
    }

    @Override
    public String toCommandString(boolean startWith) {
        if (startWith)
            return "SetGrade:";
        else
            return "SetGrade:" + studentId + ":" + subjectId + ":" + assessmentId + ":" + gradeId;
    }

    @Override
    public Response exec(DatabaseUtility db) {
        GradeAssessment g = new GradeAssessment(studentId, subjectId, assessmentId, gradeId);
        g.save(db);
        return new Response(Response.STATUS_OK, "ok");
    }


    @Override
    public String getCommandStringUi() {
        return "Set Grade For A Chosen Student And Subject";
    }

    @Override
    public void doInputUi(DatabaseUtility db) {
        Scanner scanner = new Scanner(System.in);
        //////////show List of Student from database
        System.out.println("=======================================");
        List<Student> students = Student.getAll(db);
        int i = 1;
        for (Student student : students) {
            System.out.println(i + ". " + student.getFullName());
            i++;
        }
        System.out.println("=======================================");
        System.out.print("Enter Student Id: ");
        setStudentId(scanner.nextInt());
        scanner.nextLine();
        //////////show List of Subject from database
        System.out.println("=======================================");
        List<Subject> subjects = Subject.getAll(db);
        int I = 1;
        for (Subject subject : subjects){
            System.out.println(I + ". " + subject.getSubjectName());
            I++;
        }
        System.out.println("=======================================");
        System.out.print("Enter Subject Id: ");
        setSubjectId(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Assessment Id(eg 11.1): ");
        setAssessmentId(scanner.next());
        scanner.nextLine();
        //////////show List of Grade from database
        System.out.println("=======================================");
        List<Grade> grades = Grade.getAll(db);
        int ii = 1;
        for (Grade grade : grades) {
            System.out.println(ii + ". " + grade.getDegree());
            ii++;
        }
        System.out.println("=======================================");
        System.out.print("Enter Grade Id: ");
        setGradeId(scanner.nextInt());
    }
}


///////////////////////////////////////////////////////
