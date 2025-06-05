package com.example.linguisticsummarizationfuzzylogic;

import java.util.List;

public class EntityValue {
    private String value;
    private boolean enabled;
    private List<ElectoralDistrict> electoralDistricts;

    public EntityValue(String value, boolean enabled, List<ElectoralDistrict> electoralDistricts) {
        this.value = value;
        this.enabled = enabled;
        this.electoralDistricts = electoralDistricts;
    }

    public List<ElectoralDistrict> getElectoralDistricts() {
        return electoralDistricts;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle(){
        this.enabled = !this.enabled;
    }

    public String getValue() {
        return value;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }
}
