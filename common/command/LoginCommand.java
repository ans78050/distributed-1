package common.command;

import common.model.Assessment;
import common.model.Users;
import protocol.Response;
import server.DatabaseUtility;

import java.util.List;

public class LoginCommand extends Command {

    private String username;
    private String password;

    public LoginCommand() {

    }

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void setCommandString(String command) {
        String[] s = command.split(":");
        username = s[1];      ///1st input
        password = s[2];     ///2nd input
    }

    @Override
    public String toCommandString(boolean startWith) {
        if (startWith)
            return "Login:";
        else
            return "Login:" + username + ":" + password;
    }

    @Override
    public Response exec(DatabaseUtility db) {

        Users user = Users.getByUsernameAndPassword(db, username, password);
        if (user == null) {
            return new Response(Response.STATUS_PAGE_NOT_FOUND, "User not exist!");
        }
        String res = user.serialize();
        return new Response(Response.STATUS_OK, res);
    }

    @Override
    public int authorizationLevel() {
        return AUTHORIZATION_LEVEL_NORMAL;
    }

    @Override
    public boolean showInUi(Users users) {
        return false;
    }

    @Override
    public String getCommandStringUi() {
        return null;
    }

    @Override
    public void doInputUi(DatabaseUtility db) {

    }
}
