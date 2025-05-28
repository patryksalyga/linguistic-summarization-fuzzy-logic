package com.example.linguisticsummarizationfuzzylogic;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private String name;
    List<EntityValue> valuesList;

    public Entity(String name) {
        this.name = name;
        valuesList = new ArrayList<>();
    }

    public void addValue(String value, ElectoralDistricts electoralDistricts) {
        if (name.equals("Area Type")) {
            List <ElectoralDistrict> districts = new ArrayList<>();
                switch (value) {
                    case "miasto":
                        for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                            if (district.getAreaType().equals("miasto") || district.getAreaType().equals("miasto i wieś")) {
                                districts.add(district);
                            }
                        }
                        break;
                    case "wieś":
                        for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                            if (district.getAreaType().equals("wieś")) {
                                districts.add(district);
                            }
                        }
                        break;
                    case "statek":
                        for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                            if (district.getAreaType().equals("statek")) {
                                districts.add(district);
                            }
                        }
                        break;
                    case "zagranica":
                        for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                            if (district.getAreaType().equals("zagranica")) {
                                districts.add(district);
                            }
                        }
                        break;
                    default:
                        System.out.println("Unsupported district type: " + value);
                }
            valuesList.add(new EntityValue(value, false, districts));
        }

        if (name.equals("Voivodeship")) {
            List <ElectoralDistrict> districts = new ArrayList<>();
            switch (value) {
                case "dolnośląskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("dolnośląskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "kujawsko-pomorskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("kujawsko-pomorskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "lubelskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("lubelskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "lubuskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("lubuskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "łódzkie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("łódzkie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "małopolskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("małopolskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "mazowieckie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("mazowieckie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "opolskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("opolskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "podkarpackie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("podkarpackie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "podlaskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("podlaskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "pomorskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("pomorskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "śląskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("śląskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "świętokrzyskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("świętokrzyskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "warmińsko-mazurskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("warmińsko-mazurskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "wielkopolskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("wielkopolskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                case "zachodniopomorskie":
                    for (ElectoralDistrict district : electoralDistricts.getDistricts()) {
                        if (district.getVoivodeship().equals("zachodniopomorskie")) {
                            districts.add(district);
                        }
                    }
                    break;
                default:
                    System.out.println("Unsupported voivodeship: " + value);
            }
            valuesList.add(new EntityValue(value, false, districts));
        }
    }

    public List<EntityValue> getValues() {
        return valuesList;
    }

    public String getName() {
        return name;
    }
}
