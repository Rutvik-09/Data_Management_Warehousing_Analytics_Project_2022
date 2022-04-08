package entity;

public class DBResponse {
    private boolean status;
    private String response;
    private Table dataOfTable;

    public DBResponse(){}

    public DBResponse(boolean status, String response) {
        this.status = status;
        this.response = response;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean success) {
        this.status = success;
    }

    public String getMessage() {
        return response;
    }

    public void setMessage(String msg) {
        this.response = msg;
    }

    public Table getDataOfTable() {
        if(dataOfTable == null)
            dataOfTable = new Table();
        return dataOfTable;
    }

    public void setDataOfTable(Table dataOfTable) {
        this.dataOfTable = dataOfTable;
    }

    @Override
    public String toString() {
        return "DBResponse{" +
                "status=" + status +
                ", response='" + response + '\'' +
                ", dataOfTable=" + dataOfTable +
                '}';
    }
}
