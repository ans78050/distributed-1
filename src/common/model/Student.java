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

public class Student implements Serializable, Tablable {

    public final static String TABLE_NAME = "students";
    public final static String TABLE_NAME2 = "subjects";


    private int studentId;
    private String fullName;
    private int yearLevel;


    public Student() {
        this.studentId = this.studentId;
        this.fullName = this.fullName;
        this.yearLevel = this.yearLevel;
    }

    public Student(int id, int subject_id, String assessment_id, int grade_id) {

    }

    public boolean saveStudent(DatabaseUtility db) {
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "SELECT * FROM " + TABLE_NAME + " " +
                            "WHERE full_name = ? ");

            statement.setString(1, fullName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.getFetchSize() > 0) {
                PreparedStatement updateStatement = db.getConnection().prepareStatement(
                        "UPDATE " + TABLE_NAME + " " +
                                "SET " +
                                "full_name = ?,"
                );

                updateStatement.setString(1, fullName);
                updateStatement.executeUpdate();
                return true;
            } else {

                PreparedStatement insertStatement = db.getConnection().prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (full_name, year_level)" +
                                "VALUES (?,?)");
                insertStatement.setString(1, "John Clarke");
                insertStatement.setInt(2, 11);
                insertStatement.execute();
                insertStatement.setString(1, "Peter White");
                insertStatement.setInt(2, 11);
                insertStatement.execute();
                insertStatement.setString(1, "Lily Li");
                insertStatement.setInt(2, 11);
                insertStatement.execute();
                insertStatement.setString(1, "Lisa Soon");
                insertStatement.setInt(2, 11);
                insertStatement.execute();
                insertStatement.setString(1, "Tom Dixon");
                insertStatement.setInt(2, 11);
                insertStatement.execute();

                return true;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }

    }



    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("student_id", studentId);
        data.put("full_name", fullName);
        data.put("year_level", yearLevel);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        studentId = (int) data.get("student_id");
        fullName = (String) data.get("full_name");
        yearLevel = (int) data.get("year_level");
    }



//    public static List<Student> getAll(DatabaseUtility db) {
//        List<Student> students = new ArrayList<>();
//        try {
//            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                int studentId = resultSet.getInt("student_id");
//                String fullName = resultSet.getString("full_name");
//                int yearLevel = resultSet.getInt("year_level");
//                Student student = new Student(studentId, fullName, yearLevel);
//                students.add(student);
//            }
//        } catch (SQLException e) {
//            return null;
//        }
//        return students;
//    }

    public static Student getById(DatabaseUtility db, int id) {
        try {
            PreparedStatement statement = db.getConnection().prepareStatement(
                    "SELECT " + TABLE_NAME + ".*," + TABLE_NAME2 + ".* " +
                            "FROM " + TABLE_NAME + "," + TABLE_NAME2 +
                            " WHERE " + TABLE_NAME+".subjectName = " + TABLE_NAME2 + ".subject "+
                            "AND subject_id = " +id+
                            "ORDER BY subjectName ASC");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int studentId = resultSet.getInt("student_id");
            String fullName = resultSet.getString("full_name");
            int yearLevel = resultSet.getInt("year_level");
            return new Student();
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", fullName='" + fullName + '\'' +
                ", yearLevel=" + yearLevel +
                '}';
    }


    public static String[] getHeaders() {
        return new String[]{"Id", "Student Name", "Year Level"};
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getStudentId() + "",
                getFullName(),
                getYearLevel() + ""
        };
    }
}
