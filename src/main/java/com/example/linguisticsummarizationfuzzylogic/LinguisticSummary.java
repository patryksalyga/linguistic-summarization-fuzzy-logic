package com.example.linguisticsummarizationfuzzylogic;

import java.util.List;

public class LinguisticSummary {
    private FuzzySet fuzzySet;
    private Quantifier quantifier;

    private String text;

    private double value;

    public LinguisticSummary(FuzzySet fuzzySet, Quantifier quantifier, String summary) {
        this.fuzzySet = fuzzySet;
        this.quantifier = quantifier;
        text = quantifier.getLabel() + " obwodów wyborczych są/mają " + summary + ".";
    }

    public LinguisticSummary(List<FuzzySet> fuzzySets, Quantifier quantifier, List<String> summarys) {
        //TODO: multiple fuzzy sets logic
        this.quantifier = quantifier;
        text = quantifier.getLabel() + " obwodów wyborczych są/mają " + summarys.get(0);
        for (int i = 1; i < summarys.size(); i++) {
            text += " oraz " + summarys.get(i);
        }
        text += ".";
    }

    public String getText() {
        return text;
    }
}
