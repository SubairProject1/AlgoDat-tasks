package main.java.framework;

import java.lang.reflect.Field;

public class NameFieldPair {
    private String name;
    private Field field;

    public NameFieldPair(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }
}
