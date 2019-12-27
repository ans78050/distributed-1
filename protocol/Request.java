package protocol;

public class Request {

    private String host;
    private int port;
    private String message;

    public Request(String host, int port, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getMessage() {
        return message;
    }
}
