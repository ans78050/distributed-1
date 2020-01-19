package common.command;

import common.Log;
import common.model.Subject;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;
import java.util.Scanner;

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


    @Override
    public String getCommandStringUi() {
        return "List Of Assessment For Chosen Subject";
    }

    @Override
    public void doInputUi(DatabaseUtility db) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======================================");
        System.out.println("1. English");
        System.out.println("2. Mathematics B");
        System.out.println("3. Biology");
        System.out.println("4. Business and Communication Technologies");
        System.out.println("5. Religion and Ethics");
        System.out.println("=======================================");
        System.out.print("Enter Subject Id: ");
        setSubjectId(scanner.nextInt());
    }


}
