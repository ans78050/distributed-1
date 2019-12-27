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

public class GradeAssessment implements Serializable, Tablable {

    public final static String TABLE_NAME = "grade_assessment";

    private int studentId, subjectId, gradeId;
    private String assessmentId;

    public GradeAssessment(int studentId, int subjectId, String assessmentId, int gradeId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.gradeId = gradeId;
        this.assessmentId = assessmentId;
    }
//    public static GradeAssessment getByIdGrade(DatabaseUtility db, int id, int id2) {
//        List<GradeAssessment> gradeAssessment = new ArrayList<>();
//
//
//        try {
//            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE student_id = " + id + "AND subject_id = " + id2);
//            ResultSet resultSet = statement.executeQuery();
//            while(resultSet.next()) {
//                int studentId = resultSet.getInt("student_id");
//                int subjectId = resultSet.getInt("subject_id");
//                String assessmentId = resultSet.getString("assessment_id");
//                int gradeId = resultSet.getInt("grade_id");
//                GradeAssessment grade = new GradeAssessment(studentId, subjectId, assessmentId, gradeId);
//                gradeAssessment.add(grade);
//            }
//        } catch (SQLException e) {
//            return null;
//        }
//        return (GradeAssessment) gradeAssessment;
//    }
    public static GradeAssessment getByIdGrade(DatabaseUtility db, int id, int id2) {
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE student_id = " + id + "AND subject_id = " + id2);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int studentId = resultSet.getInt("student_id");
            int subjectId = resultSet.getInt("subject_id");
            String assessmentId = resultSet.getString("assessment_id");
            int gradeId = resultSet.getInt("grade_id");
            return new GradeAssessment(studentId, subjectId, assessmentId, gradeId);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean save(DatabaseUtility db) {
        try {
            PreparedStatement insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " " +
                            "SET " +
                            "student_id = ?," +
                            "assessment_id = ?," +
                            "subject_id = ?," +
                            "grade_id = ?"

            );
            insertStatement.setInt(1, studentId);
            insertStatement.setString(2, assessmentId);
            insertStatement.setInt(3, subjectId);
            insertStatement.setInt(4, gradeId);

            insertStatement.execute();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }


    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("student_id", studentId);
        data.put("subject_id", subjectId);
        data.put("assessment_id", assessmentId);
        data.put("grade_id", gradeId);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        studentId = (int) data.get("student_id");
        subjectId = (int) data.get("subject_id");
        assessmentId = (String) data.get("assessment_id");
        gradeId = (int) data.get("grade_id");
    }
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", subjectId='" + subjectId + '\'' +
                ", assessmentId=" + assessmentId + '\'' +
                ", gradeId=" + gradeId +
                '}';
    }

    public static String[] getHeaders() {
        return new String[]{
                "Student Id",
                "Subject Id",
                "Assessment Id",
                "Grade Id"
        };
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getStudentId() + "",
                getSubjectId() + "",
                getAssessmentId(),
                getGradeId() + ""
        };
}

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }



    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }}
