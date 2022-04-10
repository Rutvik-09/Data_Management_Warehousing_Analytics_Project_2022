package entity;

public class DBResponse {
    private String response;
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public DBResponse(boolean status, String response) {
        this.status = status;
        this.response = response;
    }

    public String getMessage() {
        return response;
    }
}
