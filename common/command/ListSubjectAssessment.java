package common.command;

import common.Log;
import common.model.Subject;
import common.model.SubjectAssessment;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;
import java.util.Scanner;

public class ListSubjectAssessment extends Command {

    public ListSubjectAssessment() {

    }

    @Override
    public int authorizationLevel() {
        return Command.AUTHORIZATION_LEVEL_NORMAL;
    }

    private int subjectId;

    public ListSubjectAssessment(int subjectId) {
        this.subjectId = subjectId;

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
        List<SubjectAssessment> subjectAssessments = SubjectAssessment.getById(db, subjectId);
        Log.i("subjects size = " + subjectAssessments.size());
        String res = Command.serialize(subjectAssessments);
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
        List<Subject> subjects = Subject.getAll(db);
        int I = 1;
        for (Subject subject : subjects){
            System.out.println(I + ". " + subject.getSubjectName());
            I++;
        }
        System.out.println("=======================================");
        System.out.print("Enter Subject Id: ");
        setSubjectId(scanner.nextInt());
    }


}
