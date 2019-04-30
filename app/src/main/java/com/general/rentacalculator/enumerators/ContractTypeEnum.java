package com.general.rentacalculator.enumerators;

public enum ContractTypeEnum {
    CONTRATO_INDEFINIDO("Contrato indefinido"),
    CONTRATO_DEFINIDO("Contrato de duración determinada");

    String i18nKey;

    ContractTypeEnum(String i18nKey){
        this.i18nKey=i18nKey;
    }
}
