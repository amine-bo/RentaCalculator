package com.general.rentacalculator.services;

import android.content.Context;
import android.content.res.AssetManager;

import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationHolder {

    public static String getProperty(String key, Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("application-constants.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    protected static double getGeographicMobility() {
        // TODO
        return 2000;
    }

    public static double getMinimalGravamenForContributor() {
        // TODO
        return 5550d;
    }

    public static double getMinimalGravamenForContributorOlder65() {
        // TODO
        return 5550 + 1150;
    }

    public static double getMinimalGravamenForContributorOlder75() {
        // TODO
        return 5500 + 1150 + 1400;
    }

    public static double getMinimalGravamenForOneChild() {
        // TODO
        return 2400;
    }

    public static double getMinimalGravamenForTwoChildren() {
        // TODO
        return 2700;
    }

    public static double getMinimalGravamenForThreeChildren() {
        // TODO
        return 4000;
    }

    public static double getMinimalGravamenForFourOrMoreChildren() {
        // TODO
        return 4500;
    }

    public static double getGravamenIncreaseByHavingChildYoungerThanThree() {
        // TODO
        return 2800;
    }

    public static double getMinimalGravamenForAscendentOlder65OrDisabled() {
        // TODO
        return 1150;
    }

    public static double getMinimalGravamenForAscendentOlder75() {
        // TODO
        return 1150 + 1400;
    }

    public static double getGravamenForContributorDisability33() {
        // TODO
        return 3000;
    }

    public static double getGravamenForContributorDisabilityWithHelpOrReducedMobility() {
        // TODO
        return 6000;
    }

    public static double getGravamenForContributorDisability65() {
        // TODO
        return 12000;
    }

    public static double getValorImpositivoContingenciasComunes() {
        // TODO
        return 4.70;
    }

    public static double getValorImpositivoFormacion() {
        // TODO
        return 0.10;
    }

    public static double getValorImpositivoContratoIndefinido() {
        // TODO
        return 1.55;
    }

    public static double getValorImpositivoContratoTemporal() {
        // TODO
        return 1.60;
    }

    public static double getValorCotizacionHorasExtraOrdinarias() {
        // TODO
        return 4.70;
    }

    public static double getValorCotizacionHorasExtraFuerza() {
        // TODO
        return 2.00;
    }

    public static double getMinimo(){
        // TODO
        return 22000;
    }

    public static double getInfimo(){
        // TODO
        return 12643;
    }
}
