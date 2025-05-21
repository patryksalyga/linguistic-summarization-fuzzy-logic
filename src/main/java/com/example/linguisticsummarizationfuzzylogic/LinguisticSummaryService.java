package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinguisticSummaryService {
    private ElectoralDistricts electoralDistricts;
    private LinguisticRepository linguisticRepository;
    private QuantifiersRepository quantifiersRepository;

    private List<LinguisticSummary> linguisticSummaries = new ArrayList<>();

    private List<AbsoluteQuantifier> absoluteQuantifiers = new ArrayList<>();
    private List<RelativeQuantifier> relativeQuantifiers = new ArrayList<>();
    private Map<String, FuzzySet> fuzzySetsMap = new HashMap<>();

    public LinguisticSummaryService(ElectoralDistricts electoralDistricts, LinguisticRepository linguisticRepository, QuantifiersRepository quantifiersRepository) {
        this.electoralDistricts = electoralDistricts;
        this.linguisticRepository = linguisticRepository;
        this.quantifiersRepository = quantifiersRepository;
    }

    public void prepareData() {
        clearData();
        for (AbsoluteQuantifier quantifier : quantifiersRepository.getAbsoluteQuantifiers()) {
            if (quantifier.isEnabled()) {
                absoluteQuantifiers.add(quantifier);
            }
        }

        for (RelativeQuantifier quantifier : quantifiersRepository.getRelativeQuantifiers()) {
            if (quantifier.isEnabled()) {
                relativeQuantifiers.add(quantifier);
            }
        }

        for (LinguisticVariable linguisticVariable : linguisticRepository.getLinguisticVariables()) {
            for (LinguisticTerm linguisticTerm : linguisticVariable.getTerms()) {
                if (linguisticTerm.isEnabled()) {
                    List<Double> values = new ArrayList<>();
                    switch (linguisticVariable.getName()) {
                        case "Stopień przygotowania komisji":
                            values = electoralDistricts.getCommissionPreparationLevels();
                            break;
                        case "Nadmiar kart":
                            values = electoralDistricts.getSurplusBallots();
                            break;
                        case "Frekwencja wyborcza":
                            values = electoralDistricts.getVoterTurnouts();
                            break;
                        case "Mobliność wyborcza":
                            values = electoralDistricts.getVoterMobilizations();
                            break;
                        case "Zgodność urny z wydaniami kart":
                            values = electoralDistricts.getBallotBoxConsistencies();
                            break;
                        case "Udział głosów korespondencyjnych":
                            values = electoralDistricts.getPostalVoteShares();
                            break;
                        case "Skala nieważnych kart":
                            values = electoralDistricts.getInvalidBallotsRates();
                            break;
                        case "Skuteczność głosowania":
                            values = electoralDistricts.getVotingEffectivenesses();
                            break;
                        case "Liczba głosujących przez pełnomocnika":
                            values = electoralDistricts.getProxyVotersCounts();
                            break;
                        case "Poparcie kandydata A":
                            values = electoralDistricts.getCandidateASupports();
                            break;
                        case "Poparcie kandydata B":
                            values = electoralDistricts.getCandidateBSupports();
                            break;
                    }

                    FuzzySet fuzzySet = new FuzzySet(values, linguisticTerm.getMembershipFunction());
                    fuzzySetsMap.put(linguisticTerm.getLabel() + " " + linguisticVariable.getName(), fuzzySet);
                }
            }
        }
    }

    public void generateZadeh() {
        for (String key : fuzzySetsMap.keySet()) {
            FuzzySet fuzzySet = fuzzySetsMap.get(key);
            for (AbsoluteQuantifier absoluteQuantifier : absoluteQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(fuzzySet, absoluteQuantifier, key);
                linguisticSummaries.add(linguisticSummary);

            }
            for (RelativeQuantifier relativeQuantifier : relativeQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(fuzzySet, relativeQuantifier, key);
                linguisticSummaries.add(linguisticSummary);
            }

        }
    }

    public void generateYager() {
        List<String> keys = new ArrayList<>(fuzzySetsMap.keySet());

        for (int r = 2; r <= keys.size(); r++) {
            generateYagerCombinations(keys, r, 0, new ArrayList<>(), fuzzySetsMap);
        }
    }

    private void generateYagerCombinations(List<String> input, int r, int start,
                                      List<String> currentKeys, Map<String, FuzzySet> fuzzySetsMap) {
        if (currentKeys.size() == r) {
            List<FuzzySet> correspondingSets = new ArrayList<>();
            for (String key : currentKeys) {
                correspondingSets.add(fuzzySetsMap.get(key));
            }

            for (AbsoluteQuantifier absoluteQuantifier : absoluteQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(correspondingSets, absoluteQuantifier, currentKeys);
                linguisticSummaries.add(linguisticSummary);

            }
            for (RelativeQuantifier relativeQuantifier : relativeQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(correspondingSets, relativeQuantifier, currentKeys);
                linguisticSummaries.add(linguisticSummary);
            }

            return;
        }

        for (int i = start; i < input.size(); i++) {
            currentKeys.add(input.get(i));
            generateYagerCombinations(input, r, i + 1, currentKeys, fuzzySetsMap);
            currentKeys.remove(currentKeys.size() - 1); // backtrack
        }
    }

    public List<LinguisticSummary> getLinguisticSummaries() {
        return linguisticSummaries;
    }

    public void clearData() {
        absoluteQuantifiers.clear();
        relativeQuantifiers.clear();
        fuzzySetsMap.clear();
        linguisticSummaries.clear();
    }
}
