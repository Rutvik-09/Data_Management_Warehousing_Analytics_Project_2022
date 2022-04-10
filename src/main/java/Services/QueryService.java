package Services;

import CustomExceptions.ExceptionDB;

import java.io.IOException;

public interface QueryService {
    // receive the Query from user
    void readData() throws IOException, ExceptionDB;
}
