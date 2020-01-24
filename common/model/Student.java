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


    private String fullName;
    private int yearLevel;


    public Student(String fullName, int yearLevel) {
        this.fullName = fullName;
        this.yearLevel = yearLevel;
    }

    public String getFullName() {
        return fullName;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    ///////////////////////////////////////////////////////////////////
    //I have chang from add data by SQL coding to read from file
    ///////////////////////////////////////////////////////////////////
    public boolean saveStudent(DatabaseUtility db) throws SQLException {

        PreparedStatement insertStatement = db.getConnection().prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (full_name, year_level) " +
                        "VALUE (?, ?) "
        );
        insertStatement.setString(1, fullName);
        insertStatement.setInt(2, yearLevel);
        insertStatement.execute();
        return true;
    }
    ////////////////////////////////////////////////////////////
    //Add student
    ///////////////////////////////////////////////////////////
    public boolean addStudent(DatabaseUtility db) {
        try {
            PreparedStatement insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " " +
                            "SET " +
                            "full_name = ?," +
                            "year_level = ?,"

            );
            insertStatement.setString(1, fullName);
            insertStatement.setInt(2, yearLevel);
            insertStatement.execute();
            return true;
        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////
    //I have chang from add data by SQL coding to read from file
    ///////////////////////////////////////////////////////////////////
//    public boolean saveStudent(DatabaseUtility db) {
//        try {
//            PreparedStatement statement = db.getConnection().prepareStatement(
//                    "SELECT * FROM " + TABLE_NAME + " " +
//                            "WHERE full_name = ? ");
//
//            statement.setString(1, fullName);
//            PreparedStatement insertStatement = db.getConnection().prepareStatement(
//                    "INSERT INTO " + TABLE_NAME + " (full_name, year_level)" +
//                            "VALUES (?,?)");
//            insertStatement.setString(1, "John Clarke");
//            insertStatement.setInt(2, 11);
//            insertStatement.execute();
//            insertStatement.setString(1, "Peter White");
//            insertStatement.setInt(2, 11);
//            insertStatement.execute();
//            insertStatement.setString(1, "Lily Li");
//            insertStatement.setInt(2, 11);
//            insertStatement.execute();
//            insertStatement.setString(1, "Lisa Soon");
//            insertStatement.setInt(2, 11);
//            insertStatement.execute();
//            insertStatement.setString(1, "Tom Dixon");
//            insertStatement.setInt(2, 11);
//            insertStatement.execute();
//
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }

    //////////////////////////////////////////////////////////////
    //getAll use to list Student from Student table
    /////////////////////////////////////////////////////////////
    public static List<Student> getAll(DatabaseUtility db) {
        List<Student> students = new ArrayList<>();
        try {
            PreparedStatement statement = db.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                int yearLevel = resultSet.getInt("year_level");
                Student student = new Student(fullName, yearLevel);
                students.add(student);
            }
        } catch (SQLException e) {
            return null;
        }
        return students;
    }

    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("full_name", fullName);
        data.put("year_level", yearLevel);
        return JsonHelper.toJson(data);
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        fullName = (String) data.get("full_name");
        yearLevel = (int) data.get("year_level");
    }


//    public static Student getById(DatabaseUtility db, int id) {
//        try {
//            PreparedStatement statement = db.getConnection().prepareStatement(
//                    "SELECT " + TABLE_NAME + ".*," + TABLE_NAME2 + ".* " +
//                            "FROM " + TABLE_NAME + "," + TABLE_NAME2 +
//                            " WHERE " + TABLE_NAME+".subjectName = " + TABLE_NAME2 + ".subject "+
//                            "AND subject_id = " +id+
//                            "ORDER BY subjectName ASC");
//            ResultSet resultSet = statement.executeQuery();
//            resultSet.next();
//            int studentId = resultSet.getInt("student_id");
//            String fullName = resultSet.getString("full_name");
//            int yearLevel = resultSet.getInt("year_level");
//            return new Student();
//        } catch (SQLException e) {
//            return null;
//        }
//    }

    @Override
    public String toString() {
        return "Student{" +
                "fullName='" + fullName + '\'' +
                ", yearLevel=" + yearLevel +
                '}';
    }


    @Override
    public String[] toTable() {
        return new String[]{
                getFullName(),
                getYearLevel() + ""
        };
    }
}
