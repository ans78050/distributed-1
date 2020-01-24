package server;

import common.Log;
import common.model.*;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;

public class DatabaseUtility {

    private static final String DB_NAME = "student_grade";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_URL = MYSQL_URL + DB_NAME;

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    private DatabaseUtility(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseUtility connect() throws ClassNotFoundException {

        //check mysql driver exist.
        Class.forName("com.mysql.jdbc.Driver");

        try {
            Connection connection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
            DatabaseUtility db = new DatabaseUtility(connection);

            boolean isDatabaseExist = db.isDatabaseExist();

            if (!isDatabaseExist) {
                db.createDatabase();
            }

            db.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);


            db.createTables();


            return db;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //=== create database ==============================================================

    private void createDatabase() throws SQLException {
        String createDatabaseSQL = String.format("CREATE DATABASE %s", DB_NAME);
        Statement statement = connection.createStatement();
        statement.execute(createDatabaseSQL);
    }

    private boolean isDatabaseExist() throws SQLException {
        ResultSet resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {
            final int META_DB_NAME_INDEX = 1;
            String databaseName = resultSet.getString(META_DB_NAME_INDEX);
            if (databaseName.equalsIgnoreCase(DB_NAME)) {
                return true;
            }
        }
        return false;
    }

    //=== create table =================================================================

    private String createStudentsTableSQL() {
        return "CREATE TABLE students(" +
                "student_id integer PRIMARY KEY not null AUTO_INCREMENT," +
                "full_name varchar(32)," +
                "year_level integer" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    private String createAssessmentTableSQL() {
        return "CREATE TABLE assessments(" +
                "assessment_id varchar(64)," +
                "subject varchar(64)," +
                "type varchar(64)," +
                "topic varchar(64)," +
                "format varchar(64)," +
                "due_date varchar(64)," +
                "PRIMARY KEY( `assessment_id`, `subject`)" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    private String createSubjectTableSQL() {
        return "CREATE TABLE subjects(" +
                "subject_id integer PRIMARY KEY not null AUTO_INCREMENT," +
                "subjectName varchar(64)" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    private String createGradeTableSQL() {
        return "CREATE TABLE grades(" +
                "grade_id integer PRIMARY KEY not null AUTO_INCREMENT," +
                "degree varchar(64)," +
                "knowledge varchar(64)," +
                "skill varchar(256)" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    private String createGradeAssessmentTableSQL() {
        return "CREATE TABLE grade_assessment(" +
                "student_id integer," +
                "subject_id integer," +
                "assessment_id varchar(64)," +
                "grade_id integer," +
                "PRIMARY KEY( `assessment_id`, `subject_id`, `grade_id`, `student_id`)" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    private String createUsersTableSQL() {
        return "CREATE TABLE users(" +
                "userId integer AUTO_INCREMENT," +
                "type varchar(8)," +
                "username varchar(32)," +
                "password varchar(32)," +
                "PRIMARY KEY( `userId`, `type`)" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    private String createAdminsTableSQL() {
        return "CREATE TABLE admins(" +
                "admin_id integer PRIMARY KEY not null AUTO_INCREMENT," +
                "adminName varchar(32)" +
                ")";
    }

    /////////////////////////////////////////////////////////////
    //command to create table in database
    private void createTables() throws SQLException {
        String[] createTableStatements = {
                createStudentsTableSQL(),
                createAssessmentTableSQL(),
                createGradeTableSQL(),
                createSubjectTableSQL(),
                createGradeAssessmentTableSQL(),
                createUsersTableSQL(),
                createAdminsTableSQL()
        };
        /////////////////////////////////////////////////////////////
        //check Table exist or not if not yet create execute above command
        for (String createTableStatement : createTableStatements) {
            Statement statement = connection.createStatement();
            try {
                statement.execute(createTableStatement);
            } catch (Exception ignored) {
                Log.i("Table already exist - " + createTableStatement);
            }
        }
    }
    /////////////////////////////////////////////////////////////
    //import data Section -> I have change from SQL coding to fill data in table to read from file by scanner method
    /////////////////////////////////////////////////////////////
//    public void importDataStudent() {
//        Student student = new Student();
//        student.saveStudent(this);
//    }

//    public void importDataGrade() throws SQLException {
//        Grade grade = new Grade();
//        grade.saveGrade(this);
//    }

//    public void importDataAdmin() throws SQLException {
//        Admin admin = new Admin();
//        admin.saveAdmin(this);
//    }

    /////////////////////////////////////////////////////////////
    //import data From FileImporter Class
    /////////////////////////////////////////////////////////////
    public void importDataFromFile() {
        FileImporter importer = new FileImporter();
        try {
            /////////////////////////////////////////////////////////////
            //import Assessment Class -> filename = COIT20257Ass2Data.csv
            /////////////////////////////////////////////////////////////
            List<Assessment> assessments = importer.importAssessment("COIT20257Ass2Data.csv");
            for (Assessment assessment : assessments) {
                Log.i("saving assessment: " + assessment);
                assessment.save(this);
            }

            /////////////////////////////////////////////////////////////
            //import Grade Class -> filename = Grade.csv
            /////////////////////////////////////////////////////////////
            List<Grade> grades = importer.importGrade("Grade.csv");
            for (Grade grade : grades) {
                Log.i("saving grade: " + grade);
                grade.saveGrade(this);
            }
            /////////////////////////////////////////////////////////////
            //import Student Class -> filename = Student.csv
            /////////////////////////////////////////////////////////////
            List<Student> students = importer.importStudent("Student.csv");
            for (Student student : students) {
                Log.i("saving student: " + student);
                student.saveStudent(this);
            }
            /////////////////////////////////////////////////////////////
            //import Subject Class -> filename = Subject.csv
            /////////////////////////////////////////////////////////////
            List<Subject> subjects = importer.importSubject("Subject.csv");
            for (Subject subject : subjects) {
                Log.i("saving subject: " + subject);
                subject.saveSubject(this);
            }
            /////////////////////////////////////////////////////////////
            //import Subject Class -> filename = Subject.csv
            /////////////////////////////////////////////////////////////
            List<Admin> admins = importer.importAdmin("Admin.csv");
            for (Admin admin : admins) {
                Log.i("saving subject: " + admin);
                admin.saveAdmin(this);
            }


        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
