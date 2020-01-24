package common.model;

import common.JsonHelper;
import common.Serializable;
import common.Tablable;
import server.DatabaseUtility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin implements Serializable, Tablable {

    public final static String TABLE_NAME = "admins";

    private int id;
    private String adminName;

    public Admin(int id, String adminName) {
        this.id = id;
        this.adminName = adminName;
    }


    public String getAdminName() {
        return adminName;
    }

    public boolean saveAdmin(DatabaseUtility db) throws SQLException {
        PreparedStatement insertStatement;
        if (id > 0) {
            insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (id, adminName) " +
                            "VALUE (?, ?) "
            );
            insertStatement.setInt(1, id);
            insertStatement.setString(2, adminName);
        } else {
            insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (adminName) " +
                            "VALUE (?) "
            );
            insertStatement.setString(1, adminName);
        }
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


    /**
     * use to list Admin from Admin table
     *
     * @param db instance of DatabaseUtility
     * @return list of admin
     */
    public static List<Admin> getAll(DatabaseUtility db) {
        List<Admin> admins = new ArrayList<>();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("adminName");
                Admin admin = new Admin(id, name);
                admins.add(admin);
            }
        } catch (SQLException e) {
            return null;
        }
        return admins;
    }

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
