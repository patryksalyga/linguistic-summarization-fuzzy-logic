package com.example.linguisticsummarizationfuzzylogic;

import com.opencsv.CSVReader;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EntityRepository {
    private List<Entity> entities;

    public EntityRepository() {
        entities = new ArrayList<>();
    }

    public void loadFromCSV(String entitiesPath) throws Exception {
        try (Reader reader = Files.newBufferedReader(Path.of(entitiesPath))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.readNext(); // Skip header
                String[] line;
                String currentVariableName = null;
                int currentVariableIndex = -1;
                Entity currentEntity = null;
                while ((line = csvReader.readNext()) != null) {
                    if (currentVariableName == null || !currentVariableName.equals(line[0])) {
                        currentVariableIndex++;
                        currentVariableName = line[0];
                        currentEntity = new Entity(line[0]);
                        entities.add(currentEntity);
                    }

                    currentEntity.addValue(line[1]);
                }
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
