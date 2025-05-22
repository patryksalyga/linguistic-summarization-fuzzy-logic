package com.example.linguisticsummarizationfuzzylogic;

import java.util.*;

public class FuzzySet {
    private final Map<Integer, Double> elements;
    private final List<Double> values;
    private final MembershipFunction membershipFunction;

    public FuzzySet(List<Double> values, MembershipFunction membershipFunction) {
        elements = new HashMap<>();
        this.values = values;
        this.membershipFunction = membershipFunction;

        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);
            double membershipValue = membershipFunction.getMembership(value);
            elements.put(i, membershipValue);
        }
    }

    public FuzzySet(Map<Integer, Double> elements, List<Double> values, MembershipFunction membershipFunction) {
        this.elements = elements;
        this.membershipFunction = membershipFunction;
        this.values = values;
    }

    public FuzzySet(Map<Integer, Double> elements, List<Double> values) {
        this.elements = elements;
        this.values = values;
        this.membershipFunction = null; // Default to null if not provided
    }

    public List<Double> getUniverseOfDiscourse() {
        return values;
    }

    public List<Double> getSupport() {
        List<Double> support = new ArrayList<>();
        elements.keySet().stream()
                .sorted()
                .forEach(key -> {
                    Double value = elements.get(key);
                    if (value != null && value > 0) {
                        support.add(values.get(key));
                    }
                });

        return support;
    }

    public FuzzySet normalize() {
        double maxMembership = Collections.max(elements.values());
        double minMembership = Collections.min(elements.values());

        if (maxMembership == minMembership) {
            return this; // No normalization needed
        }

        List<Double> normalizedValues = new ArrayList<>();
        for (Double value : values) {
            double normalizedValue = (value - minMembership) / (maxMembership - minMembership);
            normalizedValues.add(normalizedValue);
        }

        return new FuzzySet(normalizedValues, membershipFunction);
    }

    public double DoF() {
        return (double) getSupport().size() / values.size();
    }

    public boolean isNormal() {
        return 1.0 == Collections.max(elements.values());
    }

    public List<Double> alphaCut(double alpha) {
        List<Double> alphaCut = new ArrayList<>();
        elements.keySet().stream()
                .filter(key -> elements.get(key) >= alpha)
                .sorted()
                .forEach(key -> {
                    double value = elements.get(key);
                    if (value >= alpha) {
                        alphaCut.add(values.get(key));
                    }
                });
        return alphaCut;
    }

    public boolean isConvex() {
        List<Integer> sortedKeys = elements.keySet().stream().sorted().toList();
        for (int i = 1; i < sortedKeys.size() - 1; i++) {
            double muPrev = elements.get(sortedKeys.get(i - 1));
            double muCurr = elements.get(sortedKeys.get(i));
            double muNext = elements.get(sortedKeys.get(i + 1));
            if (muCurr < Math.min(muPrev, muNext)) {
                return false;
            }
        }
        return true;
    }

    public FuzzySet power(double r) {
        Map<Integer, Double> powerElements = new HashMap<>(elements);
        powerElements.replaceAll((key, value) -> Math.pow(value, r));
        return new FuzzySet(powerElements, values, membershipFunction);
    }

    public double cardinality() {
        double sum = 0;
        for (Double value : elements.values()) {
            sum += value;
        }
        return sum; // Placeholder
    }

    public FuzzySet comp() {
        Map<Integer, Double> powerElements = new HashMap<>(elements);
        powerElements.replaceAll((key, value) -> 1 - value);
        return new FuzzySet(powerElements, values, membershipFunction);
    }

    public Map<Integer, Double> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return "FuzzySet{" +
                "elements=" + elements +
                '}';
    }
}
