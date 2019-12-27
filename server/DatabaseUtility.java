package server;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import common.Log;
import common.model.Assessment;
import common.model.Grade;
import common.model.Student;
import common.model.Subject;

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

    static DatabaseUtility connect() throws ClassNotFoundException {

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

    private String createSubjectTableSQL() {
        return "CREATE TABLE subjects(" +
                "subject_id integer PRIMARY KEY not null AUTO_INCREMENT," +
                "subjectName varchar(64)" +
                ")";
    }

    private String createGradeTableSQL() {
        return "CREATE TABLE grades(" +
                "grade_id integer PRIMARY KEY not null AUTO_INCREMENT," +
                "degree varchar(64)," +
                "knowledge varchar(64)," +
                "skill varchar(256)" +
                ")";
    }

    private String createGradeAssessmentTableSQL() {
        return "CREATE TABLE grade_assessment(" +
                "student_id integer," +
                "subject_id integer," +
                "assessment_id varchar(64),"+
                "grade_id integer," +
                "PRIMARY KEY( `assessment_id`, `subject_id`, `grade_id`, `student_id`)" +
                ")";
    }

    private void createTables() throws SQLException {
        String[] createTableStatements = {
                createStudentsTableSQL(),
                createAssessmentTableSQL(),
                createGradeTableSQL(),
                createSubjectTableSQL(),
                createGradeAssessmentTableSQL()
        };

        for (String createTableStatement : createTableStatements) {
            Statement statement = connection.createStatement();
            try {
                statement.execute(createTableStatement);
            } catch (MySQLSyntaxErrorException ignored) {
                Log.i("Table already exist - " + createTableStatement);
            }
        }
    }

    public void importDataStudent() {
        Student student = new Student();
        student.saveStudent(this);
    }

    public void importDataGrade() throws SQLException {
        Grade grade  = new Grade();
        grade.saveGrade(this);
    }

    public void importDataSubject() {
        Subject subject  = new Subject();
        subject.saveSubject(this);
    }




    public void importDataFromFile() {                //import data from file
        FileImporter importer = new FileImporter();
        try {

            List<Assessment> assessments = importer.importAssessment("COIT20257Ass2Data.csv");
            for (Assessment assessment : assessments) {
                Log.i("saving assessment: " + assessment);
                assessment.save(this);
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
