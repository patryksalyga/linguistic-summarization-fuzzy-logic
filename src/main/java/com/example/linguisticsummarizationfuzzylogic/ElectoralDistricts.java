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

    @Override
    public String toString() {
        return "ElectoralDistricts{" +
                "districts=" + districts +
                '}';
    }
}
