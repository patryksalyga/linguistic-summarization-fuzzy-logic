package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private String name;
    private List<String> values;

    public Entity(String name) {
        this.name = name;
        values = new ArrayList<>();
    }

    public void addValue(String value) {
        values.add(value);
    }

    public List<String> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }
}
