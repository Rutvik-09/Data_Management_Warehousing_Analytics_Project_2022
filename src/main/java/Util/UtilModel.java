package Util;
public class UtilModel {
    String currentUserName;
    String currentDatabase;
    String databaseState;

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getDatabaseName() {
        return currentDatabase;
    }

    public void setDatabaseName(String databaseName) {
        this.currentDatabase = databaseName;
    }

}
