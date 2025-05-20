package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MembershipFunctionTest {
    @Test
    public void testTrapezoidalMembershipFunction1() {
        TrapezoidalMembershipFunction trapezoidalMembershipFunction1 = new TrapezoidalMembershipFunction(0.60, 0.70, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double value1 = 0.60;
        double value2 = 0.70;
        double value3 = 0.80;
        double value4 = 0.90;
        double value5 = 0.65;

        assertEquals(0.0, trapezoidalMembershipFunction1.getMembership(value1), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction1.getMembership(value2), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction1.getMembership(value3), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction1.getMembership(value4), 0.0001);
        assertEquals(0.5, trapezoidalMembershipFunction1.getMembership(value5), 0.0001);
    }

    @Test
    public void testTrapezoidalMembershipFunction2() {
        TrapezoidalMembershipFunction trapezoidalMembershipFunction2 = new TrapezoidalMembershipFunction(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.10, 0.11);

        double value1 = 0.0;
        double value2 = 0.05;
        double value3 = 0.10;
        double value4 = 0.105;
        double value5 = 0.11;

        assertEquals(1.0, trapezoidalMembershipFunction2.getMembership(value1), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction2.getMembership(value2), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction2.getMembership(value3), 0.0001);
        assertEquals(0.5, trapezoidalMembershipFunction2.getMembership(value4), 0.0001);
        assertEquals(0.0, trapezoidalMembershipFunction2.getMembership(value5), 0.0001);
    }

    @Test
    public void testTrapezoidalMembershipFunction3() {
        TrapezoidalMembershipFunction trapezoidalMembershipFunction3 = new TrapezoidalMembershipFunction(0.45, 0.45, 0.50, 0.50);

        double value1 = 0.40;
        double value2 = 0.45;
        double value3 = 0.50;
        double value4 = 0.55;

        assertEquals(0.0, trapezoidalMembershipFunction3.getMembership(value1), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction3.getMembership(value2), 0.0001);
        assertEquals(1.0, trapezoidalMembershipFunction3.getMembership(value3), 0.0001);
        assertEquals(0.0, trapezoidalMembershipFunction3.getMembership(value4), 0.0001);
    }

    @Test
    public void testTrapezoidalMembershipFunction4() {
        TrapezoidalMembershipFunction trapezoidalMembershipFunction4 = new TrapezoidalMembershipFunction(0.995, 1.0, 1.0, 1.0);
        double value1 = 1.0;

        assertEquals(1.0, trapezoidalMembershipFunction4.getMembership(value1), 0.0001);
    }

    @Test
    public void testTriangularMembershipFunction1() {
        TriangularMembershipFunction triangularMembershipFunction1 = new TriangularMembershipFunction(0.30, 0.45, 0.60);
        double value1 = 0.30;
        double value2 = 0.45;
        double value3 = 0.60;
        double value4 = 0.50;

        assertEquals(0.0, triangularMembershipFunction1.getMembership(value1), 0.0001);
        assertEquals(1.0, triangularMembershipFunction1.getMembership(value2), 0.0001);
        assertEquals(0.0, triangularMembershipFunction1.getMembership(value3), 0.0001);
        assertEquals(0.6666, triangularMembershipFunction1.getMembership(value4), 0.0001);
    }

    @Test
    public void testTriangularMembershipFunction2() {
        TriangularMembershipFunction triangularMembershipFunction2 = new TriangularMembershipFunction(0.10, 0.10, 0.10);
        double value1 = 0.09;
        double value2 = 0.1;
        double value3 = 0.11;

        assertEquals(0.0, triangularMembershipFunction2.getMembership(value1), 0.0001);
        assertEquals(1.0, triangularMembershipFunction2.getMembership(value2), 0.0001);
        assertEquals(0.0, triangularMembershipFunction2.getMembership(value3), 0.0001);
    }

}
