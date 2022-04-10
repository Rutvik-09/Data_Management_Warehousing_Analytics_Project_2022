package entity;

import Constants.Actions;

// Where condition for which value to match in which column
public class WhereCondition {

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public String getCol() {
        return col;
    }

    public String getValue() {
        return value;
    }

    private String value;
    private Actions actions;
    private String col;

    public void setValue(String value) {
        this.value = value;
    }


    public Actions getActions() {
        return actions;
    }

    public WhereCondition(){}

    public void setCol(String col) {
        this.col = col;
    }


}
