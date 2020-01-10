package common.command;

import common.Log;
import common.Serializable;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Command {

    public static final int AUTHORIZATION_LEVEL_NORMAL = 0;
    public static final int AUTHORIZATION_LEVEL_ADMIN = 1;

    public abstract void setCommandString(String command);

    public abstract String toCommandString(boolean startWith);

    public abstract Response exec(DatabaseUtility db);

    public abstract int authorizationLevel();

    public static Command fromCommandString(String commandString) {
        Command[] commands = {

                new ListSubjectAssessment(),
                new GetStudentGradeCommand(),
                new ListAssessmentDetailCommand(),
                new SetGradeCommand(),
                new LoginCommand()

        };
        for (Command command : commands) {
            if (commandString.startsWith(command.toCommandString(true))) {
                command.setCommandString(commandString);
                return command;
            }
        }
        Log.i("-----------------------------------------");
        return null;
    }

    public static <T extends Serializable> String serialize(List<T> data) {
        return data.stream().map(Serializable::serialize).collect(Collectors.joining("\n"));
    }

}