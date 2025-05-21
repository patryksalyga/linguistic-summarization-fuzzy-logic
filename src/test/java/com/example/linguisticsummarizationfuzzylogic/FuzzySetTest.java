package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FuzzySetTest {
    private static List<Double> elements;
    private static TriangularMembershipFunction membershipFunction;
    private static FuzzySet fuzzySet;


    @BeforeAll
    public static void setUp() {
        elements = List.of(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0);
        membershipFunction = new TriangularMembershipFunction(0.3, 0.5, 0.7);
        // Create a fuzzy set with some sample values
        fuzzySet = new FuzzySet(elements, membershipFunction);
    }

    @Test
    public void testFuzzySet() {
        //Test values
        assertEquals(0.0, fuzzySet.getElements().get(3), 0.01);
        assertEquals(0.5, fuzzySet.getElements().get(4), 0.01);
        assertEquals(1.0, fuzzySet.getElements().get(5), 0.01);
        assertEquals(0.5, fuzzySet.getElements().get(6), 0.01);
        assertEquals(0.0, fuzzySet.getElements().get(7), 0.01);
    }

    @Test
    public void testFuzzySetSupport() {
        // Test support
        List<Double> support = fuzzySet.getSupport();
        assertEquals(3, support.size());

        assertEquals(0.4, support.get(0), 0.01);
        assertEquals(0.5, support.get(1), 0.01);
        assertEquals(0.6, support.get(2), 0.01);
    }

    @Test
    public void testFuzzySetNormalize() {
        // Test normalization
        FuzzySet beforeNormalization = new FuzzySet(List.of(0.0, 0.1, 0.2), new TrapezoidalMembershipFunction(0.0, 0.5, 1.0, 1.5));
        assertEquals(0.0, beforeNormalization.getElements().get(0), 0.01);
        assertEquals(0.2, beforeNormalization.getElements().get(1), 0.01);
        assertEquals(0.4, beforeNormalization.getElements().get(2), 0.01);
        FuzzySet normalizedFuzzySet = beforeNormalization.normalize();
        assertEquals(0.0, beforeNormalization.getElements().get(0), 0.01);
        assertEquals(0.2, beforeNormalization.getElements().get(1), 0.01);
        assertEquals(0.4, beforeNormalization.getElements().get(2), 0.01);
        assertEquals(0.0, normalizedFuzzySet.getElements().get(0), 0.01);
        assertEquals(0.5, normalizedFuzzySet.getElements().get(1), 0.01);
        assertEquals(1.0, normalizedFuzzySet.getElements().get(2), 0.01);
    }

    @Test
    public void testFuzzySetDoF() {
        // Test Degree of Fuzziness
        double dof = fuzzySet.DoF();
        assertEquals(0.15, dof, 0.01);
    }

    @Test
    public void testFuzzySetIsNormal() {
        // Test if the fuzzy sets are normal
        assertTrue(fuzzySet.isNormal());

        // Test if the fuzzy sets are not normal
        FuzzySet nonNormalFuzzySet = new FuzzySet(List.of(0.0, 0.1, 0.2), new TrapezoidalMembershipFunction(0.0, 0.5, 1.0, 1.5));
        assertFalse(nonNormalFuzzySet.isNormal());
    }

    @Test
    public void testFuzzySetAlphaCut() {
        // Test alpha cut
        List<Double> alphaCut = fuzzySet.alphaCut(0.5);
        assertEquals(3, alphaCut.size());
        assertEquals(0.4, alphaCut.get(0), 0.01);
        assertEquals(0.5, alphaCut.get(1), 0.01);
        assertEquals(0.6, alphaCut.get(2), 0.01);

        // Test alpha cut with a different alpha value
        FuzzySet alphaCutFuzzySet = new FuzzySet(List.of(0.0, 0.1, 0.2, 0.3, 0.4), new TrapezoidalMembershipFunction(0.0, 0.35, 0.5, 0.7));
        List<Double> alphaCut2 = alphaCutFuzzySet.alphaCut(0.8);
        assertEquals(2, alphaCut2.size());
        assertEquals(0.3, alphaCut2.get(0), 0.01);
        assertEquals(0.4, alphaCut2.get(1), 0.01);
    }

    @Test
    public void testFuzzySetPower() {
        // Test power
        FuzzySet poweredFuzzySet = fuzzySet.power(2);
        assertEquals(0.5, fuzzySet.getElements().get(4), 0.01);
        assertEquals(0.25, poweredFuzzySet.getElements().get(4), 0.01);

        // Test power with a different exponent
        FuzzySet poweredFuzzySet2 = fuzzySet.power(0.25);
        assertEquals(0.5, fuzzySet.getElements().get(4), 0.01);
        assertEquals(0.840896, poweredFuzzySet2.getElements().get(4), 0.01);

    }

    @Test
    public void testFuzzySetCardinality() {
        // Test cardinality
        assertEquals(2, fuzzySet.cardinality());

        // Test cardinality with a different fuzzy set
        FuzzySet cardinalityFuzzySet = new FuzzySet(List.of(0.0, 0.1, 0.2, 0.3, 0.4), new TrapezoidalMembershipFunction(0.0, 0.3, 0.35, 0.45));
        assertEquals(2.49, cardinalityFuzzySet.cardinality(), 0.01);
    }

    @Test
    public void testFuzzySetComp() {
        // Test complement
        FuzzySet complementedFuzzySet = fuzzySet.comp();
        assertEquals(1.0, complementedFuzzySet.getElements().get(2), 0.01);
        assertEquals(1.0, complementedFuzzySet.getElements().get(3), 0.01);
        assertEquals(0.5, complementedFuzzySet.getElements().get(4), 0.01);
        assertEquals(0.0, complementedFuzzySet.getElements().get(5), 0.01);
        assertEquals(0.5, complementedFuzzySet.getElements().get(6), 0.01);
        assertEquals(1.0, complementedFuzzySet.getElements().get(7), 0.01);
        assertEquals(1.0, complementedFuzzySet.getElements().get(8), 0.01);

        assertEquals(0.0, fuzzySet.getElements().get(3), 0.01);
        assertEquals(0.5, fuzzySet.getElements().get(4), 0.01);
        assertEquals(1.0, fuzzySet.getElements().get(5), 0.01);
        assertEquals(0.5, fuzzySet.getElements().get(6), 0.01);
        assertEquals(0.0, fuzzySet.getElements().get(7), 0.01);
    }

    @Test
    public void testFuzzySetIsConvex() {
        // Test convexity
        assertTrue(fuzzySet.isConvex());

        // Test non-convexity
        List<Double> values = List.of(0.0, 1.0, 2.0, 3.0, 4.0);
        Map<Integer, Double> elements = new HashMap<>();
        elements.put(0, 0.8);
        elements.put(1, 1.0);
        elements.put(2, 0.2);
        elements.put(3, 1.0);
        elements.put(4, 0.8);

        FuzzySet nonConvexSet = new FuzzySet(elements, values, null);
        assertFalse(nonConvexSet.isConvex());
    }

    @Test
    public void testFuzzySetgetUniverseOfDiscourse() {
        // Test universe of discourse
        List<Double> universeOfDiscourse = fuzzySet.getUniverseOfDiscourse();
        assertEquals(0.0, universeOfDiscourse.get(0), 0.01);
        assertEquals(0.1, universeOfDiscourse.get(1), 0.01);
        assertEquals(0.2, universeOfDiscourse.get(2), 0.01);
        assertEquals(2.0, universeOfDiscourse.getLast(), 0.01);
    }

    @Test
    public void testFuzzySetRandom() {
        // Test random fuzzy set
        FuzzySet randomFuzzySet = new FuzzySet(List.of(0.2, 0.1, 0.0, 0.3, 0.5), new TrapezoidalMembershipFunction(0.0, 1.0, 1.0, 1.5));
        assertEquals(0.2, randomFuzzySet.getElements().get(0), 0.01);
        assertEquals(0.1, randomFuzzySet.getElements().get(1), 0.01);
        assertEquals(0.0, randomFuzzySet.getElements().get(2), 0.01);
        assertEquals(0.3, randomFuzzySet.getElements().get(3), 0.01);
        assertEquals(0.5, randomFuzzySet.getElements().get(4), 0.01);

        List<Double> support = randomFuzzySet.getSupport();
        assertEquals(0.2, support.get(0), 0.01);
        assertEquals(0.1, support.get(1), 0.01);
        assertEquals(0.3, support.get(2), 0.01);
        assertEquals(0.5, support.get(3), 0.01);

        FuzzySet poweredFuzzySet = randomFuzzySet.power(2);
        assertEquals(0.04, poweredFuzzySet.getElements().get(0), 0.01);
        assertEquals(0.01, poweredFuzzySet.getElements().get(1), 0.01);
        assertEquals(0.0, poweredFuzzySet.getElements().get(2), 0.01);
        assertEquals(0.09, poweredFuzzySet.getElements().get(3), 0.01);
        assertEquals(0.25, poweredFuzzySet.getElements().get(4), 0.01);
    }
}
