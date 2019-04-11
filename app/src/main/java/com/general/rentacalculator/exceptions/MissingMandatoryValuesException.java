package com.general.rentacalculator.exceptions;

public class MissingMandatoryValuesException extends Exception {

    private String reason;

    public MissingMandatoryValuesException(String reason){
        this.reason=reason;
    }

    public String getReason(){
        return this.reason;
    }

}
