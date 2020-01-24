package common.command;

import common.model.Admin;
import common.model.Student;
import common.model.Users;
import protocol.Response;
import server.DatabaseUtility;

import java.sql.SQLException;
import java.util.List;

public class RegisterCommand extends Command {

    private String username;
    private String type;
    private String password;

    public RegisterCommand() {

    }

    public RegisterCommand(String username, String type, String password) {
        this.username = username;
        this.type = type;
        this.password = password;
    }

    @Override
    public void setCommandString(String command) {
        String[] s = command.split(":");
        username = s[1];
        type = s[2];
        password = s[3];
    }

    @Override
    public String toCommandString(boolean startWith) {
        if (startWith)
            return "Register:";
        else
            return "Register:" + username + ":" + type + ":" + password;
    }

    @Override
    public Response exec(DatabaseUtility db) {

        Users user = Users.getByUsername(db, username);
        if (user != null) {
            return new Response(Response.STATUS_SERVER_ERROR, "This user already exist in db");
        }

        boolean isExist = false;
        if (type.equals("student")) {
            List<Student> students = Student.getAll(db);
            for (Student s : students) {
                if (s.getFullName().equals(username)) {
                    isExist = true;
                    break;
                }
            }
        } else if (type.equals("admin")) {
            List<Admin> admins = Admin.getAll(db);
            for (Admin s : admins) {
                if (s.getAdminName().equals(username)) {
                    isExist = true;
                    break;
                }
            }
        } else {
            return new Response(Response.STATUS_SERVER_ERROR, "type not exist");
        }

        user = new Users(0, type, username, password);
        try {
            user.save(db);
        } catch (SQLException e) {
            return new Response(Response.STATUS_SERVER_ERROR, "cannot save data to db");
        }

        user = Users.getByUsernameAndPassword(db, username, password);
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
