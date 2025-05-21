package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.List;

public class LinguisticVariable {
    private String name;
    private List<LinguisticTerm> terms;

    public LinguisticVariable(String name) {
        this.name = name;
        terms = new ArrayList<>();
    }

    public void addTerm(LinguisticTerm term) {
        terms.add(term);
    }

    public List<LinguisticTerm> getTerms() {
        return terms;
    }

    public String getName() {
        return name;
    }
}
