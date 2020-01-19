package common.command;

import common.model.GradeAssessment;
import common.model.Student;
import common.model.Subject;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class GetStudentGradeCommand extends Command {

    @Override
    public int authorizationLevel() {
        return Command.AUTHORIZATION_LEVEL_NORMAL;
    }

    private int studentId;
    private int subjectId;
    private String assessmentId;
    private int gradeId;
/////////////////////////////////////Getter Setter///////////////////////////////////////

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void setCommandString(String command) {
        String[] s = command.split(":");
        studentId = Integer.parseInt(s[1]);
        subjectId = Integer.parseInt(s[2]);
        assessmentId = (s[3]);
        gradeId = Integer.parseInt(s[4]);
    }

    @Override
    public String toCommandString(boolean startWith) {
        if (startWith)
            return "GetStudentGrade:";
        else
            return "GetStudentGrade:" + studentId +
                    ":" + subjectId +
                    ":" + assessmentId +
                    ":" + gradeId;
    }

    //    @Override
//    public Response exec(DatabaseUtility db) {
//        List<GradeAssessment> gradeAssessments = GradeAssessment.getByIdGrade(db, studentId, subjectId);
//        if (gradeAssessments == null){
//    }
//        String res = Command.serialize(gradeAssessments);
//        return new Response(Response.STATUS_OK, res);
//    }
    @Override
    public Response exec(DatabaseUtility db) {
        GradeAssessment gradeAssessment = GradeAssessment.getByIdGrade(db, studentId, subjectId);
        String res = gradeAssessment == null ? "" : gradeAssessment.serialize();
        return new Response(Response.STATUS_OK, res);
    }


    @Override
    public String getCommandStringUi() {
        return "Grade Of Assessment For Student";
    }

    @Override
    public void doInputUi(DatabaseUtility db) {
        Scanner scanner = new Scanner(System.in);
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
        System.out.println("=======================================");
        System.out.println("1. English");
        System.out.println("2. Mathematics B");
        System.out.println("3. Biology");
        System.out.println("4. Business and Communication Technologies");
        System.out.println("5. Religion and Ethics");
        System.out.println("=======================================");
        System.out.print("Enter Subject Id: ");
        setSubjectId(Integer.parseInt(scanner.nextLine()));
    }


}
