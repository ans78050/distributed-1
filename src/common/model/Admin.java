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


    private int studentId;
    private String name;

    public Admin(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }


    public static Admin getById(DatabaseUtility db, int id) {
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "SELECT * " +
                            "FROM" + TABLE_NAME + " " +
                            "WHERE id = " + id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int adminId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            return new Admin(adminId, name);
        } catch (SQLException e) {
            return null;
        }
    }


    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("student_id", studentId);
        data.put("name", name);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        studentId = (int) data.get("student_id");
        name = (String) data.get("name");
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", fullName='" + name + '\'' +
                '}';
    }


    public static String[] getHeaders() {
        return new String[]{"Id", "Student Name", "Year Level"};
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getStudentId() + "",
                getName(),
        };
    }
}
