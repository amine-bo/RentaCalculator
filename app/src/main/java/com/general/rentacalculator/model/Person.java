package com.general.rentacalculator.model;

import com.general.rentacalculator.enumerators.DisabilityEnum;

public class Person {
    private int edad;
    private DisabilityEnum discapacidad;

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public DisabilityEnum getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(DisabilityEnum discapacidad) {
        this.discapacidad = discapacidad;
    }
}
