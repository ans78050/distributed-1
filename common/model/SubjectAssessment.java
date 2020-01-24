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

public class SubjectAssessment implements Serializable, Tablable {

    private final static String TABLE_SUBJECT = "subjects";
    private final static String TABLE_ASSESMENT = "assessments";

    private int subjectId;
    private String subjectName;
    private String assessmentId;
    private String type;
    private String topic;
    private String format;
    private String dueDate;

    public SubjectAssessment(int subjectId, String subjectName, String assessmentId, String type, String topic, String format, String dueDate) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.assessmentId = assessmentId;
        this.type = type;
        this.topic = topic;
        this.format = format;
        this.dueDate = dueDate;
    }

    private int getSubjectId() {
        return subjectId;
    }


    public String getSubjectName() {
        return subjectName;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public String getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public String getFormat() {
        return format;
    }

    public String getDueDate() {
        return dueDate;
    }

    public static List<SubjectAssessment> getById(DatabaseUtility db, int id) {
        try {
            String query;
            PreparedStatement statement = db.getConnection().prepareStatement(query =

                    "SELECT * " +
                            "FROM " + TABLE_SUBJECT + "," + TABLE_ASSESMENT + " " +
                            "WHERE " + TABLE_SUBJECT + ".subjectName = " + TABLE_ASSESMENT + ".subject " +
                            "AND " + TABLE_SUBJECT + ".subject_id = " + id
            );
            Log.i("prepareStatement: " + statement);
            Log.i("QUERY: " + query);
            ResultSet resultSet = statement.executeQuery();
            List<SubjectAssessment> subjectAssessments = new ArrayList<>();
            while (resultSet.next()) {
                int subjectId = resultSet.getInt("subject_id");
                String subjectName = resultSet.getString("subjectName");
                String assessmentId = resultSet.getString("assessment_id");
                String type = resultSet.getString("type");
                String topic = resultSet.getString("topic");
                String format = resultSet.getString("format");
                String dueDate = resultSet.getString("due_date");
                subjectAssessments.add(new SubjectAssessment(subjectId, subjectName, assessmentId, type, topic, format, dueDate));
            }
            return subjectAssessments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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

    @Override
        public void deserialize (String serializer){
            Map<String, Object> data = JsonHelper.flatten(serializer);
            subjectId = (int) data.get("subject_id");
            subjectName = (String) data.get("subject_name");
            assessmentId = (String) data.get("assessment_id");
            type = (String) data.get("type");
            topic = (String) data.get("topic");
            format = (String) data.get("format");
            dueDate = (String) data.get("dueDate");
        }

    public static String[] getHeaders() {
        return new String[]{
                "Subject Id",
                "Subject Name",
                "Assessment Id",
                "Type",
                "Topic",
                "Format",
                "Due Date"
        };
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getSubjectId() + "",
                getSubjectName(),
                getAssessmentId(),
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

