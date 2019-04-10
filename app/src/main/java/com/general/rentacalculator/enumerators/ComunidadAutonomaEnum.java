package com.general.rentacalculator.enumerators;

public enum ComunidadAutonomaEnum {
    ANDALUCIA("Andalucía"),
    ARAGON("Aragón"),
    ASTURIAS("Asturias"),
    BALEARES("Baleares"),
    CANARIAS("Canarias"),
    CANTABRIA("Cantabria"),
    CASTILLA_MANCHA("Castilla la Mancha"),
    CASTILLA_LEON("Castilla y León"),
    CATALUNA("Cataluña"),
    VALENCIA("Valencia"),
    EXTREMADURA("Extremadura"),
    GALICIA("Galicia"),
    RIOJA("La Rioja"),
    MADRID("Madrid"),
    MURCIA("Murcia"),
    CEUTA("Ceuta"),
    MELILLA("Melilla");

    String i18nKey;

    private ComunidadAutonomaEnum(String i18nKey){
        this.i18nKey=i18nKey;
    }
}
