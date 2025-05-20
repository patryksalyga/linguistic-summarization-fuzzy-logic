package com.example.linguisticsummarizationfuzzylogic;

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
}
