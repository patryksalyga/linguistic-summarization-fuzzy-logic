package com.example.linguisticsummarizationfuzzylogic;

public class LinguisticTerm {
    private String label;
    private MembershipFunction membershipFunction;
    private boolean isEnabled;

    public LinguisticTerm(String label, MembershipFunction membershipFunction) {
        this.label = label;
        this.membershipFunction = membershipFunction;
        this.isEnabled = false;
    }

    public String getLabel() {
        return label;
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void toggle() {
        isEnabled = !isEnabled;
    }
}
