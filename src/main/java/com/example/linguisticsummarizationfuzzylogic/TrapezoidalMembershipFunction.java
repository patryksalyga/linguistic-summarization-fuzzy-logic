package com.example.linguisticsummarizationfuzzylogic;

public class TrapezoidalMembershipFunction implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private double d;

    public TrapezoidalMembershipFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public double getMembership(double value) {
        if (value < a || value > d) {
            return 0.0;
        } else if (value >= a && value <= b) {
            if (b == a) {
                return 1.0;
            }
            return (value - a) / (b - a);
        } else if (value >= b && value <= c) {
            return 1.0;
        } else if (value >= c && value <= d) {
            if (d == c) {
                return 1.0;
            }
            return (d - value) / (d - c);
        }
        return 0.0;
    }
}
