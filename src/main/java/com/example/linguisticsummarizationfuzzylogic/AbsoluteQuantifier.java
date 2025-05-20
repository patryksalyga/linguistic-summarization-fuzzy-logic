package com.example.linguisticsummarizationfuzzylogic;

public class AbsoluteQuantifier extends Quantifier{
    public AbsoluteQuantifier(String label, MembershipFunction membershipFunction) {
        super(label, membershipFunction);
    }

    @Override
    public String toString() {
        return "AbsoluteQuantifier{" +
                "label='" + label + '\'' +
                ", membershipFunction=" + membershipFunction +
                '}';
    }
}
