package com.general.rentacalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.general.rentacalculator.R;
import com.general.rentacalculator.enumerators.ComunidadAutonomaEnum;
import com.general.rentacalculator.filter.InputFilterMinMax;
import com.general.rentacalculator.model.Renta;
import com.general.rentacalculator.services.ConfigurationHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText salarioBruto;
    private EditText retencion;
    private Spinner tipoContrato;
    private AutoCompleteTextView comunidadAutonoma;
    private Renta renta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renta = new Renta();
        getLayoutElements();
        retencion.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        setAutocompleteComunidadAutonoma();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void onSiguienteClick(View v) {
        boolean areFieldsValid = FieldsValidation();
        if(areFieldsValid) {
            renta.setSalarioBruto(Double.valueOf(salarioBruto.getText().toString()));
            renta.setRetencion(Float.valueOf(retencion.getText().toString()));
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
        return valid;
    }

    private void setAutocompleteComunidadAutonoma() {
        List<String> comunidadesAutonomasArray = new ArrayList<>();
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
}
