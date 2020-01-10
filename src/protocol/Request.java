package protocol;

public class Request {

    private String host;
    private int port;
    private String message;

    private int userId;
    private String userType; // student, admin

    public Request(String host, int port, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
    }


    public void setUser(int userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isAdmin() {
        return userType.equals("admin");
    }

    public boolean isStudent() {
        return userType.equals("student");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getMessage() {
        return message + "/" + userId + "," + userType;
    }
}
