package common.command;

import common.Log;
import common.Serializable;
import common.model.Users;
import protocol.Response;
import server.DatabaseUtility;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Command {

    public static Command[] list() {
        return new Command[]{

                new ListSubjectAssessment(),
                new GetStudentGradeCommand(),
                new ListAssessmentDetailCommand(),
                new SetGradeCommand(),
                new LoginCommand(),
                new AddstudentCommand()
        };
    }

    public static int commandInUiCount() {
        int count = 0;
        for (Command c : list()) {
            if (c.showInUi(null)) {
                count++;
            }
        }
        return count;
    }

    public static final int AUTHORIZATION_LEVEL_NORMAL = 0;
    public static final int AUTHORIZATION_LEVEL_ADMIN = 1;

    public abstract void setCommandString(String command);

    public abstract String toCommandString(boolean startWith);

    public abstract Response exec(DatabaseUtility db) throws SQLException;

    public abstract int authorizationLevel();

    public static Command fromCommandString(String commandString) {
        for (Command command : list()) {
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

    public boolean showInUi(Users user) {
        if (authorizationLevel() == AUTHORIZATION_LEVEL_NORMAL) {
            return true;
        }
        if (authorizationLevel() == AUTHORIZATION_LEVEL_ADMIN && user != null && user.isAdmin()) {
            return true;
        }
        return false;
    }

    public abstract String getCommandStringUi();

    public abstract void doInputUi(DatabaseUtility db);

}
