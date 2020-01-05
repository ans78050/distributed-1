package common.command;

import common.model.GradeAssessment;
import protocol.Response;
import server.DatabaseUtility;

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

}


///////////////////////////////////////////////////////
