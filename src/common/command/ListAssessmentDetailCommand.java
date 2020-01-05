package common.command;

import common.model.Assessment;
import common.model.Student;
import protocol.Response;
import server.DatabaseUtility;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class ListAssessmentDetailCommand extends Command {
    @Override
    public void setCommandString(String command) {

    }

    @Override
    public String toCommandString(boolean startWith) {
        return "ListAssessmentDetail:";
    }

    @Override
    public Response exec(DatabaseUtility db) {
        List<Assessment> assessment = Assessment.getAll(db);
        if (assessment == null) {
            return new Response(Response.STATUS_SERVER_ERROR, "Cannot connect to Database!");
        }
        String res = Command.serialize(assessment);
        return new Response(Response.STATUS_OK, res);
    }
}
