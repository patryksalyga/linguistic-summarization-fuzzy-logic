package com.example.linguisticsummarizationfuzzylogic;

public abstract class Quantifier {
    protected String label;
    protected MembershipFunction membershipFunction;

    public Quantifier(String label, MembershipFunction membershipFunction) {
        this.label = label;
        this.membershipFunction = membershipFunction;
    }

    public String getLabel() {
        return label;
    }

    public double getMembership(double value) {
        return membershipFunction.getMembership(value);
    }
}
