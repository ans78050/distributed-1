package protocol;

public class Response {

    public final static int STATUS_OK = 200;
    public final static int STATUS_PAGE_NOT_FOUND = 404;
    public final static int STATUS_AUTHENTICATION_FAIL = 401;
    public final static int STATUS_SERVER_ERROR = 500;

    private int status;
    private String body;

    public Response(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
