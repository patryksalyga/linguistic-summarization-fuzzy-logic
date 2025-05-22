package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SummaryQualityEvaluatorTest {
    private static SummaryQualityEvaluator summaryQualityEvaluator1;
    private static SummaryQualityEvaluator summaryQualityEvaluator2;
    private static LinguisticSummary linguisticSummary1;
    private static LinguisticSummary linguisticSummary2;
    private static FuzzySet fuzzySet1;
    private static FuzzySet fuzzySet2;
    private static Quantifier quantifier;

    @BeforeAll
    public static void setUp() {
        fuzzySet1 = new FuzzySet(List.of(0.1, 0.2, 0.3, 0.4, 0.5), new TriangularMembershipFunction(0.1, 0.3, 0.5));
        fuzzySet2 = new FuzzySet(List.of(0.1, 0.2, 0.3, 0.4, 0.5), new TrapezoidalMembershipFunction(0.3, 0.4, 0.5, 0.6));
        quantifier = new RelativeQuantifier("około połowa", new TriangularMembershipFunction(0.3, 0.5, 0.7));
        linguisticSummary1 = new LinguisticSummary(fuzzySet1, quantifier, "poparcie dla kadydata", 5);
        List<FuzzySet> fuzzySets = List.of(fuzzySet1, fuzzySet2);
        linguisticSummary2 = new LinguisticSummary(fuzzySets, quantifier, List.of("poparcie dla kadydata", "poparcie dla partii"), 5);
        summaryQualityEvaluator1 = new SummaryQualityEvaluator(linguisticSummary1);
        summaryQualityEvaluator2 = new SummaryQualityEvaluator(linguisticSummary2);
    }

    @Test
    public void testGetT1() {
        double t1 = summaryQualityEvaluator1.get("T1");
        assert t1 >= 0 && t1 <= 1 : "T1 should be between 0 and 1";
        assertEquals(0.5, t1, 0.01);

        t1 = summaryQualityEvaluator2.get("T1");
        assert t1 >= 0 && t1 <= 1 : "T1 should be between 0 and 1";
        assertEquals(0.0, t1, 0.01);
    }

    @Test
    public void testGetT2() {
        double t2 = summaryQualityEvaluator1.get("T2");
        assert t2 >= 0 && t2 <= 1 : "T2 should be between 0 and 1";
        assertEquals(1 - Math.pow(0.6, 0.5), t2, 0.01);

        t2 = summaryQualityEvaluator2.get("T2");
        assert t2 >= 0 && t2 <= 1 : "T2 should be between 0 and 1";
        assertEquals(1 - Math.pow(0.6 * 0.4 ,0.5), t2, 0.01);
    }

    @Test
    public void testGetT3() {
        double t3 = summaryQualityEvaluator1.get("T3");
        assert t3 >= 0 && t3 <= 1 : "T3 should be between 0 and 1";
        assertEquals(0.6, t3, 0.01);

        t3 = summaryQualityEvaluator2.get("T3");
        assert t3 >= 0 && t3 <= 1 : "T3 should be between 0 and 1";
        assertEquals(0.2, t3, 0.01);
    }

    @Test
    public void testGetT4() {
        double t4 = summaryQualityEvaluator1.get("T4");
        assert t4 >= 0 && t4 <= 1 : "T4 should be between 0 and 1";
        assertEquals(0.0, t4, 0.01);

        t4 = summaryQualityEvaluator2.get("T4");
        assert t4 >= 0 && t4 <= 1 : "T4 should be between 0 and 1";
        assertEquals(0.04, t4, 0.01);
    }

    @Test
    public void testGetT5() {
        double t5 = summaryQualityEvaluator1.get("T5");
        assert t5 >= 0 && t5 <= 1 : "T5 should be between 0 and 1";
        assertEquals(1, t5, 0.01);

        t5 = summaryQualityEvaluator2.get("T5");
        assert t5 >= 0 && t5 <= 1 : "T5 should be between 0 and 1";
        assertEquals(0.5, t5, 0.01);
    }

    @Test
    public void testGetT6() {
        double t6 = summaryQualityEvaluator1.get("T6");
        assert t6 >= 0 && t6 <= 1 : "T6 should be between 0 and 1";
        assertEquals(0.4, t6, 0.01);

        t6 = summaryQualityEvaluator2.get("T6");
        assert t6 >= 0 && t6 <= 1 : "T6 should be between 0 and 1";
        assertEquals(0.4, t6, 0.01);

        // Test for T6 with absolute quantifier
        AbsoluteQuantifier absoluteQuantifier = new AbsoluteQuantifier("mniej niż 3", new TrapezoidalMembershipFunction(0,0, 3, 4));
        FuzzySet fuzzySet3 = new FuzzySet(List.of(0.1, 0.2, 0.3, 0.4, 0.5), new TriangularMembershipFunction(0.1, 0.3, 0.5));
        LinguisticSummary linguisticSummary3 = new LinguisticSummary(fuzzySet3, absoluteQuantifier, "poparcie dla kadydata", 5);
        SummaryQualityEvaluator summaryQualityEvaluator3 = new SummaryQualityEvaluator(linguisticSummary3);
        t6 = summaryQualityEvaluator3.get("T6");
        assert t6 >= 0 && t6 <= 1 : "T6 should be between 0 and 1";
        assertEquals(0.8, t6, 0.01);
    }

    @Test
    public void testGetT7() {
        double t7 = summaryQualityEvaluator1.get("T7");
        assert t7 >= 0 && t7 <= 1 : "T7 should be between 0 and 1";
        assertEquals(0.2, t7, 0.01);

        t7 = summaryQualityEvaluator2.get("T7");
        assert t7 >= 0 && t7 <= 1 : "T7 should be between 0 and 1";
        assertEquals(0.2, t7, 0.01);

        // Test for T7 with absolute quantifier
        AbsoluteQuantifier absoluteQuantifier = new AbsoluteQuantifier("mniej niż 3", new TrapezoidalMembershipFunction(0,0, 3, 4));
        FuzzySet fuzzySet3 = new FuzzySet(List.of(0.1, 0.2, 0.3, 0.4, 0.5), new TriangularMembershipFunction(0.1, 0.3, 0.5));
        LinguisticSummary linguisticSummary3 = new LinguisticSummary(fuzzySet3, absoluteQuantifier, "poparcie dla kadydata", 5);
        SummaryQualityEvaluator summaryQualityEvaluator3 = new SummaryQualityEvaluator(linguisticSummary3);
        t7 = summaryQualityEvaluator3.get("T7");
        assert t7 >= 0 && t7 <= 1 : "T6 should be between 0 and 1";
        assertEquals(0.7, t7, 0.01);
    }

    @Test
    public void testGetT8() {
        double t8 = summaryQualityEvaluator1.get("T8");
        assert t8 >= 0 && t8 <= 1 : "T8 should be between 0 and 1";
        assertEquals(1 - Math.pow(0.4, 1), t8, 0.01);

        t8 = summaryQualityEvaluator2.get("T8");
        assert t8 >= 0 && t8 <= 1 : "T8 should be between 0 and 1";
        assertEquals(1 - Math.pow(0.4 * 0.4, 0.5), t8, 0.01);
    }

    @Test
    public void testGetT9() {
        double t9 = summaryQualityEvaluator1.get("T9");
        assert t9 >= 0 && t9 <= 1 : "T9 should be between 0 and 1";
        assertEquals(1, t9, 0.01);

        t9 = summaryQualityEvaluator2.get("T9");
        assert t9 >= 0 && t9 <= 1 : "T9 should be between 0 and 1";
        assertEquals(1, t9, 0.01);
    }

    @Test
    public void testGetT10() {
        double t10 = summaryQualityEvaluator1.get("T10");
        assert t10 >= 0 && t10 <= 1 : "T10 should be between 0 and 1";
        assertEquals(1, t10, 0.01);

        t10 = summaryQualityEvaluator2.get("T10");
        assert t10 >= 0 && t10 <= 1 : "T10 should be between 0 and 1";
        assertEquals(1, t10, 0.01);
    }

    @Test
    public void testGetT11() {
        double t11 = summaryQualityEvaluator1.get("T11");
        assert t11 >= 0 && t11 <= 1 : "T11 should be between 0 and 1";
        assertEquals(1, t11, 0.01);

        t11 = summaryQualityEvaluator2.get("T11");
        assert t11 >= 0 && t11 <= 1 : "T11 should be between 0 and 1";
        assertEquals(1, t11, 0.01);
    }
}
