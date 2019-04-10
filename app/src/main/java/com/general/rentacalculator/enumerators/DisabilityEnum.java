package com.general.rentacalculator.enumerators;

public enum DisabilityEnum {
    SUPERIOR_33("Igual o superior al 33 por 100"),
        SUPERIOR_65("Igual o superior al 33 por 100");

    String i18nKey;

    private DisabilityEnum(String i18nKey){
        this.i18nKey=i18nKey;
    }

}
