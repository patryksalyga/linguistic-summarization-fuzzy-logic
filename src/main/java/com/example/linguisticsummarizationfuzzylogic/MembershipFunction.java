package com.example.linguisticsummarizationfuzzylogic;

public interface MembershipFunction {
    double getMembership(double value);
    double getLength();
    double getClm();

    double getLength(int electoralDistrictsCount);
    double getClm(int electoralDistrictsCount);
}
