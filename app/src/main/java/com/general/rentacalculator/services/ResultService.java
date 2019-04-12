package com.general.rentacalculator.services;

import com.general.rentacalculator.exceptions.MissingMandatoryValuesException;

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

}
