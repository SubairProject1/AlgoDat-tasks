package main.java.framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class ResultContainer<ResultT extends Result> {

    private String groupName;
    private int number;
    private ResultT result;
    public ResultContainer(String groupName, int number, ResultT result) {
        this.groupName = groupName;
        this.number = number;
        this.result = result;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getNumber() {
        return number;
    }

    public List<String> getRequiredValues(List<Field> requiredValues) {
        List<String> values = new ArrayList<String>();
        for (Field requiredValue : requiredValues) {
            requiredValue.setAccessible(true);
            try {
                values.add(String.valueOf(requiredValue.get(this.result)));
            } catch (IllegalAccessException e) {
                // ERROR
            }
            requiredValue.setAccessible(false);
        }
        return values;
    }
}
