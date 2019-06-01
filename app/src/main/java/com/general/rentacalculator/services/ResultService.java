package com.general.rentacalculator.services;

import android.content.Context;
import android.util.Log;

import com.general.rentacalculator.enumerators.ContractTypeEnum;
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
    private final static String INT_ESTADO = "general.constantes.int_estado";
    private final static String INT_AUTO = "general.constantes.int_auto";
    private final static String DONA_0 = "general.constantes.dona0";
    private final static String DONA_3 = "general.constantes.dona3";

    public void rentaResultCalculator(ResultRenta resultRenta, Renta renta, Context context) throws MissingMandatoryValuesException {
        Map<Double, Double> auxCuotaMap = new HashMap<>();
        // retained salary
        resultRenta.setRetenidoTotal(this.calculateRetenido(renta.getRetencion(), renta.getSalarioBruto()));

        // cotizacion
        resultRenta.setCotizado(this.calculateCotizado(renta.getTipoContrato(), renta.getSalarioBruto(), renta.getExentos(), renta.getExtrasOrdinarias(), renta.getExtrasFuerza()));

        // basis
        double base = this.calculateBase(renta.getSalarioBruto(), resultRenta.getCotizado(), renta.isMovilidadGeografica(), 0);
        resultRenta.setBase(base);

        // gravamen
        renta.setGravamen(this.calculateGravamen(renta.getEdad(), renta.getDiscapacidad(), renta.isAyuda(), renta.getHijosMenores25Anos(), renta.getHijosMenores3Anos(), renta.getAscendientesMayores65Anos(), renta.getAscendientesMayores75Anos()));

        // state cuota
        auxCuotaMap = this.obtainPropertyMap(ESTADO, context);
        double cuotaEstado = this.calculateCota(resultRenta.getBase(), auxCuotaMap) - this.calculateCota(renta.getGravamen(), auxCuotaMap);
        resultRenta.setCuotaEstado(cuotaEstado);
        Log.d("ResultService", "CuotaEst: "+cuotaEstado);

        // autonomic cuota
        auxCuotaMap = this.obtainPropertyMap(renta.getComunidadAutonoma().getTaxProperty(), context);
        double cuotaAuto = this.calculateCota(resultRenta.getBase(), auxCuotaMap) - this.calculateCota(renta.getGravamen(), auxCuotaMap);
        resultRenta.setCuotaAuto(cuotaAuto);
        Log.d("ResultService", "CuotaAut: "+cuotaAuto);
        // double tipoEst = resultRenta.getCuotaEstado() / resultRenta.getBase() * 100;
        // double tipoAuto = resultRenta.getCuotaAuto() / resultRenta.getBase() * 100;
 
        // interests
        if (renta.getInteresesBrutos() > 0) { 
            double intEst = this.calculateCota(renta.getInteresesBrutos(), this.obtainPropertyMap(INT_ESTADO,context));
            double intAuto = this.calculateCota(renta.getInteresesBrutos(),this.obtainPropertyMap(INT_AUTO,context));
            resultRenta.setInteresesEstado(intEst);
            resultRenta.setInteresesAuto(intAuto);
        }

        // donations
        if (renta.getDonaciones() > 0) {
            Map<Double, Double> tramosDonacion = this.obtainTramosDonacion(renta.isMas3Anos(), context);
            double desgravacionByDonation = this.calculateCota(renta.getDonaciones(), tramosDonacion);
            resultRenta.setDeduccionDonacionFinal(this.calculateDonationFinal(desgravacionByDonation, renta.getSalarioBruto()));
            resultRenta.setDonacionEfectiva(this.calculateDonacionEfectiva(renta.getDonaciones(),resultRenta.getDeduccionDonacionFinal()));
        }

        // state reliefs and taxes
        double deduccionesEstado = this.calculateDeduccionesPartial(renta.getDeduccionesEstado(), resultRenta.getDeduccionDonacionFinal());
        double liquidoEstado = this.calculateLiquidoPartial(resultRenta.getCuotaEstado(), resultRenta.getInteresesEstado(), deduccionesEstado);
        Log.d("ResultService", "DeduccEstado: "+deduccionesEstado+", liquidoEstado: "+liquidoEstado);

        // autonomic reliefs and taxes
        double deduccionesAutonomia = this.calculateDeduccionesPartial(renta.getDeduccionesComunidad(), resultRenta.getDeduccionDonacionFinal());
        double liquidoAutonomia = this.calculateLiquidoPartial(resultRenta.getCuotaAuto(), resultRenta.getInteresesAuto(), deduccionesAutonomia);
        Log.d("ResultService", "DeduccAuto: "+deduccionesAutonomia+", liquidoAuto: "+liquidoAutonomia);

        // total reliefs and taxes
        resultRenta.setCuotaLiquida(this.calculateLiquidoTotal(liquidoEstado, liquidoAutonomia));
        resultRenta.setRetenidoTotal(this.calculateRetenidoTotal(resultRenta.getRetenidoTotal(), renta.getInteresesRetenidos()));

        // result
        resultRenta.setResultado(this.calculateResultado(resultRenta.getCuotaLiquida(), resultRenta.getRetenidoTotal()));
        resultRenta.setTasaRecomendada(this.calculateTasaRecomendada(resultRenta.getCuotaLiquida(), renta.getSalarioBruto()));

        // duties
        if(base > ConfigurationHolder.getMinimo()){
            resultRenta.setObligatorio(true);

        }else if(base > ConfigurationHolder.getInfimo()){
            resultRenta.setObligacionCondicionante(true);

        }else{
            resultRenta.setExento(true);
        }
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
    private double calculateBase(double salarioBruto, double cotizado, boolean movilidadGeo, double otrosGastosDeducibles) throws MissingMandatoryValuesException {
        this.assertNonNull(salarioBruto, "Gross salary is missing");
        double base = salarioBruto - cotizado - otrosGastosDeducibles - getValueByGeographicMobility(movilidadGeo);
        Log.d("ResultService","Base: "+base);
        return base;
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
    private double calculateRetenido(double retencion, double salarioBruto) throws MissingMandatoryValuesException {
        this.assertNonNull(salarioBruto,"Gross salary is missing");
        double retenido = salarioBruto * retencion / 100;
        Log.d("ResultService", "Retenido: "+ retenido);
        return retenido;
    }

    /**
     * Calculates Cotizado from Contract type
     *
     * @param tipoContrato         contract type
     * @param salarioBruto
     * @param exentos              optional
     * @param horasExtraOrdinarias optional
     * @param horasExtraFuerza     optional
     * @throws MissingMandatoryValuesException if mandatory value is missing such as gross salary or contract type
     */
    private double calculateCotizado(ContractTypeEnum tipoContrato,
                                     double salarioBruto, double exentos,
                                     int horasExtraOrdinarias,
                                     int horasExtraFuerza) throws MissingMandatoryValuesException {
        this.assertNonNull(salarioBruto, "Gross salary is missing");

        double valorImpositivoContingenciasComunes = ConfigurationHolder.getValorImpositivoContingenciasComunes();
        double valorImpositivoFormacion = ConfigurationHolder.getValorImpositivoFormacion();
        double valorImpositivoByTipoContrato;
        if (ContractTypeEnum.CONTRATO_INDEFINIDO.equals(tipoContrato)) {
            valorImpositivoByTipoContrato = ConfigurationHolder.getValorImpositivoContratoIndefinido();
        } else if (ContractTypeEnum.CONTRATO_DEFINIDO.equals(tipoContrato)) {
            valorImpositivoByTipoContrato = ConfigurationHolder.getValorImpositivoContratoTemporal();
        } else {
            throw new MissingMandatoryValuesException("Contract type missing");
        }

        double cotizacion = valorImpositivoByTipoContrato + valorImpositivoContingenciasComunes
                + valorImpositivoFormacion;
        Log.d("ResultService","Cotizacion: "+cotizacion);
        return this.calculateCotizado(cotizacion, salarioBruto, exentos, horasExtraOrdinarias,
                horasExtraFuerza);

    }

    /**
     * Calculates Cotizado from Cotizacion percentage
     *
     * @param cotizacion           as percentage
     * @param salarioBruto
     * @param exentos              optional
     * @param horasExtraOrdinarias optional
     * @param horasExtraFuerza     optional
     */
    private double calculateCotizado(double cotizacion, double salarioBruto, double exentos,
                                     int horasExtraOrdinarias,
                                     int horasExtraFuerza) throws MissingMandatoryValuesException {
        double cotizacionHorasExtraOrdinarias = 0;
        if (horasExtraOrdinarias > 0) {
            cotizacionHorasExtraOrdinarias = horasExtraOrdinarias * ConfigurationHolder.getValorCotizacionHorasExtraOrdinarias() / 100;
        }

        double cotizacionHorasExtraFuerza = 0;
        if (horasExtraFuerza > 0) {
            cotizacionHorasExtraFuerza = horasExtraFuerza * ConfigurationHolder.getValorCotizacionHorasExtraFuerza() / 100;
        }

        this.assertNonNull(salarioBruto, "Gross salary is missing");

        double cotizado = ((salarioBruto + exentos) * cotizacion / 100)
                + cotizacionHorasExtraOrdinarias
                + cotizacionHorasExtraFuerza;
        Log.d("ResultService", "Cotizado: "+cotizado);
        return cotizado;
    }

    /**
     * Calculates retained from gross and net
     *
     * @param interesesBrutos
     * @param interesesNetos
     */
    private double calculateInteresesRetenidos(double interesesBrutos, double interesesNetos) throws MissingMandatoryValuesException{
        this.assertNonNull(interesesBrutos, "Gross interests is missing");
        double intBrutos =interesesBrutos - interesesNetos;
        Log.d("ResultService", "IntBrutos: "+intBrutos);
        return intBrutos;
    }

    /**
     * Calculates net from gross and retained
     *
     * @param interesesBrutos
     * @param interesesRetenidos
     */
    private double calculateInteresesNetos(double interesesBrutos, double interesesRetenidos) throws MissingMandatoryValuesException{
        this.assertNonNull(interesesBrutos, "Gross interests is missing");
        double intNetos =interesesBrutos - interesesRetenidos;
        Log.d("ResultService","IntNetos: "+intNetos);
        return intNetos;
    }

    /**
     * Calculates gross from net and retained
     *
     * @param interesesNetos
     * @param interesesRetenidos
     */
    private double calculateInteresesBrutos(double interesesNetos, double interesesRetenidos) {
        double intBrutos =interesesNetos + interesesRetenidos;
        Log.d("ResultService", "IntBrutos: "+intBrutos);
        return intBrutos;
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
    public double calculateCota(double baseCalculo, Map<Double, Double> tramos) throws MissingMandatoryValuesException{
        this.assertNonNull(baseCalculo, "Calculus basis is missing");

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
        this.assertNonNull(contributorAge,"Contributor age is missing or invalid");

        // personal situation
        double gravamen = calculateGravamenByAge(contributorAge);
        gravamen += calculateGravamenByPersonalDisability(contributorDisabiliy, requiresHelpOrHaveReducedMobility);

        // children
        gravamen += calculateGravamentByChildren(descendentsQuantity);
        gravamen += calculateGravamenByChildrenMinor3yo(minors3yo);

        // ascendents
        gravamen += calculateGravamentByAscendents(ascendentsOlder65OrDisabledQuantity, ascendentsOlder75Quantity);

        Log.d("ResultService","Gravamen: "+gravamen);
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
     * @param cuotaLiquida
     * @param salarioBruto
     * @return
     * @throws MissingMandatoryValuesException if basis is invalid
     */
    private double calculateTasaRecomendada(double cuotaLiquida, double salarioBruto) throws MissingMandatoryValuesException {
        this.assertNonNull(salarioBruto, "Gross Salary is missing");
        double tasa = 100 * Math.abs(cuotaLiquida / salarioBruto);
        Log.d("ResultService", "TasaRecomendada: "+tasa);
        return tasa;
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
        this.assertNonNull(cuotaLiquida, "Liquid cuota has invalid value");
        this.assertNonNull(retenidoTotal,"Total retained has invalid value");

        double res =cuotaLiquida - retenidoTotal;
        Log.d("ResultService", "Resultado: "+res);
        return res;
    }

    /**
     * Calculates actual amount paid by user. It sums up retained money by salary and by interests
     *
     * @param retenido
     * @param interesesRetenidos
     * @return
     */
    private double calculateRetenidoTotal(double retenido, double interesesRetenidos) {
        double ret =retenido + interesesRetenidos;
        Log.d("ResultService","Retenido: "+ret);
        return ret;
    }

    /**
     * Calculates total amount user has to pay, summing up what has to pay to state and to autonomic government
     *
     * @param liquidoEstado
     * @param liquidoAutonomia
     * @return
     */
    private double calculateLiquidoTotal(double liquidoEstado, double liquidoAutonomia) {
        double liq =liquidoAutonomia + liquidoEstado;
        Log.d("ResultService","LiquidoTotal: "+liq);
        return liq;
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
     *
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

    /**
     * Checks the value is valid or else throws an error with given message
     * @param value value to check
     * @param message error message if not valid
     */
    private void assertNonNull(double value, String message) throws  MissingMandatoryValuesException{
        if(value <= 0){
            throw new MissingMandatoryValuesException(message);
        }
    }

}
