package com.general.rentacalculator.enumerators;

import android.content.Context;

import com.general.rentacalculator.services.ConfigurationHolder;

public enum ComunidadAutonomaEnum {
    ANDALUCIA("general.comunidad.andalucia", "general.constantes.auto.andalucia"),
    ARAGON("general.comunidad.aragon", "general.constantes.auto.aragon"),
    ASTURIAS("general.comunidad.asturias", "general.constantes.auto.asturias"),
    BALEARES("general.comunidad.baleares", "general.constantes.auto.baleares"),
    CANARIAS("general.comunidad.canarias", "general.constantes.auto.canarias"),
    CANTABRIA("general.comunidad.cantabria", "general.constantes.auto.cantabria"),
    CASTILLA_MANCHA("general.comunidad.castillaMancha", "general.constantes.auto.castillaMancha"),
    CASTILLA_LEON("general.comunidad.castillaLeon", "general.constantes.auto.castillaLeon"),
    CATALUNA("general.comunidad.cataluna", "general.constantes.auto.cataluna"),
    VALENCIA("general.comunidad.valencia", "general.constantes.auto.valencia"),
    EXTREMADURA("general.comunidad.extremadura", "general.constantes.auto.extremadura"),
    GALICIA("general.comunidad.galicia", "general.constantes.auto.galicia"),
    RIOJA("general.comunidad.rioja", "general.constantes.auto.rioja"),
    MADRID("general.comunidad.madrid", "general.constantes.auto.madrid"),
    MURCIA("general.comunidad.murcia", "general.constantes.auto.murcia"),
    CEUTA("general.comunidad.ceuta", "general.constantes.auto.ceuta"),
    MELILLA("general.comunidad.melilla", "general.constantes.auto.melilla");

    private String i18nKey;
    private String taxProperty;

    ComunidadAutonomaEnum(String i18nKey, String taxProperty){
        this.i18nKey = i18nKey;
        this.taxProperty = taxProperty;
    }

    public String getI18nKey() {
        return i18nKey;
    }
    
    public String getTaxProperty(){
        return taxProperty;
    }

    public static ComunidadAutonomaEnum getByText(String text, Context context){
        for(ComunidadAutonomaEnum value: ComunidadAutonomaEnum.values()){
            String str = ConfigurationHolder.getProperty(value.getI18nKey(),context);
            if(str.equals(text)){
                return value;
            }
        }
        return null;
    }
}
