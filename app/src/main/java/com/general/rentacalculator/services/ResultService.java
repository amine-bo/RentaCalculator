package com.general.rentacalculator.services;

import com.general.rentacalculator.exceptions.MissingMandatoryValuesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ResultService {

    /**
     * Obtain basis of calculation
     * @param salarioBruto
     * @param cotizado
     * @param movilidadGeo
     * @param otrosGastosDeducibles
     * @return
     */
    public double calculateBase(double salarioBruto, double cotizado, boolean movilidadGeo, double otrosGastosDeducibles){
        return salarioBruto - cotizado - otrosGastosDeducibles - ConfigurationHolder.getGeographicMobility(movilidadGeo);
    }

    /**
     * Calculates Retencion from Salario Bruto and Retencion as percentage
     *
     * @param salarioBruto
     * @param retencion    as percentage
     */
    public double calculateRetenido(double retencion, double salarioBruto) {
        return salarioBruto * retencion / 100;
    }

    /**
     * Calculates Cotizado from Contract type
     * @param valorImpositivoDebidoTipoContrato value in function of type of contract at ConfigurationHolder
     * @param valorImpositivoContingenciasComunes value from ConfigurationHolder
     * @param valorImpositivoFormacion value from ConfigurationHolder
     * @param salarioBruto
     * @param exentos optional
     * @param horasExtraOrdinarias optional
     * @param valorCotizacionHorasExtraOrdinarias as percentage mandatory if horasExtraOrdinarias present or else throws MissingMandatoryValuesException
     * @param horasExtraFuerza optional
     * @param valorCotizacionHorasExtraFuerza as percentage mandatory if horasExtraFuerza present or else throws MissingMandatoryValuesException
     * @throws MissingMandatoryValuesException
     */
    public double calculateCotizado(double valorImpositivoDebidoTipoContrato,
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
     * @param cotizacion as percentage
     * @param salarioBruto
     * @param exentos optional
     * @param horasExtraOrdinarias optional
     * @param valorCotizacionHorasExtraOrdinarias as percentage mandatory if horasExtraOrdinarias present or else throws MissingMandatoryValuesException
     * @param horasExtraFuerza optional
     * @param valorCotizacionHorasExtraFuerza as percentage mandatory if horasExtraFuerza present or else throws MissingMandatoryValuesException
     */
    public double calculateCotizado(double cotizacion, double salarioBruto, double exentos,
                              int horasExtraOrdinarias, double valorCotizacionHorasExtraOrdinarias,
                              int horasExtraFuerza, double valorCotizacionHorasExtraFuerza)throws MissingMandatoryValuesException {
        if(valorCotizacionHorasExtraOrdinarias == 0 && horasExtraOrdinarias > 0 ) {
            throw new MissingMandatoryValuesException("Regular overtime setted but contribution value is 0");
        }else if(valorCotizacionHorasExtraFuerza==0&& horasExtraFuerza>0){
            throw new MissingMandatoryValuesException("Force majeur time setted but contribution value is 0");
        }
        return ((salarioBruto + exentos) * cotizacion / 100)
                + (horasExtraOrdinarias * valorCotizacionHorasExtraOrdinarias / 100)
                + (horasExtraFuerza * valorCotizacionHorasExtraFuerza / 100);
    }

    /**
     * Calculates retained from gross and net
     * @param interesesBrutos
     * @param interesesNetos
     */
    public double calculateInteresesRetenidos(double interesesBrutos, double interesesNetos){
        return interesesBrutos - interesesNetos;
    }

    /**
     * Calculates net from gross and retained
     * @param interesesBrutos
     * @param interesesRetenidos
     */
    public double calculateInteresesNetos(double interesesBrutos, double interesesRetenidos){
        return interesesBrutos - interesesRetenidos;
    }

    /**
     * Calculates gross from net and retained
     * @param interesesNetos
     * @param interesesRetenidos
     */
    public double calculateInteresesBrutos(double interesesNetos, double interesesRetenidos){
        return interesesNetos + interesesRetenidos;
    }


    /**
     * Caclculates cota from taxes sections. The last section should be an enormeous limit.
     * In case calculus bases is bigger than this enormous last limit, tax would be applied in any
     * case to following section
     * TODO should be private
     * @param baseCalculo calculus basis
     * @param tramos taxes sections
     * @return
     */
    public double calculateCota(double baseCalculo, Map<Double,Double> tramos){
        List<Double> limits = new ArrayList<>(tramos.keySet());
        Collections.sort(limits);
        double last = 0;
        double result = 0d;

        for(int i=0; i<limits.size();i++){
            // we get the limit of current section
            double lim = limits.get(i);

            if(i==limits.size()-1 || baseCalculo<lim){
                // if its the last section we apply last tax to the rest, doesnt mind if bigger than limit
                // if its lower than section limit, we apply tax to the rest
                result += (baseCalculo-last) * (tramos.get(lim)/100);
            }else {
                // calculate result from tax to current section
                result += (lim-last) * (tramos.get(lim)/100);
            }

            if(baseCalculo > lim){
                // if there are still more sections to be applied, we continue
                last=lim;
            }else {
                break;
            }
        }

        return result;
    }


}
