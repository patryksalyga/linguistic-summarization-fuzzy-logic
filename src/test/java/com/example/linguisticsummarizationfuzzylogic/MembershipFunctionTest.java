package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MembershipFunctionTest {
    private static TrapezoidalMembershipFunction trapezoidalMembershipFunction;
    private static TriangularMembershipFunction triangularMembershipFunction;

    @BeforeAll
    public static void setUp() {
        trapezoidalMembershipFunction = new TrapezoidalMembershipFunction(0.60, 0.70, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        triangularMembershipFunction = new TriangularMembershipFunction(0.30, 0.45, 0.60);
    }

    @Test
    public void testTrapezoidalMembershipFunction() {
        double value1 = 0.60;
        double value2 = 0.70;
        double value3 = 0.80;
        double value4 = 0.90;
        double value5 = 0.65;

        assertEquals(0.0, trapezoidalMembershipFunction.getMembership(value1), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction.getMembership(value2), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction.getMembership(value3), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction.getMembership(value4), 0.0001);
        assertEquals(0.5, trapezoidalMembershipFunction.getMembership(value5), 0.0001);
    }

    @Test
    public void testTriangularMembershipFunction() {
        double value1 = 0.30;
        double value2 = 0.45;
        double value3 = 0.60;
        double value4 = 0.50;

        assertEquals(0.0, triangularMembershipFunction.getMembership(value1), 0.0001);
        assertEquals(1.0, triangularMembershipFunction.getMembership(value2), 0.0001);
        assertEquals(0.0, triangularMembershipFunction.getMembership(value3), 0.0001);
        assertEquals(0.6666, triangularMembershipFunction.getMembership(value4), 0.0001);
    }

}
