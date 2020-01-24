package common.command;

import common.model.Student;
import protocol.Response;
import server.DatabaseUtility;

import java.sql.SQLException;
import java.util.Scanner;

public class AddstudentCommand extends Command{
    private String fullName;
    private int yearLevel;

    public AddstudentCommand( String fullName, int yearLevel) {
        this.fullName = fullName;
        this.yearLevel = yearLevel;
    }

    public AddstudentCommand() {

    }


    @Override
    public int authorizationLevel() {
        return Command.AUTHORIZATION_LEVEL_ADMIN;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    @Override
    public void setCommandString(String command) {
        String[] s = command.split(":");
        fullName = (s[1]);
        yearLevel = Integer.parseInt(s[2]);

    }

    @Override
    public String toCommandString(boolean startWith) {
        if (startWith)
            return "AddStudent:";
        else
            return "AddStudent:" + fullName + ":" + yearLevel ;    }

    @Override
    public Response exec(DatabaseUtility db) throws SQLException {
        Student g = new Student(fullName, yearLevel);
        g.addStudent(db);
        return new Response(Response.STATUS_OK, "ok");
    }


    @Override
    public String getCommandStringUi() {
        return "Add Student To Database";
    }

    @Override
    public void doInputUi(DatabaseUtility db) {
        Scanner scanner = new Scanner(System.in);
        //////////Add Student

        System.out.println("=======================================");
        System.out.print("Enter Student Name: ");
        setFullName(scanner.nextLine());
        scanner.next();

        System.out.print("Enter Year Level: ");
        setYearLevel(scanner.nextInt());
        scanner.nextLine();
    }
}
