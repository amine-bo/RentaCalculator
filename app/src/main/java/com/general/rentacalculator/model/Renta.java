package com.general.rentacalculator.model;

import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;
import com.general.rentacalculator.enumerators.DisabilityEnum;
import com.general.rentacalculator.exceptions.MissingMandatoryValuesException;

import java.util.List;

public class Renta {
    // contractual situation
    private double salarioBruto;
    private double retenido;
    private double exentos;
    private double cotizado;
    private double extrasOrdinarias;
    private double extrasFuerza;

    // personal situation
    private ComunidadAutonomaEnum comunidadAutonoma;
    private int edad;
    private DisabilityEnum discapacidad;
    private boolean ayuda;
    private List<Person> hijos;
    private List<Person> ascendentes;

    // extras
    private double interesesBrutos;
    private double interesesNetos;
    private double interesesRetenidos;
    private double donaciones;
    private boolean mas3Anos;
    private double deduccionesEstado;
    private double deduccionesComunidad;
    private double otrosGastosDeducibles;
    private boolean movilidadGeografica;

    // gravamen
    private double gravamen;

    // extraordinary getters and setters

    /**
     * Calculates Retencion from Salario Bruto and Retencion as percentage
     *
     * @param salarioBruto
     * @param retencion    as percentage
     */
    public void setRetencion(double retencion, double salarioBruto) {
        this.retenido = salarioBruto * retencion / 100;
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
    public void setContrato(double valorImpositivoDebidoTipoContrato,
                            double valorImpositivoContingenciasComunes,
                            double valorImpositivoFormacion, double salarioBruto, double exentos,
                            int horasExtraOrdinarias, double valorCotizacionHorasExtraOrdinarias,
                            int horasExtraFuerza, double valorCotizacionHorasExtraFuerza) throws MissingMandatoryValuesException {
        double cotizacion = valorImpositivoDebidoTipoContrato + valorImpositivoContingenciasComunes
                + valorImpositivoFormacion;
        this.setCotizacion(cotizacion, salarioBruto, exentos, horasExtraOrdinarias,
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
    public void setCotizacion(double cotizacion, double salarioBruto, double exentos,
                              int horasExtraOrdinarias, double valorCotizacionHorasExtraOrdinarias,
                              int horasExtraFuerza, double valorCotizacionHorasExtraFuerza)throws MissingMandatoryValuesException {
        if(valorCotizacionHorasExtraOrdinarias == 0 && horasExtraOrdinarias > 0 ) {
            throw new MissingMandatoryValuesException("Regular overtime setted but contribution value is 0");
        }else if(valorCotizacionHorasExtraFuerza==0&& horasExtraFuerza>0){
            throw new MissingMandatoryValuesException("Force majeur time setted but contribution value is 0");
        }
        this.cotizado = ((salarioBruto + exentos) * cotizacion / 100)
                + (horasExtraOrdinarias * valorCotizacionHorasExtraOrdinarias / 100)
                + (horasExtraFuerza * valorCotizacionHorasExtraFuerza / 100);
    }

    /**
     * Calculates all three interests: gross, net and retained from gross and net
     * @param interesesBrutos
     * @param interesesNetos
     */
    public void setInteresesBrutosAndNetos(double interesesBrutos, double interesesNetos){
        this.interesesBrutos = interesesBrutos;
        this.interesesNetos = interesesNetos;
        this.interesesRetenidos = interesesBrutos - interesesNetos;
}

    /**
     * Calculates all three interests: gross, net and retained from gross and retained
     * @param interesesBrutos
     * @param interesesRetenidos
     */
    public void setInteresesBrutosAndRetenidos(double interesesBrutos, double interesesRetenidos){
        this.interesesBrutos = interesesBrutos;
        this.interesesNetos = interesesBrutos - interesesRetenidos;
        this.interesesRetenidos = interesesRetenidos;
    }

    /**
     * Calculates all three interests: gross, net and retained from net and retained
     * @param interesesNetos
     * @param interesesRetenidos
     */
    public void setInteresesNetosAndRetenidos(double interesesNetos, double interesesRetenidos){
    this.interesesBrutos = interesesNetos + interesesRetenidos;
    this.interesesNetos = interesesNetos;
    this.interesesRetenidos = interesesRetenidos;
}

    // usual getters and setters
    public double getSalarioBruto() {
        return salarioBruto;
    }

    public void setSalarioBruto(double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public double getRetenido() {
        return retenido;
    }

    public void setRetenido(double retenido) {
        this.retenido = retenido;
    }

    public double getExentos() {
        return exentos;
    }

    public void setExentos(double exentos) {
        this.exentos = exentos;
    }

    public double getCotizado() {
        return cotizado;
    }

    public void setCotizado(double cotizado) {
        this.cotizado = cotizado;
    }

    public double getExtrasOrdinarias() {
        return extrasOrdinarias;
    }

    public void setExtrasOrdinarias(double extrasOrdinarias) {
        this.extrasOrdinarias = extrasOrdinarias;
    }

    public double getExtrasFuerza() {
        return extrasFuerza;
    }

    public void setExtrasFuerza(double extrasFuerza) {
        this.extrasFuerza = extrasFuerza;
    }

    public ComunidadAutonomaEnum getComunidadAutonoma() {
        return comunidadAutonoma;
    }

    public void setComunidadAutonoma(ComunidadAutonomaEnum comunidadAutonoma) {
        this.comunidadAutonoma = comunidadAutonoma;
    }

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

    public boolean isAyuda() {
        return ayuda;
    }

    public void setAyuda(boolean ayuda) {
        this.ayuda = ayuda;
    }

    public List<Person> getHijos() {
        return hijos;
    }

    public void setHijos(List<Person> hijos) {
        this.hijos = hijos;
    }

    public List<Person> getAscendentes() {
        return ascendentes;
    }

    public void setAscendentes(List<Person> ascendentes) {
        this.ascendentes = ascendentes;
    }

    public double getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(double donaciones) {
        this.donaciones = donaciones;
    }

    public boolean isMas3Anos() {
        return mas3Anos;
    }

    public void setMas3Anos(boolean mas3Anos) {
        this.mas3Anos = mas3Anos;
    }

    public double getDeduccionesEstado() {
        return deduccionesEstado;
    }

    public void setDeduccionesEstado(double deduccionesEstado) {
        this.deduccionesEstado = deduccionesEstado;
    }

    public double getDeduccionesComunidad() {
        return deduccionesComunidad;
    }

    public void setDeduccionesComunidad(double deduccionesComunidad) {
        this.deduccionesComunidad = deduccionesComunidad;
    }

    public double getOtrosGastosDeducibles() {
        return otrosGastosDeducibles;
    }

    public void setOtrosGastosDeducibles(double otrosGastosDeducibles) {
        this.otrosGastosDeducibles = otrosGastosDeducibles;
    }

    public boolean isMovilidadGeografica() {
        return movilidadGeografica;
    }

    public void setMovilidadGeografica(boolean movilidadGeografica) {
        this.movilidadGeografica = movilidadGeografica;
    }

    public double getGravamen() {
        return gravamen;
    }

    public void setGravamen(double gravamen) {
        this.gravamen = gravamen;
    }

    public double getInteresesBrutos() {
        return interesesBrutos;
    }

    public double getInteresesNetos() {
        return interesesNetos;
    }

    public double getInteresesRetenidos() {
        return interesesRetenidos;
    }
}
