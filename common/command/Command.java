package common.command;

import common.Log;
import common.Serializable;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Command {

    public abstract void setCommandString(String command);

    public abstract String toCommandString(boolean startWith);

    public abstract Response exec(DatabaseUtility db);

    public static Command fromCommandString(String commandString) {
        Command[] commands = {

                new ListSubjectAssessment(),
                new GetStudentGradeCommand(),
                new ListAssessmentDetailCommand(),
                new SetGradeCommand()

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
