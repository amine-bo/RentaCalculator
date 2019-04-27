package com.general.rentacalculator.services;

import android.content.Context;

import com.general.rentacalculator.enumerators.DisabilityEnum;
import com.general.rentacalculator.exceptions.MissingMandatoryValuesException;
import com.general.rentacalculator.model.Renta;
import com.general.rentacalculator.model.ResultRenta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultService {
    private final static String ESTADO = "general.constantes.estado";
    private final static String AUTO = "general.constantes.auto";
    private final static String INT_ESTADO = "general.constantes.int_estado";
    private final static String INT_AUTO = "general.constantes.int_auto";
    private final static String DONA_0 = "general.constantes.dona0";
    private final static String DONA_3 = "general.constantes.dona3";

    public void rentaResultCalculator(ResultRenta resultRenta, Renta renta, Context context) {
        Map<Double, Double> auxCuotaMap = new HashMap<>();
        double base = this.calculateBase(renta.getSalarioBruto(), renta.getCotizado(), false, 0);
        resultRenta.setBase(base);
        String[] estadoArray = ConfigurationHolder.getProperty(ESTADO, context).split(";");
        for(String cuota:estadoArray){
            auxCuotaMap.put(Double.valueOf(cuota.split(",")[0]), Double.valueOf(cuota.split(",")[1]));
        }
        double cuotaEstado = this.calculateCota(resultRenta.getBase(),auxCuotaMap) - this.calculateCota(renta.getGravamen(), auxCuotaMap);
        resultRenta.setCuotaEstado(cuotaEstado);
        String[] autoArray = ConfigurationHolder.getProperty(AUTO, context).split(";");
        auxCuotaMap = new HashMap<>();
        for(String cuota:autoArray){
            auxCuotaMap.put(Double.valueOf(cuota.split(",")[0]), Double.valueOf(cuota.split(",")[1]));
        }
        double cuotaAuto = this.calculateCota(resultRenta.getBase(),auxCuotaMap) - this.calculateCota(renta.getGravamen(), auxCuotaMap);
        resultRenta.setCuotaEstado(cuotaAuto);
        double tipoEst = resultRenta.getCuotaEstado()/resultRenta.getBase() * 100;
        double tipoAuto = resultRenta.getCuotaAuto()/resultRenta.getBase() * 100;
        resultRenta.setInteresesEstado(tipoEst);
        resultRenta.setInteresesAuto(tipoAuto);
    }

    /**
     * Obtain basis of calculation
     *
     * @param salarioBruto
     * @param cotizado
     * @param movilidadGeo
     * @param otrosGastosDeducibles
     * @return
     */
    private double calculateBase(double salarioBruto, double cotizado, boolean movilidadGeo, double otrosGastosDeducibles) {
        return salarioBruto - cotizado - otrosGastosDeducibles - getValueByGeographicMobility(movilidadGeo);
    }

    private double getValueByGeographicMobility(boolean movilidadGeo) {
        if (movilidadGeo) {
            return ConfigurationHolder.getGeographicMobility();
        } else {
            return 0;
        }
    }

    /**
     * Calculates Retencion from Salario Bruto and Retencion as percentage
     *
     * @param salarioBruto
     * @param retencion    as percentage
     */
    private double calculateRetenido(double retencion, double salarioBruto) {
        return salarioBruto * retencion / 100;
    }

    /**
     * Calculates Cotizado from Contract type
     *
     * @param valorImpositivoDebidoTipoContrato   value in function of type of contract at ConfigurationHolder
     * @param valorImpositivoContingenciasComunes value from ConfigurationHolder
     * @param valorImpositivoFormacion            value from ConfigurationHolder
     * @param salarioBruto
     * @param exentos                             optional
     * @param horasExtraOrdinarias                optional
     * @param valorCotizacionHorasExtraOrdinarias as percentage mandatory if horasExtraOrdinarias present or else throws MissingMandatoryValuesException
     * @param horasExtraFuerza                    optional
     * @param valorCotizacionHorasExtraFuerza     as percentage mandatory if horasExtraFuerza present or else throws MissingMandatoryValuesException
     * @throws MissingMandatoryValuesException
     */
    private double calculateCotizado(double valorImpositivoDebidoTipoContrato,
                                    double valorImpositivoContingenciasComunes,
                                    double valorImpositivoFormacion, double salarioBruto, double exentos,
                                    int horasExtraOrdinarias, double valorCotizacionHorasExtraOrdinarias,
                                    int horasExtraFuerza, double valorCotizacionHorasExtraFuerza) throws MissingMandatoryValuesException {
        double cotizacion = valorImpositivoDebidoTipoContrato + valorImpositivoContingenciasComunes
                + valorImpositivoFormacion;
        return this.calculateCotizado(cotizacion, salarioBruto, exentos, horasExtraOrdinarias,
                valorCotizacionHorasExtraOrdinarias, horasExtraFuerza,
                valorCotizacionHorasExtraFuerza);

    }

    /**
     * Calculates Cotizado from Cotizacion percentage
     *
     * @param cotizacion                          as percentage
     * @param salarioBruto
     * @param exentos                             optional
     * @param horasExtraOrdinarias                optional
     * @param valorCotizacionHorasExtraOrdinarias as percentage mandatory if horasExtraOrdinarias present or else throws MissingMandatoryValuesException
     * @param horasExtraFuerza                    optional
     * @param valorCotizacionHorasExtraFuerza     as percentage mandatory if horasExtraFuerza present or else throws MissingMandatoryValuesException
     */
    private double calculateCotizado(double cotizacion, double salarioBruto, double exentos,
                                    int horasExtraOrdinarias, double valorCotizacionHorasExtraOrdinarias,
                                    int horasExtraFuerza, double valorCotizacionHorasExtraFuerza) throws MissingMandatoryValuesException {
        if (valorCotizacionHorasExtraOrdinarias == 0 && horasExtraOrdinarias > 0) {
            throw new MissingMandatoryValuesException("Regular overtime setted but contribution value is 0");
        } else if (valorCotizacionHorasExtraFuerza == 0 && horasExtraFuerza > 0) {
            throw new MissingMandatoryValuesException("Force majeur time setted but contribution value is 0");
        }
        return ((salarioBruto + exentos) * cotizacion / 100)
                + (horasExtraOrdinarias * valorCotizacionHorasExtraOrdinarias / 100)
                + (horasExtraFuerza * valorCotizacionHorasExtraFuerza / 100);
    }

    /**
     * Calculates retained from gross and net
     *
     * @param interesesBrutos
     * @param interesesNetos
     */
    private double calculateInteresesRetenidos(double interesesBrutos, double interesesNetos) {
        return interesesBrutos - interesesNetos;
    }

    /**
     * Calculates net from gross and retained
     *
     * @param interesesBrutos
     * @param interesesRetenidos
     */
    private double calculateInteresesNetos(double interesesBrutos, double interesesRetenidos) {
        return interesesBrutos - interesesRetenidos;
    }

    /**
     * Calculates gross from net and retained
     *
     * @param interesesNetos
     * @param interesesRetenidos
     */
    private double calculateInteresesBrutos(double interesesNetos, double interesesRetenidos) {
        return interesesNetos + interesesRetenidos;
    }


    /**
     * Caclculates cota from taxes sections. The last section should be an enormeous limit.
     * In case calculus bases is bigger than this enormous last limit, tax would be applied in any
     * case to following section
     * TODO should be private
     *
     * @param baseCalculo calculus basis
     * @param tramos      taxes sections
     * @return
     */
    private double calculateCota(double baseCalculo, Map<Double, Double> tramos) {
        List<Double> limits = new ArrayList<>(tramos.keySet());
        Collections.sort(limits);
        double last = 0;
        double result = 0d;

        for (int i = 0; i < limits.size(); i++) {
            // we get the limit of current section
            double lim = limits.get(i);

            if (i == limits.size() - 1 || baseCalculo < lim) {
                // if its the last section we apply last tax to the rest, doesnt mind if bigger than limit
                // if its lower than section limit, we apply tax to the rest
                result += (baseCalculo - last) * (tramos.get(lim) / 100);
            } else {
                // calculate result from tax to current section
                result += (lim - last) * (tramos.get(lim) / 100);
            }

            if (baseCalculo > lim) {
                // if there are still more sections to be applied, we continue
                last = lim;
            } else {
                break;
            }
        }

        return result;
    }

    private double calculateGravamen(int contributorAge, DisabilityEnum contributorDisabiliy, boolean requiresHelpOrHaveReducedMobility, int descendentsQuantity, int minors3yo, int ascendentsOlder65OrDisabledQuantity, int ascendentsOlder75Quantity) {
        // personal situation
        double gravamen = calculateGravamenByAge(contributorAge);
        gravamen += calculateGravamenByPersonalDisability(contributorDisabiliy, requiresHelpOrHaveReducedMobility);

        // children
        gravamen += calculateGravamentByChildren(descendentsQuantity);
        gravamen += calculateGravamenByChildrenMinor3yo(minors3yo);

        // ascendents
        gravamen += calculateGravamentByAscendents(ascendentsOlder65OrDisabledQuantity, ascendentsOlder75Quantity);

        return gravamen;
    }

    private double calculateGravamenByAge(int contributorAge) {
        if (contributorAge >= 75) {
            return ConfigurationHolder.getMinimalGravamenForContributorOlder75();
        } else if (contributorAge >= 65) {
            return ConfigurationHolder.getMinimalGravamenForContributorOlder65();
        } else {
            return ConfigurationHolder.getMinimalGravamenForContributor();
        }
    }

    private double calculateGravamenByPersonalDisability(DisabilityEnum contributorDisabiliy, boolean requiresHelpOrHaveReducedMobility) {
        if (DisabilityEnum.SUPERIOR_65.equals(contributorDisabiliy)) {
            return ConfigurationHolder.getGravamenForContributorDisability65();
        } else if (DisabilityEnum.SUPERIOR_33.equals(contributorDisabiliy) && !requiresHelpOrHaveReducedMobility) {
            return ConfigurationHolder.getGravamenForContributorDisability33();
        } else if (DisabilityEnum.SUPERIOR_33.equals(contributorDisabiliy) && requiresHelpOrHaveReducedMobility) {
            return ConfigurationHolder.getGravamenForContributorDisabilityWithHelpOrReducedMobility();
        }
        return 0;
    }

    private double calculateGravamentByChildren(int descendentsQuantity) {
        if (descendentsQuantity == 1) {
            return ConfigurationHolder.getMinimalGravamenForOneChild();
        } else if (descendentsQuantity == 2) {
            return ConfigurationHolder.getMinimalGravamenForTwoChildren();
        } else if (descendentsQuantity == 3) {
            return ConfigurationHolder.getMinimalGravamenForThreeChildren();
        } else if (descendentsQuantity >= 4) {
            return ConfigurationHolder.getMinimalGravamenForFourOrMoreChildren();
        }
        return 0;
    }

    private double calculateGravamenByChildrenMinor3yo(int minors3yo) {
        if (minors3yo > 0) {
            return ConfigurationHolder.getGravamenIncreaseByHavingChildYoungerThanThree();
        }
        return 0;
    }

    private double calculateGravamentByAscendents(int ascendentsOlder65OrDisabledQuantity, int ascendentsOlder75Quantity) {
        if (ascendentsOlder75Quantity > 0) {
            return ConfigurationHolder.getMinimalGravamenForAscendentOlder75();
        } else if (ascendentsOlder65OrDisabledQuantity > 0) {
            return ConfigurationHolder.getMinimalGravamenForAscendentOlder65OrDisabled();
        }
        return 0;
    }

}
