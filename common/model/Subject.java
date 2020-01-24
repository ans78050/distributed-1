package common.model;

import common.JsonHelper;
import common.Log;
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
public class Subject  implements Serializable, Tablable {

    public final static String TABLE_SUBJECT = "subjects";

    private String subjectName;

    public Subject(String subjectName) {
        this.subjectName = subjectName;

    }

    public Subject() {

    }



    public String getSubjectName() {
        return subjectName;
    }


    public static List<Subject> getAll(DatabaseUtility db) {
        List<Subject> subjects = new ArrayList<>();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_SUBJECT);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String subjectName = resultSet.getString("subjectName");
                Subject subject = new Subject(subjectName);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            return null;
        }
        return subjects;
    }

    public boolean saveSubject(DatabaseUtility db) throws SQLException {


                PreparedStatement insertStatement = db.getConnection().prepareStatement(
                        "INSERT INTO " + TABLE_SUBJECT + " (subjectName) " +
                                "VALUE (?) "
                );
                insertStatement.setString(1, subjectName);
                insertStatement.execute();
                return true;
            }


    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("subject_name", subjectName);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        subjectName = (String) data.get("subject_name");
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getSubjectName(),
        };

    }

    @Override
    public String toString() {
        return "Subject{" +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
