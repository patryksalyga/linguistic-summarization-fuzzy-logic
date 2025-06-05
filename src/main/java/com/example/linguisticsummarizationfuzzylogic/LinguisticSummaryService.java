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

        for (EntityValue entityValue : entityRepository.getEntities().get(1).getValues()) {
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
        System.out.println("Generating Zadeh");
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
        System.out.println("Generating Yager");
        List<String> keys = new ArrayList<>(fuzzySetsMap.keySet());

        for (int r = 2; r <= keys.size(); r++) {
            generateYagerCombinations(keys, r, 0, new ArrayList<>(), fuzzySetsMap);
        }
    }

    public void generateKacprzyk() {
        System.out.println("Generating Kacprzyk");
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
            System.out.println(electoralDistricts.getDistricts().size());
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

    public void generateComparativeSummaries() {
        /// ///////////////////////////////////////////////////////////////////////////////////////
        System.out.println("Generating Comparative Summaries");
        List<String> keys = new ArrayList<>(fuzzySetsMap.keySet());

        ElectoralDistricts electoralDistricts1 = new ElectoralDistricts();
        ElectoralDistricts electoralDistricts2 = new ElectoralDistricts();

        electoralDistricts1.addDistricts(entityValues.get(0).getElectoralDistricts());
        electoralDistricts2.addDistricts(entityValues.get(1).getElectoralDistricts());

        System.out.println("Electoral Districts 1: " + electoralDistricts1.getDistricts().size());
        System.out.println("Electoral Districts 2: " + electoralDistricts2.getDistricts().size());

        for (String key : keys) {
            FuzzySet fuzzySet = fuzzySetsMap.get(key);

            List<Double> values1 = new ArrayList<>();
            List<Double> values2 = new ArrayList<>();

            switch (fuzzySetsMap.get(key).getName()) {
                case "Stopień przygotowania komisji":
                    values1 = electoralDistricts1.getCommissionPreparationLevels();
                    values2 = electoralDistricts2.getCommissionPreparationLevels();
                    break;
                case "Nadmiar kart":
                    values1 = electoralDistricts1.getSurplusBallots();
                    values2 = electoralDistricts2.getSurplusBallots();
                    break;
                case "Frekwencja wyborcza":
                    values1 = electoralDistricts1.getVoterTurnouts();
                    values2 = electoralDistricts2.getVoterTurnouts();
                    break;
                case "Mobliność wyborcza":
                    values1 = electoralDistricts1.getVoterMobilizations();
                    values2 = electoralDistricts2.getVoterMobilizations();
                    break;
                case "Zgodność urny z wydaniami kart":
                    values1 = electoralDistricts1.getBallotBoxConsistencies();
                    values2 = electoralDistricts2.getBallotBoxConsistencies();
                    break;
                case "Udział głosów korespondencyjnych":
                    values1 = electoralDistricts1.getPostalVoteShares();
                    values2 = electoralDistricts2.getPostalVoteShares();
                    break;
                case "Skala nieważnych kart":
                    values1 = electoralDistricts1.getInvalidBallotsRates();
                    values2 = electoralDistricts2.getInvalidBallotsRates();
                    break;
                case "Skuteczność głosowania":
                    values1 = electoralDistricts1.getVotingEffectivenesses();
                    values2 = electoralDistricts2.getVotingEffectivenesses();
                    break;
                case "Liczba głosujących przez pełnomocnika":
                    values1 = electoralDistricts1.getProxyVotersCounts();
                    values2 = electoralDistricts2.getProxyVotersCounts();
                    break;
                case "Poparcie dla kandydata A":
                    values1 = electoralDistricts1.getCandidateASupports();
                    values2 = electoralDistricts2.getCandidateASupports();
                    break;
                case "Poparcie dla kandydata B":
                    values1 = electoralDistricts1.getCandidateBSupports();
                    values2 = electoralDistricts2.getCandidateBSupports();
                    break;
            }
            System.out.println("Values1 size: " + values1.size() + ", Values2 size: " + values2.size());

            FuzzySet fuzzySet1 = new FuzzySet(values1, fuzzySet.getMembershipFunction(), electoralDistricts1.getDistricts(), fuzzySet.getName());
            FuzzySet fuzzySet2 = new FuzzySet(values2, fuzzySet.getMembershipFunction(), electoralDistricts2.getDistricts(), fuzzySet.getName());

            for (RelativeQuantifier relativeQuantifier : relativeQuantifiers) {
                String text = relativeQuantifier.label + " obwódów wyborczych z " + entityValues.get(0).getValue() + " w porównaniu do obwodów z " + entityValues.get(1).getValue() + " ma " + key;
                LinguisticSummary linguisticSummary = new LinguisticSummary(fuzzySet1, fuzzySet2, relativeQuantifier, text, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);

                text = relativeQuantifier.label + " obwódów wyborczych z " + entityValues.get(1).getValue() + " w porównaniu do obwodów z " + entityValues.get(0).getValue() + " ma " + key;
                linguisticSummary = new LinguisticSummary(fuzzySet2, fuzzySet1, relativeQuantifier, text, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);

                
//                System.out.println("4. " + "Więcej obwodów wyborczych z " + entityValues.get(0).getValue() + " niż obwodów z " + entityValues.get(1).getValue() + " ma " + key);
//                System.out.println("4. " + "Więcej obwodów wyborczych z " + entityValues.get(1).getValue() + " niż obwodów z " + entityValues.get(0).getValue() + " ma " + key);
            }
        }

        for (int r = 2; r <= keys.size(); r++) {
            generateComparativeCombinations(keys, r, 0, new ArrayList<>(), fuzzySetsMap);
        }
        /// /////////////////////////////////////////////////////////////////////////////////////////////////
        List<String> qualifierKeys = new ArrayList<>();
        List<String> summarizerKeys = new ArrayList<>();

        for (String key : fuzzySetsMap.keySet()) {
            qualifierKeys.add(key);
            summarizerKeys.add(key);
        }

        for (RelativeQuantifier quantifier : relativeQuantifiers) {
            for (int qSize = 1; qSize < qualifierKeys.size(); qSize++) {
                generateComparativeQualifierCombinations(qualifierKeys, qSize, 0, new ArrayList<>(), summarizerKeys, quantifier);
            }
        }
    }

    private void generateComparativeCombinations(List<String> input, int r, int start,
                                           List<String> currentKeys, Map<String, FuzzySet> fuzzySetsMap) {

        ElectoralDistricts electoralDistricts1 = new ElectoralDistricts();
        ElectoralDistricts electoralDistricts2 = new ElectoralDistricts();

        electoralDistricts1.addDistricts(entityValues.get(0).getElectoralDistricts());
        electoralDistricts2.addDistricts(entityValues.get(1).getElectoralDistricts());

        if (currentKeys.size() == r) {
            List<FuzzySet> correspondingSets1 = new ArrayList<>();
            List<FuzzySet> correspondingSets2 = new ArrayList<>();

            for (String key : currentKeys) {
                FuzzySet fuzzySet = fuzzySetsMap.get(key);

                List<Double> values1 = new ArrayList<>();
                List<Double> values2 = new ArrayList<>();

                switch (fuzzySetsMap.get(key).getName()) {
                    case "Stopień przygotowania komisji":
                        values1 = electoralDistricts1.getCommissionPreparationLevels();
                        values2 = electoralDistricts2.getCommissionPreparationLevels();
                        break;
                    case "Nadmiar kart":
                        values1 = electoralDistricts1.getSurplusBallots();
                        values2 = electoralDistricts2.getSurplusBallots();
                        break;
                    case "Frekwencja wyborcza":
                        values1 = electoralDistricts1.getVoterTurnouts();
                        values2 = electoralDistricts2.getVoterTurnouts();
                        break;
                    case "Mobliność wyborcza":
                        values1 = electoralDistricts1.getVoterMobilizations();
                        values2 = electoralDistricts2.getVoterMobilizations();
                        break;
                    case "Zgodność urny z wydaniami kart":
                        values1 = electoralDistricts1.getBallotBoxConsistencies();
                        values2 = electoralDistricts2.getBallotBoxConsistencies();
                        break;
                    case "Udział głosów korespondencyjnych":
                        values1 = electoralDistricts1.getPostalVoteShares();
                        values2 = electoralDistricts2.getPostalVoteShares();
                        break;
                    case "Skala nieważnych kart":
                        values1 = electoralDistricts1.getInvalidBallotsRates();
                        values2 = electoralDistricts2.getInvalidBallotsRates();
                        break;
                    case "Skuteczność głosowania":
                        values1 = electoralDistricts1.getVotingEffectivenesses();
                        values2 = electoralDistricts2.getVotingEffectivenesses();
                        break;
                    case "Liczba głosujących przez pełnomocnika":
                        values1 = electoralDistricts1.getProxyVotersCounts();
                        values2 = electoralDistricts2.getProxyVotersCounts();
                        break;
                    case "Poparcie dla kandydata A":
                        values1 = electoralDistricts1.getCandidateASupports();
                        values2 = electoralDistricts2.getCandidateASupports();
                        break;
                    case "Poparcie dla kandydata B":
                        values1 = electoralDistricts1.getCandidateBSupports();
                        values2 = electoralDistricts2.getCandidateBSupports();
                        break;
                }

                FuzzySet fuzzySet1 = new FuzzySet(values1, fuzzySet.getMembershipFunction(), electoralDistricts1.getDistricts(), fuzzySet.getName());
                FuzzySet fuzzySet2 = new FuzzySet(values2, fuzzySet.getMembershipFunction(), electoralDistricts2.getDistricts(), fuzzySet.getName());

                correspondingSets1.add(fuzzySet1);
                correspondingSets2.add(fuzzySet2);
            }

            for (RelativeQuantifier relativeQuantifier : relativeQuantifiers) {
                String text = relativeQuantifier.label + " obwódów wyborczych z " + entityValues.get(0).getValue() + " w porównaniu do obwodów z " + entityValues.get(1).getValue() + " ma " + String.join(" i ", currentKeys);
                LinguisticSummary linguisticSummary = new LinguisticSummary(correspondingSets1, correspondingSets2, relativeQuantifier, text, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);

                text = relativeQuantifier.label + " obwódów wyborczych z " + entityValues.get(1).getValue() + " w porównaniu do obwodów z " + entityValues.get(0).getValue() + " ma " + String.join(" i ", currentKeys);
                linguisticSummary = new LinguisticSummary(correspondingSets2, correspondingSets1, relativeQuantifier, text, electoralDistrictsCount);
                linguisticSummaries.add(linguisticSummary);


//                System.out.println("4. " + "Więcej obwodów wyborczych z " + entityValues.get(0).getValue() + " niż obwodów z " + entityValues.get(1).getValue() + " ma " + String.join(" i ", currentKeys));
//                System.out.println("4. " + "Więcej obwodów wyborczych z " + entityValues.get(1).getValue() + " niż obwodów z " + entityValues.get(0).getValue() + " ma " + String.join(" i ", currentKeys));
            }

            return;
        }

        for (int i = start; i < input.size(); i++) {
            currentKeys.add(input.get(i));
            generateComparativeCombinations(input, r, i + 1, currentKeys, fuzzySetsMap);
            currentKeys.remove(currentKeys.size() - 1); // backtrack
        }
    }

    private void generateComparativeQualifierCombinations(List<String> qualifierKeys, int qSize, int start,
                                                       List<String> currentQualifier, List<String> summarizerPool,
                                                       RelativeQuantifier quantifier) {
        if (currentQualifier.size() == qSize) {
            Set<String> qualifierSet = new HashSet<>(currentQualifier);
            List<String> remainingSummarizers = summarizerPool.stream()
                    .filter(key -> !qualifierSet.contains(key))
                    .collect(Collectors.toList());

            for (int sSize = 1; sSize <= remainingSummarizers.size(); sSize++) {
                generateComparativeSummarizerCombinations(remainingSummarizers, sSize, 0, new ArrayList<>(), currentQualifier, quantifier);
            }
            return;
        }

        for (int i = start; i < qualifierKeys.size(); i++) {
            currentQualifier.add(qualifierKeys.get(i));
            generateComparativeQualifierCombinations(qualifierKeys, qSize, i + 1, currentQualifier, summarizerPool, quantifier);
            currentQualifier.remove(currentQualifier.size() - 1); // backtrack
        }
    }

    private void generateComparativeSummarizerCombinations(List<String> summarizerKeys, int sSize, int start,
                                                        List<String> currentSummarizer, List<String> qualifier,
                                                        RelativeQuantifier quantifier) {
        if (currentSummarizer.size() == sSize) {
            List<FuzzySet> qualifierFuzzySets = new ArrayList<>();
            List<String> qualifierSummary = new ArrayList<>();

            for (String qualifierKey : qualifier) {
                qualifierFuzzySets.add(fuzzySetsMap.get(qualifierKey));
                qualifierSummary.add(qualifierKey);
            }

            List<FuzzySet> correspondingSets1 = new ArrayList<>();
            List<FuzzySet> correspondingSets2 = new ArrayList<>();

            ElectoralDistricts electoralDistricts1 = new ElectoralDistricts();
            ElectoralDistricts electoralDistricts2 = new ElectoralDistricts();

            electoralDistricts1.addDistricts(entityValues.get(0).getElectoralDistricts());
            electoralDistricts2.addDistricts(entityValues.get(1).getElectoralDistricts());


            for (FuzzySet fuzzySet : qualifierFuzzySets) {
                electoralDistricts2.deleteDistricts(fuzzySet.getElectoralDistrictsList());
            }

//            System.out.println(electoralDistricts1.getDistricts().size());
//            System.out.println(electoralDistricts2.getDistricts().size());

            for (String summarizerKey : currentSummarizer) {
                List<Double> values1 = new ArrayList<>();
                List<Double> values2 = new ArrayList<>();
                switch (fuzzySetsMap.get(summarizerKey).getName()) {
                    case "Stopień przygotowania komisji":
                        values1 = electoralDistricts1.getCommissionPreparationLevels();
                        values2 = electoralDistricts2.getCommissionPreparationLevels();
                        break;
                    case "Nadmiar kart":
                        values1 = electoralDistricts1.getSurplusBallots();
                        values2 = electoralDistricts2.getSurplusBallots();
                        break;
                    case "Frekwencja wyborcza":
                        values1 = electoralDistricts1.getVoterTurnouts();
                        values2 = electoralDistricts2.getVoterTurnouts();
                        break;
                    case "Mobliność wyborcza":
                        values1 = electoralDistricts1.getVoterMobilizations();
                        values2 = electoralDistricts2.getVoterMobilizations();
                        break;
                    case "Zgodność urny z wydaniami kart":
                        values1 = electoralDistricts1.getBallotBoxConsistencies();
                        values2 = electoralDistricts2.getBallotBoxConsistencies();
                        break;
                    case "Udział głosów korespondencyjnych":
                        values1 = electoralDistricts1.getPostalVoteShares();
                        values2 = electoralDistricts2.getPostalVoteShares();
                        break;
                    case "Skala nieważnych kart":
                        values1 = electoralDistricts1.getInvalidBallotsRates();
                        values2 = electoralDistricts2.getInvalidBallotsRates();
                        break;
                    case "Skuteczność głosowania":
                        values1 = electoralDistricts1.getVotingEffectivenesses();
                        values2 = electoralDistricts2.getVotingEffectivenesses();
                        break;
                    case "Liczba głosujących przez pełnomocnika":
                        values1 = electoralDistricts1.getProxyVotersCounts();
                        values2 = electoralDistricts2.getProxyVotersCounts();
                        break;
                    case "Poparcie dla kandydata A":
                        values1 = electoralDistricts1.getCandidateASupports();
                        values2 = electoralDistricts2.getCandidateASupports();
                        break;
                    case "Poparcie dla kandydata B":
                        values1 = electoralDistricts1.getCandidateBSupports();
                        values2 = electoralDistricts2.getCandidateBSupports();
                        break;
                }
                FuzzySet fuzzySet1 = new FuzzySet(values1, fuzzySetsMap.get(summarizerKey).getMembershipFunction(), electoralDistricts1.getDistricts(), fuzzySetsMap.get(summarizerKey).getName());
                FuzzySet fuzzySet2 = new FuzzySet(values2, fuzzySetsMap.get(summarizerKey).getMembershipFunction(), electoralDistricts2.getDistricts(), fuzzySetsMap.get(summarizerKey).getName());
                correspondingSets1.add(fuzzySet1);
                correspondingSets2.add(fuzzySet2);
            }
            String text = quantifier.label + " obwódów wyborczych z " + entityValues.get(0).getValue() + " w porównaniu do obwodów z " + entityValues.get(1).getValue() + ", które mają " + String.join(" i ", qualifierSummary) + " ma " + String.join(" i ", currentSummarizer);
            LinguisticSummary linguisticSummary = new LinguisticSummary(correspondingSets1, correspondingSets2, quantifier, text, electoralDistrictsCount);
            linguisticSummaries.add(linguisticSummary);
            text = quantifier.label + " obwódów wyborczych z " + entityValues.get(1).getValue() +  ", które mają " + String.join(" i ", qualifierSummary) + " w porównaniu do obwodów z " + entityValues.get(0).getValue()  + " ma " + String.join(" i ", currentSummarizer);
            linguisticSummary = new LinguisticSummary(correspondingSets2, correspondingSets1, quantifier, text, electoralDistrictsCount);
            linguisticSummaries.add(linguisticSummary);

            correspondingSets1.clear();
            correspondingSets2.clear();

            electoralDistricts1 = new ElectoralDistricts();
            electoralDistricts2 = new ElectoralDistricts();

            electoralDistricts1.addDistricts(entityValues.get(0).getElectoralDistricts());
            electoralDistricts2.addDistricts(entityValues.get(1).getElectoralDistricts());


            for (FuzzySet fuzzySet : qualifierFuzzySets) {
                electoralDistricts1.deleteDistricts(fuzzySet.getElectoralDistrictsList());
            }

//            System.out.println(electoralDistricts1.getDistricts().size());
//            System.out.println(electoralDistricts2.getDistricts().size());

            for (String summarizerKey : currentSummarizer) {
                List<Double> values1 = new ArrayList<>();
                List<Double> values2 = new ArrayList<>();
                switch (fuzzySetsMap.get(summarizerKey).getName()) {
                    case "Stopień przygotowania komisji":
                        values1 = electoralDistricts1.getCommissionPreparationLevels();
                        values2 = electoralDistricts2.getCommissionPreparationLevels();
                        break;
                    case "Nadmiar kart":
                        values1 = electoralDistricts1.getSurplusBallots();
                        values2 = electoralDistricts2.getSurplusBallots();
                        break;
                    case "Frekwencja wyborcza":
                        values1 = electoralDistricts1.getVoterTurnouts();
                        values2 = electoralDistricts2.getVoterTurnouts();
                        break;
                    case "Mobliność wyborcza":
                        values1 = electoralDistricts1.getVoterMobilizations();
                        values2 = electoralDistricts2.getVoterMobilizations();
                        break;
                    case "Zgodność urny z wydaniami kart":
                        values1 = electoralDistricts1.getBallotBoxConsistencies();
                        values2 = electoralDistricts2.getBallotBoxConsistencies();
                        break;
                    case "Udział głosów korespondencyjnych":
                        values1 = electoralDistricts1.getPostalVoteShares();
                        values2 = electoralDistricts2.getPostalVoteShares();
                        break;
                    case "Skala nieważnych kart":
                        values1 = electoralDistricts1.getInvalidBallotsRates();
                        values2 = electoralDistricts2.getInvalidBallotsRates();
                        break;
                    case "Skuteczność głosowania":
                        values1 = electoralDistricts1.getVotingEffectivenesses();
                        values2 = electoralDistricts2.getVotingEffectivenesses();
                        break;
                    case "Liczba głosujących przez pełnomocnika":
                        values1 = electoralDistricts1.getProxyVotersCounts();
                        values2 = electoralDistricts2.getProxyVotersCounts();
                        break;
                    case "Poparcie dla kandydata A":
                        values1 = electoralDistricts1.getCandidateASupports();
                        values2 = electoralDistricts2.getCandidateASupports();
                        break;
                    case "Poparcie dla kandydata B":
                        values1 = electoralDistricts1.getCandidateBSupports();
                        values2 = electoralDistricts2.getCandidateBSupports();
                        break;
                }
                FuzzySet fuzzySet1 = new FuzzySet(values1, fuzzySetsMap.get(summarizerKey).getMembershipFunction(), electoralDistricts1.getDistricts(), fuzzySetsMap.get(summarizerKey).getName());
                FuzzySet fuzzySet2 = new FuzzySet(values2, fuzzySetsMap.get(summarizerKey).getMembershipFunction(), electoralDistricts2.getDistricts(), fuzzySetsMap.get(summarizerKey).getName());
                correspondingSets1.add(fuzzySet1);
                correspondingSets2.add(fuzzySet2);
            }
            text = quantifier.label + " obwódów wyborczych z " + entityValues.get(1).getValue() + " w porównaniu do obwodów z " + entityValues.get(0).getValue() + ", które mają " + String.join(" i ", qualifierSummary) + " ma " + String.join(" i ", currentSummarizer);
            linguisticSummary = new LinguisticSummary(correspondingSets1, correspondingSets2, quantifier, text, electoralDistrictsCount);
            linguisticSummaries.add(linguisticSummary);
            text = quantifier.label + " obwódów wyborczych z " + entityValues.get(0).getValue() +  ", które mają " + String.join(" i ", qualifierSummary) + " w porównaniu do obwodów z " + entityValues.get(1).getValue()  + " ma " + String.join(" i ", currentSummarizer);
            linguisticSummary = new LinguisticSummary(correspondingSets2, correspondingSets1, quantifier, text, electoralDistrictsCount);
            linguisticSummaries.add(linguisticSummary);
            return;
        }

        for (int i = start; i < summarizerKeys.size(); i++) {
            currentSummarizer.add(summarizerKeys.get(i));
            generateComparativeSummarizerCombinations(summarizerKeys, sSize, i + 1, currentSummarizer, qualifier, quantifier);
            currentSummarizer.remove(currentSummarizer.size() - 1); // backtrack
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
