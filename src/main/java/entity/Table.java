package entity;

import java.util.ArrayList;
import java.util.Arrays;

// Table Object stores as
// columnNames - [column1, column2, column3] - header
// rows - {[1,2,3],[4,5,6]}; - data
public class Table {
    private String[] columnNames;
    private ArrayList<String[]> data;

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public ArrayList<String[]> getData() {
        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(ArrayList<String[]> data) {
        this.data = data;
    }

    public void setRow(String[] rows) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(rows);
    }

    @Override
    public String toString() {
        return "Table{" +
                "columnNames=" + Arrays.toString(columnNames) +
                ", data=" + data +
                '}';
    }
}
