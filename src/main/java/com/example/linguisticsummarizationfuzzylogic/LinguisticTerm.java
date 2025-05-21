package com.example.linguisticsummarizationfuzzylogic;

public class LinguisticTerm {
    private String label;
    private MembershipFunction membershipFunction;

    public LinguisticTerm(String label, MembershipFunction membershipFunction) {
        this.label = label;
        this.membershipFunction = membershipFunction;
    }

    public String getLabel() {
        return label;
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }
}
