package com.example.linguisticsummarizationfuzzylogic;

public abstract class Quantifier {
    protected String label;
    protected MembershipFunction membershipFunction;
    protected boolean isEnabled;

    public Quantifier(String label, MembershipFunction membershipFunction) {
        this.label = label;
        this.membershipFunction = membershipFunction;
        this.isEnabled = false;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void toggle() {
        isEnabled = !isEnabled;
    }

    public double getMembership(double value) {
        return membershipFunction.getMembership(value);
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }
}
