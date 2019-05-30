package com.general.rentacalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.general.rentacalculator.R;
import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;
import com.general.rentacalculator.enumerators.ContractTypeEnum;
import com.general.rentacalculator.filter.InputFilterMinMax;
import com.general.rentacalculator.model.Renta;
import com.general.rentacalculator.services.ActivityUtils;
import com.general.rentacalculator.services.ConfigurationHolder;
import com.general.rentacalculator.services.RentaUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TOOLTIP_SALARIOBRUTO = "general.UC01.salarioBruto";
    private static final String TOOLTIP_RETENCION = "general.UC01.retencion";
    private static final String TOOLTIP_TIPOCONTRATO = "general.UC01.tipoContrato";
    private static final String TOOLTIP_COMUNIDADAUTONOMA = "general.UC01.ComunidadAutonoma";
    private EditText salarioBruto;
    private EditText retencion;
    private Spinner tipoContrato;
    private AutoCompleteTextView comunidadAutonoma;
    private Renta renta;
    List<String> comunidadesAutonomasArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renta = new Renta();
        getLayoutElements();
        setUpElements();

        final String text = "This is a test msg to bottom";
        RentaUtils.setTooltipText(TOOLTIP_SALARIOBRUTO, salarioBruto, this);
        RentaUtils.setTooltipText(TOOLTIP_RETENCION, retencion, this);
        RentaUtils.setTooltipText(TOOLTIP_TIPOCONTRATO, tipoContrato, this);
        RentaUtils.setTooltipText(TOOLTIP_COMUNIDADAUTONOMA, comunidadAutonoma, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void onSiguienteClick(View v) {
        boolean areFieldsValid = FieldsValidation();
        if(areFieldsValid) {
            // populate data
            renta.setSalarioBruto(Double.valueOf(salarioBruto.getText().toString()));
            renta.setRetencion(Float.valueOf(retencion.getText().toString()));
            renta.setTipoContrato(ActivityUtils.getEnumByText(tipoContrato.getSelectedItem().toString(), ContractTypeEnum.class, this));
            renta.setComunidadAutonoma(ComunidadAutonomaEnum.getByText(comunidadAutonoma.getText().toString(), this));
            Log.d("MainActivity","SalarioBruto: "+renta.getSalarioBruto()+", Retención: "+renta.getRetencion()+", TipoContrato: "+renta.getTipoContrato()+", ComAutonoma: "+renta.getComunidadAutonoma());

            // next screen
            Intent intentStep2 = new Intent(MainActivity.this, SecondFormActivity.class);
            intentStep2.putExtra("rentaModel", renta);
            startActivity(intentStep2);
        }
    }

    private boolean FieldsValidation() {
        boolean valid = true;
        if(TextUtils.isEmpty(salarioBruto.getText())){
            salarioBruto.setError("El salario bruto es un campo obligatorio");
            valid = false;
        }
        if(TextUtils.isEmpty(retencion.getText())){
            retencion.setError("La retención es un campo obligatorio");
            valid = false;
        }

        if(TextUtils.isEmpty(comunidadAutonoma.getText()) || !comunidadesAutonomasArray.contains(comunidadAutonoma.getText().toString())){
            comunidadAutonoma.setError("La comunidad autónoma es un campo obligatorio");
            valid = false;
        }

        return valid;
    }

    private void setAutocompleteComunidadAutonoma() {
        comunidadesAutonomasArray = new ArrayList<>();
        for(ComunidadAutonomaEnum value: ComunidadAutonomaEnum.values()){
            String i18nKey = ConfigurationHolder.getProperty(value.getI18nKey(), this);
            comunidadesAutonomasArray.add(i18nKey);
        }
        String[] comunidadesAutonomasI18nkey = new String[comunidadesAutonomasArray.size()];
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comunidadesAutonomasArray.toArray(comunidadesAutonomasI18nkey));
        comunidadAutonoma.setAdapter(adapter);
    }

    private void getLayoutElements() {
        comunidadAutonoma = findViewById(R.id.comunidadAutonoma);
        tipoContrato = findViewById(R.id.tipoContrato);
        retencion = findViewById(R.id.retencion);
        salarioBruto = findViewById(R.id.salarioBruto);
    }

    private void setUpElements(){
        retencion.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        setAutocompleteComunidadAutonoma();
        ActivityUtils.setUpSpinner(ContractTypeEnum.class, tipoContrato, this);
    }


}
