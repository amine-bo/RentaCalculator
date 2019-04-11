package com.general.rentacalculator.enumerators;

public enum ComunidadAutonomaEnum {
    ANDALUCIA("general.comunidad.andalucia"),
    ARAGON("general.comunidad.aragon"),
    ASTURIAS("general.comunidad.asturias"),
    BALEARES("general.comunidad.baleares"),
    CANARIAS("general.comunidad.canarias"),
    CANTABRIA("general.comunidad.cantabria"),
    CASTILLA_MANCHA("general.comunidad.castillaMancha"),
    CASTILLA_LEON("general.comunidad.castillaLeon"),
    CATALUNA("general.comunidad.cataluna"),
    VALENCIA("general.comunidad.valencia"),
    EXTREMADURA("general.comunidad.extremadura"),
    GALICIA("general.comunidad.galicia"),
    RIOJA("general.comunidad.rioja"),
    MADRID("general.comunidad.madrid"),
    MURCIA("general.comunidad.murcia"),
    CEUTA("general.comunidad.ceuta"),
    MELILLA("general.comunidad.melilla");

    private String i18nKey;

    ComunidadAutonomaEnum(String i18nKey){
        this.i18nKey=i18nKey;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
