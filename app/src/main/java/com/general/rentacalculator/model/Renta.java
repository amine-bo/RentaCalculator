package com.general.rentacalculator.model;

import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;
import com.general.rentacalculator.enumerators.DisabilityEnum;
import com.general.rentacalculator.exceptions.MissingMandatoryValuesException;

import java.io.Serializable;
import java.util.List;

public class Renta implements Serializable {
    // contractual situation
    private double salarioBruto;
    private double retenido;
    private float retencion;
    private double exentos;
    private double cotizado;
    private double extrasOrdinarias;
    private double extrasFuerza;

    // personal situation
    private ComunidadAutonomaEnum comunidadAutonoma;
    private int edad;
    private DisabilityEnum discapacidad;
    private boolean ayuda;
    private Integer hijosMenores3Anos;
    private Integer hijosMenores25Anos;
    private Integer ascendientesMayores65Anos;
    private Integer ascendientesMayores75Anos;

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

    public float getRetencion() {
        return retencion;
    }

    public void setRetencion(float retencion) {
        this.retencion = retencion;
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

    public Integer getHijosMenores3Anos() {
        return hijosMenores3Anos;
    }

    public void setHijosMenores3Anos(Integer hijosMenores3Anos) {
        this.hijosMenores3Anos = hijosMenores3Anos;
    }

    public Integer getHijosMenores25Anos() {
        return hijosMenores25Anos;
    }

    public void setHijosMenores25Anos(Integer hijosMenores25Anos) {
        this.hijosMenores25Anos = hijosMenores25Anos;
    }

    public Integer getAscendientesMayores65Anos() {
        return ascendientesMayores65Anos;
    }

    public void setAscendientesMayores65Anos(Integer ascendientesMayores65Anos) {
        this.ascendientesMayores65Anos = ascendientesMayores65Anos;
    }

    public Integer getAscendientesMayores75Anos() {
        return ascendientesMayores75Anos;
    }

    public void setAscendientesMayores75Anos(Integer ascendientesMayores75Anos) {
        this.ascendientesMayores75Anos = ascendientesMayores75Anos;
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

    public void setInteresesBrutos(double interesesBrutos) {
        this.interesesBrutos = interesesBrutos;
    }

    public void setInteresesNetos(double interesesNetos) {
        this.interesesNetos = interesesNetos;
    }

    public void setInteresesRetenidos(double interesesRetenidos) {
        this.interesesRetenidos = interesesRetenidos;
    }
}
