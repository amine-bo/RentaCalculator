package com.general.rentacalculator.services;

import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;

public class ConfigurationHolder {

    /**
     * Get list of taxes of given administration
     * @param comunidad
     * @return
     */
    public int getTaxComunidad(ComunidadAutonomaEnum comunidad){
        // TODO
        return 1;
    }

    /**
     * Get State taxes list
     * @return
     */
    public int getTaxState(){
        // TODO
        return 1;
    }

    /**
     * Get list of taxes of given administration for interests
     * @param comunidad
     * @return
     */
    public int getInterestsTaxComunidad(ComunidadAutonomaEnum comunidad){
        // TODO
        return 1;
    }

    /**
     * Get State taxes list for interests
     * @return
     */
    public int getInterestsTaxState(){
        // TODO
        return 1;
    }

    /**
     * Get discount list for donations
     * @param true if donations have been made for more than 3 years
     * @return
     */
    public int getDonationsDiscount(boolean moreThreeYears){
        // TODO
        return 1;
    }

    public static double getGeographicMobility(boolean mobility){
        if(mobility){
            // TODO
            return 1;
        }else{
            return 0;
        }
    }

}
