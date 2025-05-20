package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelDataControllerTest {

    private static ElectoralDistricts electoralDistricts;
    private static ExcelDataController excelDataController;

    @BeforeAll
    public static void setUp() {
        electoralDistricts = new ElectoralDistricts();
        excelDataController = new ExcelDataController(electoralDistricts);
        excelDataController.loadDataFromExcel("src/main/resources/com/example/linguisticsummarizationfuzzylogic/wybory2020.xlsx");
    }

    @Test
    public void testLoadDataSize() {
        assertEquals(27225, electoralDistricts.getDistricts().size());
    }

    @Test
    public void testLoadDataFirstDistrict() {
        ElectoralDistrict firstDistrict = electoralDistricts.getDistricts().get(0);
        assertEquals("miasto", firstDistrict.getAreaType());
        assertEquals("dolnośląskie", firstDistrict.getVoivodeship());
        assertEquals(0.922086285, firstDistrict.getCommissionPreparationLevel(), 0.0001);
        assertEquals(0.31773743, firstDistrict.getSurplusBallots(), 0.0001);
        assertEquals(0.629104958, firstDistrict.getVoterTurnout(), 0.0001);
        assertEquals(0.004094166, firstDistrict.getVoterMobilization(), 0.0001);
        assertEquals(1.006141249, firstDistrict.getBallotBoxConsistency(), 0.0001);
        assertEquals(0.006103764, firstDistrict.getPostalVoteShare(), 0.0001);
        assertEquals(0.0, firstDistrict.getInvalidBallotsRate(), 0.0001);
        assertEquals(1.0, firstDistrict.getVotingEffectiveness(), 0.0001);
        assertEquals(0, firstDistrict.getProxyVotersCount());
        assertEquals(0.373727088, firstDistrict.getCandidateASupport(), 0.0001);
        assertEquals(0.344195519, firstDistrict.getCandidateBSupport(), 0.0001);
    }

    @Test
    public void testLoadDataLastDistrict() {
        ElectoralDistrict lastDistrict = electoralDistricts.getDistricts().get(electoralDistricts.getDistricts().size() - 1);
        assertEquals("miasto", lastDistrict.getAreaType());
        assertEquals("zachodniopomorskie", lastDistrict.getVoivodeship());
        assertEquals(1.103448276, lastDistrict.getCommissionPreparationLevel(), 0.0001);
        assertEquals(1.0, lastDistrict.getSurplusBallots(), 0.0001);
        assertEquals(0.0, lastDistrict.getVoterTurnout(), 0.0001);
        assertEquals(0.0, lastDistrict.getVoterMobilization(), 0.0001);
        assertEquals(0.0, lastDistrict.getBallotBoxConsistency(), 0.0001);
        assertEquals(0.0, lastDistrict.getPostalVoteShare(), 0.0001);
        assertEquals(0.0, lastDistrict.getInvalidBallotsRate(), 0.0001);
        assertEquals(0.0, lastDistrict.getVotingEffectiveness(), 0.0001);
        assertEquals(0, lastDistrict.getProxyVotersCount());
        assertEquals(0.0, lastDistrict.getCandidateASupport(), 0.0001);
        assertEquals(0.0, lastDistrict.getCandidateBSupport(), 0.0001);
    }
}
