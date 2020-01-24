package common.model;

import common.JsonHelper;
import common.Log;
import common.Serializable;
import common.Tablable;
import common.display.Table;
import server.DatabaseUtility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grade implements Serializable, Tablable {

    public final static String TABLE_NAME = "grades";


    private String degree, knowledge, skill;

    public Grade(String degree, String knowledge, String skill) {
        this.degree = degree;
        this.knowledge = knowledge;
        this.skill = skill;
    }



    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public static List<Grade> getAll(DatabaseUtility db) {
        List<Grade> grades = new ArrayList<>();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String degree = resultSet.getString("degree");
                String knowledge = resultSet.getString("knowledge");
                String skill = resultSet.getString("skill");
                Grade grade = new Grade( degree, knowledge, skill);
                grades.add(grade);
            }
        } catch (SQLException e) {
            return null;
        }
        return grades;
    }

    public boolean saveGrade(DatabaseUtility db) throws SQLException {

                PreparedStatement insertStatement = db.getConnection().prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (degree, knowledge, skill) " +
                                "VALUE (?, ?, ?) "
                );
                insertStatement.setString(1, degree);
                insertStatement.setString(2, knowledge);
                insertStatement.setString(3, skill);
                insertStatement.execute();
                return true;
            }


    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("degree", degree);
        data.put("knowledge", knowledge);
        data.put("skill", skill);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        degree = (String) data.get("degree");
        knowledge = (String) data.get("knowledge");
        skill = (String) data.get("skill");
    }

    @Override
    public String toString() {
        return "Student{" +
                "degree='" + degree + '\'' +
                ", knowledge=" + knowledge + '\'' +
                ", skill=" + skill + '\'' +
                '}';
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getDegree(),
                getKnowledge(),
                getSkill()
        };

    }
}
