package com.example.linguisticsummarizationfuzzylogic;

public class TriangularMembershipFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;

    public TriangularMembershipFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double getMembership(double value) {
        if (value < a || value > c) {
            return 0.0;
        } else if (value == b) {
            return 1.0;
        } else if (value < b) {
            return (value - a) / (b - a);
        } else {
            return (c - value) / (c - b);
        }
    }
}
