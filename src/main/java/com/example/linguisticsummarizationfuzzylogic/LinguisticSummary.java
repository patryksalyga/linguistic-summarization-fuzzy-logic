package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinguisticSummary {
    private FuzzySet fuzzySet;
    private Quantifier quantifier;
    private List<FuzzySet> summarizators;
    private List<FuzzySet> qualifiers;

    private String text;

    private SummaryQualityEvaluator summaryQualityEvaluator;

    private int electoralDistrictsCount;

    public LinguisticSummary(FuzzySet fuzzySet, Quantifier quantifier, String summary, int electoralDistrictsCount) {
        this.fuzzySet = fuzzySet;
        this.summarizators = new ArrayList<>();
        this.summarizators.add(fuzzySet);
        this.quantifier = quantifier;
        this.qualifiers = new ArrayList<>();
        text = quantifier.getLabel() + " obwodów wyborczych są/mają " + summary + ".";
        this.electoralDistrictsCount = electoralDistrictsCount;
        summaryQualityEvaluator = new SummaryQualityEvaluator(this);
    }

    public LinguisticSummary(List<FuzzySet> fuzzySets, Quantifier quantifier, List<String> summarys, int electoralDistrictsCount) {
        this.summarizators = fuzzySets;

        int length = fuzzySets.get(0).getUniverseOfDiscourse().size();
        Map<Integer, Double> elements = new HashMap<>();
        for (int i = 0; i < length; i++) {
            double min = Double.POSITIVE_INFINITY;
            for (FuzzySet fuzzySet : fuzzySets) {
                double membershipValue = fuzzySet.getElements().get(i) != null ? fuzzySet.getElements().get(i) : 0.0;
                if (membershipValue < min) {
                    min = membershipValue;
                }
            }
            elements.put(i, min);
        }
        this.fuzzySet = new FuzzySet(elements, fuzzySets.get(0).getUniverseOfDiscourse());

        this.quantifier = quantifier;
        this.qualifiers = new ArrayList<>();

        text = quantifier.getLabel() + " obwodów wyborczych są/mają " + summarys.get(0);
        for (int i = 1; i < summarys.size(); i++) {
            text += " oraz " + summarys.get(i);
        }
        text += ".";

        this.electoralDistrictsCount = electoralDistrictsCount;

        summaryQualityEvaluator = new SummaryQualityEvaluator(this);
    }

    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }

    public String getText() {
        return text + " [" + summaryQualityEvaluator.getOverallQuality() + "] [" + summaryQualityEvaluator.get("T1") + "] [" + summaryQualityEvaluator.get("T2") + "] [" + summaryQualityEvaluator.get("T3") + "] [" + summaryQualityEvaluator.get("T4") + "] [" + summaryQualityEvaluator.get("T5") + "] [" + summaryQualityEvaluator.get("T6") + "] [" + summaryQualityEvaluator.get("T7") + "] [" + summaryQualityEvaluator.get("T8") + "] [" + summaryQualityEvaluator.get("T9") + "] [" + summaryQualityEvaluator.get("T10") + "] [" + summaryQualityEvaluator.get("T11") + "]";
    }

    public List<FuzzySet> getSummarizators() {
        return summarizators;
    }

    public Quantifier getQuantifier() {
        return quantifier;
    }

    public List<FuzzySet> getQualifiers() {
        return qualifiers;
    }

    public int getElectoralDistrictsCount() {
        return electoralDistrictsCount;
    }

    public SummaryQualityEvaluator getSummaryQualityEvaluator() {
        return summaryQualityEvaluator;
    }
}
