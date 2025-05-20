package com.example.linguisticsummarizationfuzzylogic;

public class ElectoralDistrict {
    private String areaType;
    private String voivodeship;
    private double commissionPreparationLevel;
    private double surplusBallots;
    private double voterTurnout;
    private double voterMobilization;
    private double ballotBoxConsistency;
    private double postalVoteShare;
    private double invalidBallotsRate;
    private double votingEffectiveness;
    private int proxyVotersCount;
    private double candidateASupport;
    private double candidateBSupport;

    public ElectoralDistrict(String areaType, String voivodeship, double commissionPreparationLevel, double surplusBallots, double voterTurnout, double voterMobilization, double ballotBoxConsistency, double postalVoteShare, double invalidBallotsRate, double votingEffectiveness, int proxyVotersCount, double candidateASupport, double candidateBSupport) {
        this.areaType = areaType;
        this.voivodeship = voivodeship;
        this.commissionPreparationLevel = commissionPreparationLevel;
        this.surplusBallots = surplusBallots;
        this.voterTurnout = voterTurnout;
        this.voterMobilization = voterMobilization;
        this.ballotBoxConsistency = ballotBoxConsistency;
        this.postalVoteShare = postalVoteShare;
        this.invalidBallotsRate = invalidBallotsRate;
        this.votingEffectiveness = votingEffectiveness;
        this.proxyVotersCount = proxyVotersCount;
        this.candidateASupport = candidateASupport;
        this.candidateBSupport = candidateBSupport;
    }

    public String getAreaType() {
        return areaType;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public double getCommissionPreparationLevel() {
        return commissionPreparationLevel;
    }

    public double getSurplusBallots() {
        return surplusBallots;
    }

    public double getVoterTurnout() {
        return voterTurnout;
    }

    public double getVoterMobilization() {
        return voterMobilization;
    }

    public double getBallotBoxConsistency() {
        return ballotBoxConsistency;
    }

    public double getPostalVoteShare() {
        return postalVoteShare;
    }

    public double getInvalidBallotsRate() {
        return invalidBallotsRate;
    }

    public double getVotingEffectiveness() {
        return votingEffectiveness;
    }

    public int getProxyVotersCount() {
        return proxyVotersCount;
    }

    public double getCandidateASupport() {
        return candidateASupport;
    }

    public double getCandidateBSupport() {
        return candidateBSupport;
    }

    @Override
    public String toString() {
        return "ElectoralDistrict{" +
                "areaType='" + areaType + '\'' +
                ", voivodeship='" + voivodeship + '\'' +
                ", commissionPreparationLevel=" + commissionPreparationLevel +
                ", surplusBallots=" + surplusBallots +
                ", voterTurnout=" + voterTurnout +
                ", voterMobilization=" + voterMobilization +
                ", ballotBoxConsistency=" + ballotBoxConsistency +
                ", postalVoteShare=" + postalVoteShare +
                ", invalidBallotsRate=" + invalidBallotsRate +
                ", votingEffectiveness=" + votingEffectiveness +
                ", proxyVotersCount=" + proxyVotersCount +
                ", candidateASupport=" + candidateASupport +
                ", candidateBSupport=" + candidateBSupport +
                '}';
    }
}
