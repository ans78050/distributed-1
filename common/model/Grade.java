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

    public Grade(int grade_id, String degree, String knowledge, String skill) {

    }

    public Grade() {

    }

    public String getDegree() {
        return degree;
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
                Grade grade = new Grade(degree, knowledge, skill);
                grades.add(grade);
            }
        } catch (SQLException e) {
            return null;
        }
        return grades;
    }

    public boolean saveGrade(DatabaseUtility db) throws SQLException {
        PreparedStatement insertStatement = db.getConnection().prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (degree, knowledge, skill)" +
                        "VALUES (?,?,?)");
        try {
        insertStatement.setString(1, "Very high");
        insertStatement.setString(2, "thorough understanding");
        insertStatement.setString(3, "uses a high level of skill in both familiar and new situations");
        insertStatement.execute();
        insertStatement.setString(1, "High");
        insertStatement.setString(2, "clear understanding");
        insertStatement.setString(3, "uses a high level of skill in familiar situations, and is beginning to use skills in new situations");
        insertStatement.execute();
        insertStatement.setString(1, "Sound");
        insertStatement.setString(2, "understanding");
        insertStatement.setString(3, "uses skills in situations familiar to them");
        insertStatement.execute();
        insertStatement.setString(1, "Developing");
        insertStatement.setString(2, "understands aspects of");
        insertStatement.setString(3, "uses varying levels of skill in situations familiar to them");
        insertStatement.execute();
        insertStatement.setString(1, "basic understanding");
        insertStatement.setString(2, "thorough understanding");
        insertStatement.setString(3, "beginning to use skills in familiar situations");
        insertStatement.execute();
            return true;
        } catch (SQLException e) {

            return false;
        }
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

    public static String[] getHeaders() {
        return new String[]{
                "Degree of achievement",
                "Knowledge and understanding",
                "Skill and use of skill"
        };
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
