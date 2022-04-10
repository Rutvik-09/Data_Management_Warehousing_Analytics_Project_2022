package entity;

import Constants.ValueTypes;

import java.util.Arrays;

// Stores Columns Data which is as
// column name - column1
// valueTypes - INT
// constraints - PRIMARY KEY, FOREIGN KEY
public class Columns {
    private String[] constraint;
    private String colName;
    private ValueTypes valTypes;


    public ValueTypes getValTypes() {
        return valTypes;
    }


    public String[] getConstraint() {
        return constraint;
    }


    public String getColName() {
        return colName;
    }


    public void setConstraint(String[] constraint) {
        this.constraint = constraint;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }


    public void setValTypes(ValueTypes valTypes) {
        this.valTypes = valTypes;
    }
}
