package com.general.rentacalculator.model;

public class ResultRenta {

    /**
     * Total cotizado
     */
    private double cotizado;

    /**
     * Calculation basis
     */
    private double base;
    /**
     * State fee as for income
     */
    private double cuotaEstado;
    /**
     * Comunidad fee as for income
     */
    private double cuotaAuto;

    /**
     * State fee as for interests
     */
    private double interesesEstado;
    /**
     * Comunidad fee as for interests
     */
    private double interesesAuto;

    /**
     * Actual quantity to be deducted as for donations
     */
    private double deduccionDonacionFinal;
    /**
     * Actual quantity donated
     */
    private double donacionEfectiva;

    /**
     * Liquid fee, total amount to be paid
     */
    private double cuotaLiquida;
    /**
     * Total retained, total amount retained, already paid
     */
    private double retenidoTotal;
    /**
     * Final result
     */
    private double resultado;
    /**
     * Actual tax
     */
    private double tasaRecomendada;

    /**
     * Doing declaration is mandatory
     */
    private boolean obligatorio=false;
    /**
     * Doing declaration depends on other factors
     */
    private boolean obligacionCondicionante=false;
    /**
     * Doing declaration is optional
     */
    private boolean exento=false;

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getCuotaEstado() {
        return cuotaEstado;
    }

    public void setCuotaEstado(double cuotaEstado) {
        this.cuotaEstado = cuotaEstado;
    }

    public double getCuotaAuto() {
        return cuotaAuto;
    }

    public void setCuotaAuto(double cuotaAuto) {
        this.cuotaAuto = cuotaAuto;
    }

    public double getInteresesEstado() {
        return interesesEstado;
    }

    public void setInteresesEstado(double interesesEstado) {
        this.interesesEstado = interesesEstado;
    }

    public double getInteresesAuto() {
        return interesesAuto;
    }

    public void setInteresesAuto(double interesesAuto) {
        this.interesesAuto = interesesAuto;
    }

    public double getDeduccionDonacionFinal() {
        return deduccionDonacionFinal;
    }

    public void setDeduccionDonacionFinal(double deduccionDonacionFinal) {
        this.deduccionDonacionFinal = deduccionDonacionFinal;
    }

    public double getDonacionEfectiva() {
        return donacionEfectiva;
    }

    public void setDonacionEfectiva(double donacionEfectiva) {
        this.donacionEfectiva = donacionEfectiva;
    }

    public double getCuotaLiquida() {
        return cuotaLiquida;
    }

    public void setCuotaLiquida(double cuotaLiquida) {
        this.cuotaLiquida = cuotaLiquida;
    }

    public double getRetenidoTotal() {
        return retenidoTotal;
    }

    public void setRetenidoTotal(double retenidoTotal) {
        this.retenidoTotal = retenidoTotal;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getTasaRecomendada() {
        return tasaRecomendada;
    }

    public void setTasaRecomendada(double tasaRecomendada) {
        this.tasaRecomendada = tasaRecomendada;
    }

    public double getCotizado() {
        return cotizado;
    }

    public void setCotizado(double cotizado) {
        this.cotizado = cotizado;
    }

    public boolean isObligatorio(){ return obligatorio; }

    public void setObligatorio(boolean obligatorio) { this.obligatorio = obligatorio; }

    public boolean isExento() { return exento; }

    public void setExento(boolean exento) { this.exento = exento; }

    public boolean isObligacionCondicionante() { return obligacionCondicionante; }

    public void setObligacionCondicionante(boolean obligacionCondicionante) { this.obligacionCondicionante=obligacionCondicionante; }
}
