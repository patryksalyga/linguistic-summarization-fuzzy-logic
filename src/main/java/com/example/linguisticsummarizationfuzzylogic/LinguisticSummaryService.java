package com.example.linguisticsummarizationfuzzylogic;

import java.util.*;
import java.util.stream.Collectors;

public class LinguisticSummaryService {
    private ElectoralDistricts electoralDistricts;
    private LinguisticRepository linguisticRepository;
    private QuantifiersRepository quantifiersRepository;
    private EntityRepository entityRepository;

    private List<LinguisticSummary> linguisticSummaries = new ArrayList<>();

    private List<AbsoluteQuantifier> absoluteQuantifiers = new ArrayList<>();
    private List<RelativeQuantifier> relativeQuantifiers = new ArrayList<>();
    private Map<String, FuzzySet> fuzzySetsMap = new HashMap<>();
    private List<EntityValue> entityValues = new ArrayList<>();

    private final int electoralDistrictsCount;

    public LinguisticSummaryService(ElectoralDistricts electoralDistricts, LinguisticRepository linguisticRepository, QuantifiersRepository quantifiersRepository, EntityRepository entityRepository) {
        this.electoralDistricts = electoralDistricts;
        this.linguisticRepository = linguisticRepository;
        this.quantifiersRepository = quantifiersRepository;
        this.electoralDistrictsCount = electoralDistricts.getDistricts().size();
        this.entityRepository = entityRepository;
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

        for (EntityValue entityValue : entityRepository.getEntities().get(0).getValues()) {
            if (entityValue.isEnabled()) {
                entityValues.add(entityValue);
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
                        case "Poparcie dla kandydata A":
                            values = electoralDistricts.getCandidateASupports();
                            break;
                        case "Poparcie dla kandydata B":
                            values = electoralDistricts.getCandidateBSupports();
                            break;
                    }

                    FuzzySet fuzzySet = new FuzzySet(values, linguisticTerm.getMembershipFunction(), electoralDistricts.getDistricts(), linguisticVariable.getName());
                    fuzzySetsMap.put(linguisticTerm.getLabel() + " " + linguisticVariable.getName(), fuzzySet);
                }
            }
        }
    }

    public void generateZadeh() {
        for (String key : fuzzySetsMap.keySet()) {
            FuzzySet fuzzySet = fuzzySetsMap.get(key);
            for (AbsoluteQuantifier absoluteQuantifier : absoluteQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(fuzzySet, absoluteQuantifier, key, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);
                linguisticSummary = new LinguisticSummary(fuzzySet.power(2), absoluteQuantifier, "bardzo " + key, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);
                linguisticSummary = new LinguisticSummary(fuzzySet.power(0.5), absoluteQuantifier, "mniej więcej " + key, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);

            }
            for (RelativeQuantifier relativeQuantifier : relativeQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(fuzzySet, relativeQuantifier, key, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);
                linguisticSummary = new LinguisticSummary(fuzzySet.power(2), relativeQuantifier, "bardzo " + key, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);
                linguisticSummary = new LinguisticSummary(fuzzySet.power(0.5), relativeQuantifier, "mniej więcej " + key, electoralDistrictsCount);
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

    public void generateKacprzyk() {
        List<String> qualifierKeys = new ArrayList<>();
        List<String> summarizerKeys = new ArrayList<>();

        for (EntityValue entityValue : entityValues) {
            qualifierKeys.add("[E]"+entityValue.getValue());
        }

        for (String key : fuzzySetsMap.keySet()) {
                qualifierKeys.add(key);
                summarizerKeys.add(key);
        }

        for (RelativeQuantifier quantifier : relativeQuantifiers) {
            for (int qSize = 1; qSize < qualifierKeys.size(); qSize++) {
                generateKacprzykQualifierCombinations(qualifierKeys, qSize, 0, new ArrayList<>(), summarizerKeys, quantifier);
            }
        }
    }

    private void generateKacprzykQualifierCombinations(List<String> qualifierKeys, int qSize, int start,
                                                       List<String> currentQualifier, List<String> summarizerPool,
                                                       RelativeQuantifier quantifier) {
        if (currentQualifier.size() == qSize) {
            Set<String> qualifierSet = new HashSet<>(currentQualifier);
            List<String> remainingSummarizers = summarizerPool.stream()
                    .filter(key -> !qualifierSet.contains(key))
                    .collect(Collectors.toList());

            for (int sSize = 1; sSize <= remainingSummarizers.size(); sSize++) {
                generateKacprzykSummarizerCombinations(remainingSummarizers, sSize, 0, new ArrayList<>(), currentQualifier, quantifier);
            }
            return;
        }

        for (int i = start; i < qualifierKeys.size(); i++) {
            currentQualifier.add(qualifierKeys.get(i));
            generateKacprzykQualifierCombinations(qualifierKeys, qSize, i + 1, currentQualifier, summarizerPool, quantifier);
            currentQualifier.remove(currentQualifier.size() - 1); // backtrack
        }
    }

    private void generateKacprzykSummarizerCombinations(List<String> summarizerKeys, int sSize, int start,
                                                        List<String> currentSummarizer, List<String> qualifier,
                                                        RelativeQuantifier quantifier) {
        if (currentSummarizer.size() == sSize) {
//            System.out.println("Dla kwantyfikatora: \"" + quantifier.getLabel() + "\"");
//            System.out.println("  Kwalifikator: " + String.join(" AND ", qualifier));
//            System.out.println("  Sumaryzator: " + String.join(" AND ", currentSummarizer));
//            System.out.println("-----------------------------------------------------");
            List<EntityValue> qualifierEntities = new ArrayList<>();
            List<FuzzySet> qualifierFuzzySets = new ArrayList<>();
            List<String> qualifierSummary = new ArrayList<>();
            for (String qualifierKey : qualifier) {
                if (qualifierKey.substring(0, 3).equals("[E]")) {
                    Optional<EntityValue> maybeEntity = entityValues.stream()
                            .filter(ev -> ev.getValue().equals(qualifierKey.substring(3)))
                            .findFirst();

                    maybeEntity.ifPresent(qualifierEntities::add);
                    //qualifierSummary.add(qualifierKey.substring(3));
                }
                else {
                    qualifierFuzzySets.add(fuzzySetsMap.get(qualifierKey));
                    qualifierSummary.add(qualifierKey);
                }
            }
            List<FuzzySet> summarizerFuzzySets = new ArrayList<>();

            ElectoralDistricts electoralDistricts = new ElectoralDistricts();
            if (qualifierEntities.isEmpty()) {
                electoralDistricts.addDistricts(this.electoralDistricts.getDistricts());
                for (FuzzySet fuzzySet : qualifierFuzzySets) {
                    electoralDistricts.deleteDistricts(fuzzySet.getElectoralDistrictsList());
                }

            } else {
                for (EntityValue entityValue : qualifierEntities) {
                    electoralDistricts.addDistricts(entityValue.getElectoralDistricts());
                }
                for (FuzzySet fuzzySet : qualifierFuzzySets) {
                    electoralDistricts.deleteDistricts(fuzzySet.getElectoralDistrictsList());
                }

            }
                for (String summarizerKey : currentSummarizer) {
                    List<Double> values = new ArrayList<>();
                    switch (fuzzySetsMap.get(summarizerKey).getName()) {
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
                        case "Poparcie dla kandydata A":
                            values = electoralDistricts.getCandidateASupports();
                            break;
                        case "Poparcie dla kandydata B":
                            values = electoralDistricts.getCandidateBSupports();
                            break;
                    }
                    FuzzySet fuzzySet = new FuzzySet(values, fuzzySetsMap.get(summarizerKey).getMembershipFunction(), electoralDistricts.getDistricts(), fuzzySetsMap.get(summarizerKey).getName());
                    summarizerFuzzySets.add(fuzzySet);
                }
                LinguisticSummary linguisticSummary = new LinguisticSummary(summarizerFuzzySets, quantifier, qualifierEntities, qualifierFuzzySets, qualifierSummary, currentSummarizer, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);
                return;
            }

        for (int i = start; i < summarizerKeys.size(); i++) {
            currentSummarizer.add(summarizerKeys.get(i));
            generateKacprzykSummarizerCombinations(summarizerKeys, sSize, i + 1, currentSummarizer, qualifier, quantifier);
            currentSummarizer.remove(currentSummarizer.size() - 1); // backtrack
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
                LinguisticSummary linguisticSummary = new LinguisticSummary(correspondingSets, absoluteQuantifier, currentKeys, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);

            }
            for (RelativeQuantifier relativeQuantifier : relativeQuantifiers) {
                LinguisticSummary linguisticSummary = new LinguisticSummary(correspondingSets, relativeQuantifier, currentKeys, electoralDistrictsCount);
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
