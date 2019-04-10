package com.general.rentacalculator.services;

public class ResultService {

    /**
     * Obtain basis of calculation
     * @param grossSalary
     * @param contributed
     * @param geographicMobility
     * @param deductible
     * @return
     */
    public double getBasis(double grossSalary, double contributed, boolean geographicMobility, double deductible){
        return grossSalary - contributed - deductible - ConfigurationHolder.getGeographicMobility(geographicMobility);
    }

}
