package com.general.rentacalculator.enumerators;

import com.general.rentacalculator.interfaces.IdI18nKeyEnumQualifier;
import com.general.rentacalculator.R;

public enum ContractTypeEnum implements IdI18nKeyEnumQualifier {
    CONTRATO_INDEFINIDO(R.string.contratoIndefinido),
    CONTRATO_DEFINIDO(R.string.contratoTemporal);

    int idI18nKey;

    ContractTypeEnum(int idI18nKey){
        this.idI18nKey=idI18nKey;
    }

    public int getIdI18nKey(){
        return this.idI18nKey;
    }

}
