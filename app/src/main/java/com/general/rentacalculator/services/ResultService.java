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

    public void rentaResultCalculator(ResultRenta resultRenta, Renta renta, Context context) throws MissingMandatoryValuesException {
        Map<Double, Double> auxCuotaMap = new HashMap<>();
        // basis
        double base = this.calculateBase(renta.getSalarioBruto(), renta.getCotizado(), false, 0);
        resultRenta.setBase(base);

        // gravamen
        renta.setGravamen(calculateGravamen(renta.getEdad(), renta.getDiscapacidad(), renta.isAyuda(), renta.getHijosMenores25Anos(), renta.getHijosMenores3Anos(), renta.getAscendientesMayores65Anos(), renta.getAscendientesMayores75Anos()));

        // state cuota
        auxCuotaMap = obtainPropertyMap(ESTADO, context);
        double cuotaEstado = this.calculateCota(resultRenta.getBase(), auxCuotaMap) - this.calculateCota(renta.getGravamen(), auxCuotaMap);
        resultRenta.setCuotaEstado(cuotaEstado);

        // autonomic cuota
        auxCuotaMap = obtainPropertyMap(AUTO, context);
        double cuotaAuto = this.calculateCota(resultRenta.getBase(), auxCuotaMap) - this.calculateCota(renta.getGravamen(), auxCuotaMap);
        resultRenta.setCuotaEstado(cuotaAuto);
        double tipoEst = resultRenta.getCuotaEstado() / resultRenta.getBase() * 100;
        double tipoAuto = resultRenta.getCuotaAuto() / resultRenta.getBase() * 100;
        // FIXME interests arent tipo. Actually, i think tipo isnt necessary
        resultRenta.setInteresesEstado(tipoEst);
        resultRenta.setInteresesAuto(tipoAuto);

        // donations
        if(renta.getDonaciones()>0){
            Map<Double, Double> tramosDonacion = obtainTramosDonacion(renta.isMas3Anos(), context);
            double desgravacionByDonation = this.calculateCota(resultRenta.getBase(), tramosDonacion);
            resultRenta.setDeduccionDonacionFinal(calculateDonationFinal(desgravacionByDonation, resultRenta.getBase()));
        }

        // state reliefs and taxes
        double deduccionesEstado = calculateDeduccionesPartial(renta.getDeduccionesEstado(), resultRenta.getDeduccionDonacionFinal());
        double liquidoEstado = calculateLiquidoPartial(resultRenta.getCuotaEstado(), resultRenta.getInteresesEstado(), deduccionesEstado);

        // autonomic reliefs and taxes
        double deduccionesAutonomia = calculateDeduccionesPartial(renta.getDeduccionesComunidad(), resultRenta.getDeduccionDonacionFinal());
        double liquidoAutonomia = calculateLiquidoPartial(resultRenta.getCuotaAuto(), resultRenta.getInteresesAuto(), deduccionesAutonomia);

        // total reliefs and taxes
        resultRenta.setCuotaLiquida(calculateLiquidoTotal(liquidoEstado, liquidoAutonomia));
        resultRenta.setRetenidoTotal(calculateRetenidoTotal(renta.getRetenido(), renta.getInteresesRetenidos()));

        // result
        resultRenta.setResultado(calculateResultado(resultRenta.getCuotaLiquida(), resultRenta.getRetenidoTotal()));
        resultRenta.setTasaEfectiva(calculateTasaEfectiva(resultRenta.getResultado(), resultRenta.getBase()));
    }

    private Map<Double, Double> obtainPropertyMap(String property, Context context) {
        String[] rawData = ConfigurationHolder.getProperty(property, context).split(";");
        Map<Double, Double> tramos = new HashMap<>();
        for (String datum : rawData) {
            tramos.put(Double.valueOf(datum.split(",")[0]), Double.valueOf(datum.split(",")[1]));
        }
        return tramos;
    }

    private Map<Double, Double> obtainTramosDonacion(boolean moreThreeYearsDonating, Context context) {
        return obtainPropertyMap(moreThreeYearsDonating ? DONA_3 : DONA_0, context);
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

    /**
     * Calculates gravamen
     *
     * @param contributorAge
     * @param contributorDisabiliy
     * @param requiresHelpOrHaveReducedMobility
     * @param descendentsQuantity
     * @param minors3yo
     * @param ascendentsOlder65OrDisabledQuantity
     * @param ascendentsOlder75Quantity
     * @return
     * @throws MissingMandatoryValuesException
     */
    public double calculateGravamen(int contributorAge, DisabilityEnum contributorDisabiliy, boolean requiresHelpOrHaveReducedMobility, int descendentsQuantity, int minors3yo, int ascendentsOlder65OrDisabledQuantity, int ascendentsOlder75Quantity) throws MissingMandatoryValuesException {
        // mandatory vars
        if (contributorAge <= 0) {
            throw new MissingMandatoryValuesException("Contributor age is missing or invalid");
        }
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

    /**
     * Calculates actual tax in percentage
     *
     * @param resultado
     * @param base
     * @return
     * @throws MissingMandatoryValuesException if basis is invalid
     */
    private double calculateTasaEfectiva(double resultado, double base) throws MissingMandatoryValuesException {
        if (base <= 0) {
            throw new MissingMandatoryValuesException("Calculus basis is equals to 0 or invalid value");
        }

        return resultado / base;
    }

    /**
     * Calculates result of declaration, whether user has paid in excess or not, therefore, if user
     * has to be returned or has to pay.
     * If result is positive, user has to pay, if it's negative, user has to be paid.
     *
     * @param cuotaLiquida  what actually have to be paid
     * @param retenidoTotal what have been paid
     * @return result
     * @throws MissingMandatoryValuesException if any of both parameters has invalid value
     */
    private double calculateResultado(double cuotaLiquida, double retenidoTotal) throws MissingMandatoryValuesException {
        if (cuotaLiquida <= 0) {
            throw new MissingMandatoryValuesException("Liquid cuota has invalid value");
        } else if (retenidoTotal <= 0) {
            throw new MissingMandatoryValuesException("Total retained has invalid value");
        }

        return cuotaLiquida - retenidoTotal;
    }

    /**
     * Calculates actual amount paid by user. It sums up retained money by salary and by interests
     *
     * @param retenido
     * @param interesesRetenidos
     * @return
     */
    private double calculateRetenidoTotal(double retenido, double interesesRetenidos) {
        return retenido + interesesRetenidos;
    }

    /**
     * Calculates total amount user has to pay, summing up what has to pay to state and to autonomic government
     *
     * @param liquidoEstado
     * @param liquidoAutonomia
     * @return
     */
    private double calculateLiquidoTotal(double liquidoEstado, double liquidoAutonomia) {
        return liquidoAutonomia + liquidoEstado;
    }

    /**
     * Calculates total to be paid to autonomica government or state
     *
     * @param cuota       from autonomic gov or state
     * @param intereses   from autonomic gov or state
     * @param deducciones from autonomic gov or state
     * @return
     */
    private double calculateLiquidoPartial(double cuota, double intereses, double deducciones) {
        return cuota + intereses - deducciones;
    }

    /**
     * Calculates total reliefs to autonomic government or state
     *
     * @param deducciones          from autonomic gov or state
     * @param desgravacionDonacion total relief by donations
     * @return
     */
    private double calculateDeduccionesPartial(double deducciones, double desgravacionDonacion) {
        return deducciones + desgravacionDonacion / 2;
    }

    /**
     * Calculates amount user has actually paid. Difference would be what State paid to that ONG
     *
     * @param donacion                   donation made by user directly to ONG
     * @param desgravacioneFinalDonacion reliefed by donation
     * @return
     */
    private double calculateDonacionEfectiva(double donacion, double desgravacioneFinalDonacion) {
        return donacion - desgravacioneFinalDonacion;
    }

    /**
     * Calculates actual relief by donation, it cannot be bigger than 10% basis
     * @param desgravacionByDonation
     * @param basis
     * @return
     */
    private double calculateDonationFinal(double desgravacionByDonation, double basis) {
        if (desgravacionByDonation < 0.1 * basis) {
            return desgravacionByDonation;
        } else {
            return 0.1 * basis;
        }
    }

}
