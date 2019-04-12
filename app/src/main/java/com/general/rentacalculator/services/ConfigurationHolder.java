package com.general.rentacalculator.services;

import android.content.Context;
import android.content.res.AssetManager;

import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationHolder {

    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("application-constants.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }

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

    protected static double getGeographicMobility(boolean mobility){
        if(mobility){
            // TODO
            return 1;
        }else{
            return 0;
        }
    }

}
