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


    private int adminId;
    private String adminName;

    public Admin() {
        this.adminId = adminId;
        this.adminName = adminName;
    }


    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getAdminName() {
        return adminName;
    }


    public boolean saveAdmin(DatabaseUtility db) throws SQLException {

                PreparedStatement insertStatement = db.getConnection().prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (adminName)" +
                                "VALUES (?)");
                insertStatement.setString(1, "Rahul Dileep");
                insertStatement.execute();
                insertStatement.setString(1, "Wei Li");
                insertStatement.execute();
                insertStatement.setString(1, "Steve Smith");
                insertStatement.execute();

                return true;
            }

    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("admin_id", adminId);
        data.put("adminName", adminName);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        adminId = (int) data.get("admin_id");
        adminName = (String) data.get("adminName");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                '}';
    }


    public static String[] getHeaders() {
        return new String[]{"adminId", "Admin Name"};
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getAdminId() + "",
                getAdminName(),
        };
    }
}
