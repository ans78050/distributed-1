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

public class Assessment implements Serializable, Tablable {

    public final static String TABLE_NAME = "assessments";

    public  String assessmentId;
    public  String subject;
    public  String type;
    public  String topic;
    public  String format;
    public  String dueDate;

    public Assessment(String assessmentId, String subject, String type, String topic, String format, String dueDate) {
        this.assessmentId = assessmentId;
        this.subject = subject;
        this.type = type;
        this.topic = topic;
        this.format = format;
        this.dueDate = dueDate;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    public static List<Assessment> getAll(DatabaseUtility db) {
        List<Assessment> assessments = new ArrayList<>();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String assessmentId = resultSet.getString("assessment_id");
                String subject = resultSet.getString("subject");
                String type = resultSet.getString("type");
                String topic = resultSet.getString("topic");
                String format = resultSet.getString("format");
                String dueDate = resultSet.getString("due_date");
                Assessment assessment = new Assessment(assessmentId, subject, type, topic, format, dueDate);
                assessments.add(assessment);
            }
        } catch (SQLException e) {
            return null;
        }
        return assessments;
    }
    ////////////////////////////////////////////////
    //method use to save data from file to database
    ////////////////////////////////////////////////
    public boolean save(DatabaseUtility db) {
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "SELECT * FROM " + TABLE_NAME + " " +
                            "WHERE assessment_id = ? " +
                            "AND subject = ? ");
            statement.setString(1, assessmentId);
            statement.setString(2, subject);
            ResultSet resultSet = statement.executeQuery();
            Log.i(assessmentId + "," + subject + ", getFetchSize: " + resultSet.getFetchSize());

            if (resultSet.getFetchSize() > 0) {
                PreparedStatement updateStatement = db.getConnection().prepareStatement(
                        "UPDATE " + TABLE_NAME + " " +
                                "SET " +
                                "type = ?," +
                                "topic = ?," +
                                "format = ?," +
                                "due_date = ? " +
                                "WHERE assessment_id = ? " +
                                "AND subject = ? "
                );
                updateStatement.setString(1, type);
                updateStatement.setString(2, topic);
                updateStatement.setString(3, format);
                updateStatement.setString(4, dueDate);
                updateStatement.setString(5, assessmentId);
                updateStatement.setString(6, subject);
                updateStatement.executeUpdate();
                return true;
            } else {
                PreparedStatement insertStatement = db.getConnection().prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " " +
                                "SET " +
                                "type = ?," +
                                "topic = ?," +
                                "format = ?," +
                                "due_date = ?," +
                                "assessment_id = ?," +
                                "subject = ?"
                );
                insertStatement.setString(1, type);
                insertStatement.setString(2, topic);
                insertStatement.setString(3, format);
                insertStatement.setString(4, dueDate);
                insertStatement.setString(5, assessmentId);
                insertStatement.setString(6, subject);
                insertStatement.execute();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("assessment_id", assessmentId);
        data.put("subject", subject);
        data.put("type", type);
        data.put("topic", topic);
        data.put("format", format);
        data.put("dueDate", dueDate);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        assessmentId = (String) data.get("assessment_id");
        subject = (String) data.get("subject");
        type = (String) data.get("type");
        topic = (String) data.get("topic");
        format = (String) data.get("format");
        dueDate = (String) data.get("dueDate");
    }

    public static String[] getHeaders() {
        return new String[]{
                "Id",
                "Subject",
                "Type",
                "Topic",
                "Format",
                "Due Date"
        };
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getAssessmentId() + "",
                getSubject(),
                getType(),
                getTopic(),
                getFormat(),
                getDueDate()
        };
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentId=" + assessmentId +
                ", subject='" + subject + '\'' +
                ", type='" + type + '\'' +
                ", topic='" + topic + '\'' +
                ", format='" + format + '\'' +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
