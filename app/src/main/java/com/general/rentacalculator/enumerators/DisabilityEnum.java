package com.general.rentacalculator.enumerators;

import com.general.rentacalculator.R;

public enum DisabilityEnum {
    SUPERIOR_33(R.string.disability33),
    SUPERIOR_65(R.string.disability65),
    NONE(R.string.disabilityNone);

    private int idI18nKey;

    private DisabilityEnum(int idI18nKey){
        this.idI18nKey=idI18nKey;
    }

    /**
     * Returns the ID, not the i18nKey
     * @return
     */
    public int getIdI18nKey(){
        return  idI18nKey;
    }

}
