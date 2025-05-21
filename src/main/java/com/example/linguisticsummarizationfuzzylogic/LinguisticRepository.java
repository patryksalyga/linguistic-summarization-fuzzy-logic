package com.example.linguisticsummarizationfuzzylogic;

import com.opencsv.CSVReader;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LinguisticRepository {
    private List<LinguisticVariable> linguisticVariables;

    public LinguisticRepository() {
        linguisticVariables = new ArrayList<>();
    }

    public void addLinguisticVariable(LinguisticVariable variable) {
        linguisticVariables.add(variable);
    }

    public List<LinguisticVariable> getLinguisticVariables() {
        return linguisticVariables;
    }

    public void loadFromCSV(String featuresPath) throws Exception {
        try (Reader reader = Files.newBufferedReader(Path.of(featuresPath))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.readNext(); // Skip header
                String[] line;
                String currentVariableName = null;
                int currentVariableIndex = -1;
                while ((line = csvReader.readNext()) != null) {
                    if (currentVariableName == null || !currentVariableName.equals(line[0])) {
                        currentVariableIndex++;
                        currentVariableName = line[0];
                        linguisticVariables.add(new LinguisticVariable(line[0]));
                    }

                    if (line[2].equals("trapezoidal")) {
                        linguisticVariables.get(currentVariableIndex).addTerm(
                                new LinguisticTerm(
                                        line[1],
                                        new TrapezoidalMembershipFunction(
                                                CsvUtils.parseSpecialDouble(line[3]),
                                                CsvUtils.parseSpecialDouble(line[4]),
                                                CsvUtils.parseSpecialDouble(line[5]),
                                                CsvUtils.parseSpecialDouble(line[6])
                                        )
                                )
                        );
                    } else if (line[2].equals("triangular")) {
                        linguisticVariables.get(currentVariableIndex).addTerm(
                                new LinguisticTerm(
                                        line[1],
                                        new TriangularMembershipFunction(
                                                CsvUtils.parseSpecialDouble(line[3]),
                                                CsvUtils.parseSpecialDouble(line[4]),
                                                CsvUtils.parseSpecialDouble(line[5])
                                        )
                                )
                        );
                    }
                }
            }
        }
    }
}
