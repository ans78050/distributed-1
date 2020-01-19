package common.command;

import common.model.GradeAssessment;
import protocol.Response;
import server.DatabaseUtility;

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
        System.out.println("=======================================");
        System.out.println("1. John Clarke");
        System.out.println("2. Peter White");
        System.out.println("3. Lily Li");
        System.out.println("4. Lisa Soon");
        System.out.println("5. Tom Dixon");
        System.out.println("=======================================");
        System.out.print("Enter Student Id: ");
        setStudentId(scanner.nextInt());
        scanner.nextLine();
        System.out.println("=======================================");
        System.out.println("1. English");
        System.out.println("2. Mathematics B");
        System.out.println("3. Biology");
        System.out.println("4. Business and Communication Technologies");
        System.out.println("5. Religion and Ethics");
        System.out.println("=======================================");
        System.out.print("Enter Subject Id: ");
        setSubjectId(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Assessment Id(eg 11.1): ");
        setAssessmentId(scanner.next());
        scanner.nextLine();
        System.out.println("=======================================");
        System.out.println("1. Very high");
        System.out.println("2. High");
        System.out.println("3. Sound");
        System.out.println("4. Developing");
        System.out.println("5. Basic understanding");
        System.out.println("=======================================");
        System.out.print("Enter Grade Id: ");
        setGradeId(scanner.nextInt());
    }
}


///////////////////////////////////////////////////////
