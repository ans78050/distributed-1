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

public class Users implements Serializable, Tablable {

    public final static String TABLE_NAME = "users";

    private int id;
    private String type;
    private String username;
    private String password;

    public Users() {

    }

    public Users(int id, String type, String username, String password) {
        this.id = id;
        this.type = type;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public boolean isAdmin() {
        return getType().equals("admin");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static Users getByIdAndPassword(DatabaseUtility db, int id, String password) {
        try {
            String query;
            PreparedStatement statement = db.getConnection().prepareStatement(query =             //////////////TABLE_NAME = subjects
                    "SELECT * " +                 //////////////TABLE_NAME2 = assessments
                            "FROM " + TABLE_NAME + " " +
                            "WHERE userId=" + id + " " +
                            "AND password='" + password + "' "
            );
            Log.i("prepareStatement: " + statement);
            Log.i("QUERY: " + query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id2 = resultSet.getInt("userId");
                String type = resultSet.getString("type");
                String u = resultSet.getString("username");
                String p = resultSet.getString("password");
                return new Users(id2, type, u, p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Users getByUsernameAndPassword(DatabaseUtility db, String username, String password) {
        try {
            String query;
            PreparedStatement statement = db.getConnection().prepareStatement(query =             //////////////TABLE_NAME = subjects
                    "SELECT * " +                 //////////////TABLE_NAME2 = assessments
                            "FROM " + TABLE_NAME + " " +
                            "WHERE username='" + username + "' " +
                            "AND password='" + password + "' "
            );
            Log.i("prepareStatement: " + statement);
            Log.i("QUERY: " + query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("userId");
                String type = resultSet.getString("type");
                String u = resultSet.getString("username");
                String p = resultSet.getString("password");
                return new Users(id, type, u, p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Users getByUsername(DatabaseUtility db, String username) {
        try {
            String query;
            PreparedStatement statement = db.getConnection().prepareStatement(query =             //////////////TABLE_NAME = subjects
                    "SELECT * " +                 //////////////TABLE_NAME2 = assessments
                            "FROM " + TABLE_NAME + " " +
                            "WHERE username='" + username + "' "
            );
            Log.i("prepareStatement: " + statement);
            Log.i("QUERY: " + query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("userId");
                String type = resultSet.getString("type");
                String u = resultSet.getString("username");
                String p = resultSet.getString("password");
                return new Users(id, type, u, p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean save(DatabaseUtility db) throws SQLException {

        if (id > 0) {
            PreparedStatement insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (id, type, username, password) " +
                            "VALUE (?,?,?,?) "
            );
            insertStatement.setInt(1, id);
            insertStatement.setString(2, type);
            insertStatement.setString(3, username);
            insertStatement.setString(4, password);
            insertStatement.execute();
        } else {
            PreparedStatement insertStatement = db.getConnection().prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (type, username, password) " +
                            "VALUE (?,?,?) "
            );
            insertStatement.setString(1, type);
            insertStatement.setString(2, username);
            insertStatement.setString(3, password);
            insertStatement.execute();
        }
        return true;
    }


    @Override
    public String serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("type", type);
        data.put("username", username);
        data.put("password", password);
        return JsonHelper.toJson(data);
    }

    public static String[] getHeaders() {
        return new String[]{"Id", "Type", "Username"};
    }

    @Override
    public void deserialize(String serializer) {
        Map<String, Object> data = JsonHelper.flatten(serializer);
        id = (int) data.get("id");
        type = (String) data.get("type");
        username = (String) data.get("username");
        password = (String) data.get("password");
    }

    @Override
    public String[] toTable() {
        return new String[]{
                getId() + "",
                getType(),
                getUsername(),
                getPassword()
        };

    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
