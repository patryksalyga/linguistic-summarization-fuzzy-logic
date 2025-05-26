package com.example.linguisticsummarizationfuzzylogic;

import java.util.LinkedHashMap;
import java.util.Map;

public class Entity {
    private String name;
    private Map<String, Boolean> values;

    public Entity(String name) {
        this.name = name;
        values = new LinkedHashMap<>();
    }

    public void addValue(String value) {
        values.put(value, false);
    }

    public Map<String, Boolean> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }
}
