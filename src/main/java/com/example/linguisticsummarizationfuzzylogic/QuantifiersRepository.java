package com.example.linguisticsummarizationfuzzylogic;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QuantifiersRepository {
    private List<AbsoluteQuantifier> absoluteQuantifiers;
    private List<RelativeQuantifier> relativeQuantifiers;

    public QuantifiersRepository() {
        absoluteQuantifiers = new ArrayList<>();
        relativeQuantifiers = new ArrayList<>();
    }

    public void addAbsoluteQuantifier(AbsoluteQuantifier quantifier) {
        absoluteQuantifiers.add(quantifier);
    }

    public void addRelativeQuantifier(RelativeQuantifier quantifier) {
        relativeQuantifiers.add(quantifier);
    }

    public List<AbsoluteQuantifier> getAbsoluteQuantifiers() {
        return absoluteQuantifiers;
    }

    public List<RelativeQuantifier> getRelativeQuantifiers() {
        return relativeQuantifiers;
    }

    public void loadFromCSV(String absoluteQuantifiersPath, String relativeQuantifiersPath) throws Exception {
        try (Reader reader = Files.newBufferedReader(Path.of(absoluteQuantifiersPath))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.readNext(); // Skip header
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    if (line[1].equals("trapezoidal")) {
                        absoluteQuantifiers.add(
                                new AbsoluteQuantifier(
                                        line[0],
                                        new TrapezoidalMembershipFunction(
                                                CsvUtils.parseSpecialDouble(line[2]),
                                                CsvUtils.parseSpecialDouble(line[3]),
                                                CsvUtils.parseSpecialDouble(line[4]),
                                                CsvUtils.parseSpecialDouble(line[5])
                                        )
                                )
                        );
                    } else if (line[1].equals("triangular")) {
                        absoluteQuantifiers.add(
                                new AbsoluteQuantifier(
                                        line[0],
                                        new TriangularMembershipFunction(
                                                CsvUtils.parseSpecialDouble(line[2]),
                                                CsvUtils.parseSpecialDouble(line[3]),
                                                CsvUtils.parseSpecialDouble(line[4]
                                                )
                                        )
                                )
                        );
                    }
                }
            }
        }

        try (Reader reader = Files.newBufferedReader(Path.of(relativeQuantifiersPath))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.readNext(); // Skip header
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    if (line[1].equals("trapezoidal")) {
                        relativeQuantifiers.add(
                                new RelativeQuantifier(
                                        line[0],
                                        new TrapezoidalMembershipFunction(
                                                CsvUtils.parseSpecialDouble(line[2]),
                                                CsvUtils.parseSpecialDouble(line[3]),
                                                CsvUtils.parseSpecialDouble(line[4]),
                                                CsvUtils.parseSpecialDouble(line[5])
                                        )
                                )
                        );

                    } else if (line[1].equals("triangular")) {
                        relativeQuantifiers.add(
                                new RelativeQuantifier(
                                        line[0],
                                        new TriangularMembershipFunction(
                                                CsvUtils.parseSpecialDouble(line[2]),
                                                CsvUtils.parseSpecialDouble(line[3]),
                                                CsvUtils.parseSpecialDouble(line[4])
                                        )
                                )
                        );
                    }
                }
            }
        }
    }
}
