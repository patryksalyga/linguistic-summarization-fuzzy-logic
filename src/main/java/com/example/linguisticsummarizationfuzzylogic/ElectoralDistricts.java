package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.List;

public class ElectoralDistricts {
    private List<ElectoralDistrict> districts;

    public ElectoralDistricts() {
        this.districts = new ArrayList<>();
    }

    public void addDistrict(ElectoralDistrict district) {
        this.districts.add(district);
    }

    public List<ElectoralDistrict> getDistricts() {
        return districts;
    }

    public List<Double> getCommissionPreparationLevels() {
        List<Double> levels = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            levels.add(district.getCommissionPreparationLevel());
        }
        return levels;
    }

    public List<Double> getSurplusBallots() {
        List<Double> surplusBallots = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            surplusBallots.add(district.getSurplusBallots());
        }
        return surplusBallots;
    }

    public List<Double> getVoterTurnouts() {
        List<Double> voterTurnouts = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            voterTurnouts.add(district.getVoterTurnout());
        }
        return voterTurnouts;
    }

    public List<Double> getVoterMobilizations() {
        List<Double> voterMobilizations = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            voterMobilizations.add(district.getVoterMobilization());
        }
        return voterMobilizations;
    }

    public List<Double> getBallotBoxConsistencies() {
        List<Double> ballotBoxConsistencies = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            ballotBoxConsistencies.add(district.getBallotBoxConsistency());
        }
        return ballotBoxConsistencies;
    }

    public List<Double> getPostalVoteShares() {
        List<Double> postalVoteShares = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            postalVoteShares.add(district.getPostalVoteShare());
        }
        return postalVoteShares;
    }

    public List<Double> getInvalidBallotsRates() {
        List<Double> invalidBallotsRates = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            invalidBallotsRates.add(district.getInvalidBallotsRate());
        }
        return invalidBallotsRates;
    }

    public List<Double> getVotingEffectivenesses() {
        List<Double> votingEffectivenesses = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            votingEffectivenesses.add(district.getVotingEffectiveness());
        }
        return votingEffectivenesses;
    }

    public List<Double> getProxyVotersCounts() {
        List<Double> proxyVotersCounts = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            proxyVotersCounts.add(district.getProxyVotersCount());
        }
        return proxyVotersCounts;
    }

    public List<Double> getCandidateASupports() {
        List<Double> candidateASupports = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            candidateASupports.add(district.getCandidateASupport());
        }
        return candidateASupports;
    }

    public List<Double> getCandidateBSupports() {
        List<Double> candidateBSupports = new ArrayList<>();
        for (ElectoralDistrict district : districts) {
            candidateBSupports.add(district.getCandidateBSupport());
        }
        return candidateBSupports;
    }

    @Override
    public String toString() {
        return "ElectoralDistricts{" +
                "districts=" + districts +
                '}';
    }
}
