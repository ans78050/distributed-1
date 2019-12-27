package common.model;

import common.JsonHelper;
import common.Log;
import common.Serializable;
import common.Tablable;
import server.DatabaseUtility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subject implements Serializable, Tablable {

    public final static String TABLE_NAME = "subjects";
    public final static String TABLE_NAME2 = "assessments";

    private int subjectId;
    private String subjectName;
    private String assessmentId;
    private String subject;
    private String type;
    private String topic;
    private String format;
    private String dueDate;

    public Subject(int subjectId,
                   String subjectName, String assessmentId, String subject, String type, String topic, String format, String dueDate
) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.assessmentId = assessmentId;
        this.subject = subject;
        this.type = type;
        this.topic = topic;
        this.format = format;
        this.dueDate = dueDate;
    }





    public Subject(int id, String subjectName, String assessment_id, String type, String topic, String format, String dueDate) {

    }

    public Subject() {

    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
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

    public static Subject getById(DatabaseUtility db, int id){
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(                      //////////////TABLE_NAME = subjects
                        "SELECT " + TABLE_NAME + ".*," + TABLE_NAME2 + ".* " +                 //////////////TABLE_NAME2 = assessments
                            "FROM " + TABLE_NAME + "," + TABLE_NAME2 +
                            " WHERE " + TABLE_NAME+".subjectName = " + TABLE_NAME2 + ".subject "+
                            "AND subjects.subject_id = " + id
                            );
            Log.i("prepareStatement: " + statement);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int subjectId = resultSet.getInt("subject_id");
            String subjectName = resultSet.getString("subject_name");
            String assessmentId = resultSet.getString("assessment_id");
            String subject = resultSet.getString("subject");
            String type = resultSet.getString("type");
            String topic = resultSet.getString("topic");
            String format = resultSet.getString("format");
            String dueDate = resultSet.getString("dueDate");
            return new Subject(subjectId, subjectName, assessmentId, subject, type, topic, format, dueDate)
                    ;
        } catch (SQLException e) {
//            e.printStackTrace()
            return null;
        }

    }

    public boolean saveSubject(DatabaseUtility db) {
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "SELECT * FROM " + TABLE_NAME + " " +
                            "WHERE subjectName = ? ");

            statement.setString(1,  subjectName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.getFetchSize() > 0) {
                PreparedStatement updateStatement = db.getConnection().prepareStatement(
                        "UPDATE " + TABLE_NAME + " " +
                                "SET " +
                                "subjectName = ?"
                );
                updateStatement.setString(1, subjectName);
                updateStatement.executeUpdate();
                return true;
            } else{
                PreparedStatement insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (subjectName)" +
                            " VALUES (?)" );
                insertStatement.setString(1, "English");
                insertStatement.execute();
                insertStatement.setString (1, "Maths B");
                insertStatement.execute();
                insertStatement.setString (1, "Biology");
                insertStatement.execute();
                insertStatement.setString (1, "Business and Communication Technologies");
                insertStatement.execute();
                insertStatement.setString (1, "Religion and Ethics");
                insertStatement.execute();

            return true;}
       } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }


    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("subject_id", subjectId);
        data.put("subject_name", subjectName);
        data.put("assessment_id", assessmentId);
        data.put("type", type);
        data.put("topic", topic);
        data.put("format", format);
        data.put("due_date", dueDate);
        return JsonHelper.toJson(data);
    }
    public static String[] getHeaders() {
        return new String[]{"Subject Id", "Subject Name", "Assessment Id", "Type", "Topic", "Format", "Due Date"};
    }
    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        subjectId = (int) data.get("subject_id");
        subjectName = (String) data.get("subject_name");
        assessmentId = (String) data.get("assessment_id");
        type = (String) data.get("type");
        topic = (String) data.get("topic");
        format = (String) data.get("format");
        dueDate = (String) data.get("dueDate");
    }

    @Override
    public String[] toTable() {
        return new String[]{
                    getSubjectId() + "",
                    getSubjectName(),
                    getAssessmentId() + "",
                    getType(),
                    getTopic(),
                    getFormat(),
                    getDueDate()
        };

    }
    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", subjectName='" + subjectName + '\'' +
                ", assessmentId=" + assessmentId + '\'' +
                ", type=" + type + '\'' +
                ", topic=" + topic + '\'' +
                ", format=" + format + '\'' +
                ", dueDate=" + dueDate + '\'' +
                '}';
    }
}
