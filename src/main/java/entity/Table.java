package entity;

import java.util.ArrayList;
import java.util.Arrays;

// Table Object stores as
// columnNames - [column1, column2, column3] - header
// rows - {[1,2,3],[4,5,6]}; - data
public class Table {
    private ArrayList<String[]> data;
    private String[] colNames;

    public void setRow(String[] rows) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(rows);
    }

    public ArrayList<String[]> getData() {
        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setColNames(String[] colNames) {
        this.colNames = colNames;
    }

    public void setData(ArrayList<String[]> data) {
        this.data = data;
    }


}
