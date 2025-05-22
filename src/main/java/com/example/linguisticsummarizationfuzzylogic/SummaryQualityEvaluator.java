package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryQualityEvaluator {
    private LinguisticSummary linguisticSummary;
    private Map<String, Double> qualityMeasures = new HashMap<>();
    private double overallQuality;

    public SummaryQualityEvaluator(LinguisticSummary linguisticSummary) {
        this.linguisticSummary = linguisticSummary;
        evaluate();
    }

    private void evaluate() {
        qualityMeasures.put("T1", computeT1());
        qualityMeasures.put("T2", computeT2());
        qualityMeasures.put("T3", computeT3());
        qualityMeasures.put("T4", computeT4());
        qualityMeasures.put("T5", computeT5());
        qualityMeasures.put("T6", computeT6());
        qualityMeasures.put("T7", computeT7());
        qualityMeasures.put("T8", computeT8());
        qualityMeasures.put("T9", computeT9());
        qualityMeasures.put("T10", computeT10());
        qualityMeasures.put("T11", computeT11());
        overallQuality = computeOverallQuality();
    }

    public double get(String key) {
        return qualityMeasures.getOrDefault(key, 0.0);
    }

    public Map<String, Double> getAll() {
        return qualityMeasures;
    }

    private double computeT1() {
        // Degree of Truth
        return linguisticSummary.getQuantifier().getMembership((double) linguisticSummary.getFuzzySet().getSupport().size() / linguisticSummary.getFuzzySet().getUniverseOfDiscourse().size());
    }

    private double computeT2() {
        // Degree of Imprecision
        List<Double> count = new ArrayList<>();
        for (FuzzySet fuzzySet : linguisticSummary.getSummarizators()) {
            count.add((double) fuzzySet.getSupport().size() / fuzzySet.getUniverseOfDiscourse().size());
        }

        double product = 1.0;
        for (Double value : count) {
            product *= value;
        }

        return 1.0 - Math.pow(product, 0.5);
    }

    private double computeT3() {
        // Degree of Coverage
        if (linguisticSummary.getFuzzySet().getUniverseOfDiscourse().isEmpty()) {
            return 0.0;
        }

        return (double) linguisticSummary.getFuzzySet().getSupport().size() / linguisticSummary.getFuzzySet().getUniverseOfDiscourse().size();
    }

    private double computeT4() {
        // Degree of appropriateness
        List<Double> count = new ArrayList<>();
        for (FuzzySet fuzzySet : linguisticSummary.getSummarizators()) {
            count.add((double) fuzzySet.getSupport().size() / linguisticSummary.getElectoralDistrictsCount());
        }

        double product = 1.0;
        for (Double value : count) {
            product *= value;
        }

        return Math.abs(product - qualityMeasures.get("T3"));
    }

    private double computeT5() {
        // Length of a summary
        return 2 * Math.pow(0.5, linguisticSummary.getSummarizators().size());
    }

    private double computeT6() {
        // Degree of quantifier imprecision
        if (linguisticSummary.getQuantifier().getClass() == AbsoluteQuantifier.class) {
            return linguisticSummary.getQuantifier().getMembershipFunction().getLength() / linguisticSummary.getElectoralDistrictsCount();
        } else if (linguisticSummary.getQuantifier().getClass() == RelativeQuantifier.class) {
            return linguisticSummary.getQuantifier().getMembershipFunction().getLength();
        } else {
            return 0.0;
        }
    }

    private double computeT7() {
        // Degree of quantifier cardinality
        if (linguisticSummary.getQuantifier().getClass() == AbsoluteQuantifier.class) {
            return linguisticSummary.getQuantifier().getMembershipFunction().getClm() / linguisticSummary.getElectoralDistrictsCount();
        } else if (linguisticSummary.getQuantifier().getClass() == RelativeQuantifier.class) {
            return linguisticSummary.getQuantifier().getMembershipFunction().getClm();
        } else {
            return 0.0;
        }
    }

    private double computeT8() {
        // Degree of summarizer cardinality
        double product = 1.0;
        int count = 0;

        for (FuzzySet fuzzySet : linguisticSummary.getSummarizators()) {
            double normalizedCardinality = fuzzySet.cardinality() / (double) fuzzySet.getUniverseOfDiscourse().size();
            product *= normalizedCardinality;
            count++;
        }

        return 1.0 - Math.pow(product, 1.0 / count);
    }

    private double computeT9() {
        // Degree of qualifier imprecision
        double product = 1.0;
        int count = 0;

        if (linguisticSummary.getQualifiers().isEmpty()) {
            return 1.0;
        }

        for (FuzzySet fuzzySet : linguisticSummary.getQualifiers()) {
            double normalizedCardinality = (double) fuzzySet.getSupport().size() / fuzzySet.getUniverseOfDiscourse().size();
            product *= normalizedCardinality;
            count++;
        }

        return 1.0 - Math.pow(product, 1.0 / count);
    }

    private double computeT10() {
        // Degree of qualifier cardinality
        double product = 1.0;
        int count = 0;

        if (linguisticSummary.getQualifiers().isEmpty()) {
            return 1.0;
        }

        for (FuzzySet fuzzySet : linguisticSummary.getQualifiers()) {
            double normalizedCardinality = fuzzySet.cardinality() / (double) fuzzySet.getUniverseOfDiscourse().size();
            product *= normalizedCardinality;
            count++;
        }

        return 1.0 - Math.pow(product, 1.0 / count);
    }

    private double computeT11() {
        int length = linguisticSummary.getQualifiers().isEmpty() ? 1 : linguisticSummary.getQualifiers().size();
        return 2 * Math.pow(0.5, length);
    }

    private double computeOverallQuality() {
        for (String key : qualityMeasures.keySet()) {
            if (key.equals("T1")) {
                overallQuality += qualityMeasures.get(key) * 0.7;
            } else {
                overallQuality += qualityMeasures.get(key) * 0.03;
            }
        }
        return overallQuality;
    }

    public double getOverallQuality() {
        return overallQuality;
    }
}
