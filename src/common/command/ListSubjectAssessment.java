package common.command;

import common.Log;
import common.model.Subject;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;

public class ListSubjectAssessment extends Command {

    @Override
    public int authorizationLevel() {
        return Command.AUTHORIZATION_LEVEL_NORMAL;
    }

    private int subjectId;

    public ListSubjectAssessment(int subjectId) {
        this.subjectId = subjectId;

    }

    public ListSubjectAssessment() {

    }


    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }


    @Override
    public void setCommandString(String command) {
        String[] s = command.split(":");

        subjectId = Integer.parseInt(s[1]);
    }

    @Override
    public String toCommandString(boolean startWith) {
        if (startWith)
            return "GetSubject Assessment:";
        else
            return "GetSubject Assessment:" + subjectId;

    }

    @Override
    public Response exec(DatabaseUtility db) {
        List<Subject> subjects = Subject.getById(db, subjectId);
        Log.i("subjects size = " + subjects.size());
        String res = Command.serialize(subjects);
        return new Response(Response.STATUS_OK, res);
    }


}
