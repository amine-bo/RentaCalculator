package com.general.rentacalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.general.rentacalculator.R;
import com.general.rentacalculator.exceptions.MissingMandatoryValuesException;
import com.general.rentacalculator.model.Renta;
import com.general.rentacalculator.model.ResultRenta;
import com.general.rentacalculator.services.ConfigurationHolder;
import com.general.rentacalculator.services.ResultService;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class FifthFormActivity extends AppCompatActivity {
    private Renta renta;

    private TextView cuotaLiquida;
    private TextView retenidoTotal;
    private TextView donacionEfectiva;
    private TextView resultado;
    private TextView resultadoLetra;
    private TextView tasaReal;
    private TextView tasaAplicada;
    private TextView obligacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        renta = (Renta) getIntent().getSerializableExtra("rentaModel");
        getLayoutElements();
        ResultRenta resultRenta = new ResultRenta();
        ResultService resultService = new ResultService();
        try {
            resultService.rentaResultCalculator(resultRenta, renta, this);
            fillDataResults(resultRenta);
        } catch (MissingMandatoryValuesException e) {
            // TODO manage error
            Log.e("FifthFormActivity", "MissingMandatoryValuesException"+ e.getReason());
        }
    }

    private void getLayoutElements(){
        cuotaLiquida = findViewById(R.id.cuotaLiquida);
        retenidoTotal = findViewById(R.id.retenidoTotal);
        donacionEfectiva = findViewById(R.id.donacionEfectiva);
        resultado = findViewById(R.id.resultado);
        resultadoLetra = findViewById(R.id.resultadoLetra);
        tasaReal = findViewById(R.id.tasaReal);
        tasaAplicada = findViewById(R.id.tasaAplicada);
        obligacion = findViewById(R.id.obligacion);
    }

    private void fillDataResults(ResultRenta resultRenta){
        cuotaLiquida.setText(String.valueOf(resultRenta.getCuotaLiquida()));
        retenidoTotal.setText(String.valueOf(resultRenta.getRetenidoTotal()));
        donacionEfectiva.setText(String.valueOf(resultRenta.getDonacionEfectiva()));
        resultado.setText(String.valueOf(resultRenta.getResultado()));
        tasaReal.setText(String.valueOf(renta.getRetencion()));
        tasaAplicada.setText(String.valueOf(resultRenta.getTasaEfectiva()));

        if(resultRenta.getResultado()<0){
            resultadoLetra.setText(R.string.aDevolver);
            resultadoLetra.setTextColor(getResources().getColor(R.color.colorDevolver));
            resultado.setTextColor(getResources().getColor(R.color.colorDevolver));
        }else{
            resultadoLetra.setText(R.string.aPagar);
            resultadoLetra.setTextColor(getResources().getColor(R.color.colorPagar));
            resultado.setTextColor(getResources().getColor(R.color.colorPagar));
        }

        if(resultRenta.isObligatorio()){
             obligacion.setText(R.string.obligacion_obligatorio);
        }else if(resultRenta.isObligacionCondicionante()){
            obligacion.setText(R.string.obligacion_depende);
        }else if(resultRenta.isExento()){
            obligacion.setText(R.string.obligacion_exento);
        }

    }

}
