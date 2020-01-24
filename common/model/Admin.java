package common.model;

import common.JsonHelper;
import common.Serializable;
import common.Tablable;
import server.DatabaseUtility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Admin implements Serializable, Tablable {

    public final static String TABLE_NAME = "admins";


    private String adminName;

    public Admin(String adminName) {
        this.adminName = adminName;
    }


    public String getAdminName() {
        return adminName;
    }

    public boolean saveAdmin(DatabaseUtility db) throws SQLException {

        PreparedStatement insertStatement = db.getConnection().prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (adminName) " +
                        "VALUE (?) "
        );
        insertStatement.setString(1, adminName);
        insertStatement.execute();
        return true;
    }

    //////////////////////////////////////////////
    //Add data in table -> I have change to read from file
    /////////////////////////////////////////////
//    public boolean saveAdmin(DatabaseUtility db) throws SQLException {
//
//                PreparedStatement insertStatement = db.getConnection().prepareStatement(
//                        "INSERT INTO " + TABLE_NAME + " (adminName)" +
//                                "VALUES (?)");
//                insertStatement.setString(1, "Rahul Dileep");
//                insertStatement.execute();
//                insertStatement.setString(1, "Wei Li");
//                insertStatement.execute();
//                insertStatement.setString(1, "Steve Smith");
//                insertStatement.execute();
//
//                return true;
//            }

    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("adminName", adminName);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        adminName = (String) data.get("adminName");
    }

    @Override
    public String toString() {
        return "Admin{" +
                ", adminName='" + adminName + '\'' +
                '}';
    }


    public static String[] getHeaders() {
        return new String[]{"Admin Name"};
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getAdminName(),
        };
    }
}
