package Services;

import CustomExceptions.ExceptionDB;
import entity.Columns;
import entity.DBResponse;
import entity.Table;
import entity.WhereCondition;

import java.io.IOException;
import java.util.List;

public interface DBServices {

    DBResponse TabDrop(String name1);
    DBResponse DBcreate(String name);

    DBResponse TabUpdate(String name1, String name2, String name3, WhereCondition name4) throws IOException, ExceptionDB;

    DBResponse TabInsert(String name1, Table name2) throws IOException, ExceptionDB;


    DBResponse DBuse(String name);

    DBResponse TabSelect(String name1, String name2, WhereCondition name3) throws IOException, ExceptionDB;

    DBResponse Tabcreate(String name1, List<Columns> name2) throws IOException, ExceptionDB;


    DBResponse TabDelete(String name1, WhereCondition name2) throws IOException, ExceptionDB;



}
