package com.example.linguisticsummarizationfuzzylogic;

public class RelativeQuantifier extends Quantifier {
    public RelativeQuantifier(String label, MembershipFunction membershipFunction) {
        super(label, membershipFunction);
    }

    @Override
    public String toString() {
        return "RelativeQuantifier{" +
                "label='" + label + '\'' +
                ", membershipFunction=" + membershipFunction +
                '}';
    }
}
