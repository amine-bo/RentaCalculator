package com.general.rentacalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.TooltipCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
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
import com.general.rentacalculator.services.RentaUtils;
import com.tooltip.Tooltip;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renta = new Renta();
        getLayoutElements();
        retencion.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        setAutocompleteComunidadAutonoma();
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
            renta.setSalarioBruto(Double.valueOf(salarioBruto.getText().toString()));
            renta.setRetencion(Float.valueOf(retencion.getText().toString()));
            // TODO fill renta obj with Contract type value
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

        try{
            ComunidadAutonomaEnum.valueOf(comunidadAutonoma.getText().toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            comunidadAutonoma.setError("La comunidad autónoma es un campo obligatorio");
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
