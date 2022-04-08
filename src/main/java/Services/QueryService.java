package Services;

import CustomExceptions.DBException;

import java.io.IOException;

public interface QueryService {
    // receive the Query from user
    void read() throws IOException, DBException;
}
