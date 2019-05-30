package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;

import com.general.rentacalculator.R;
import com.general.rentacalculator.model.Renta;

public class FourthFormActivity extends AppCompatActivity {

    private EditText deduccionesEstatales;
    private EditText deduccionesComunidad;
    private CheckBox movilidadGeografica;
    private EditText interesesBrutos;
    private EditText donaciones;
    private CheckBox mas3Anos;;
    private Renta renta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLayoutFields();

        renta = (Renta) getIntent().getSerializableExtra("rentaModel");
    }

    private void getLayoutFields() {
        deduccionesEstatales = findViewById(R.id.deduccionesEstatales);
        deduccionesComunidad = findViewById(R.id.deduccionesAutonomicas);
        movilidadGeografica = findViewById(R.id.movilidadGeografica);
        interesesBrutos = findViewById(R.id.interesesBrutos);
        donaciones = findViewById(R.id.donaciones);
        mas3Anos = findViewById(R.id.mas3Anos);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onSiguienteClick(View v) {
        // populate data
            renta.setDeduccionesEstado(getFloatValue(deduccionesEstatales));
            renta.setDeduccionesComunidad(getFloatValue(deduccionesComunidad));
            renta.setInteresesBrutos(getFloatValue(interesesBrutos));
            renta.setDonaciones(getFloatValue(donaciones));
            renta.setMovilidadGeografica(movilidadGeografica.isChecked());
            renta.setMas3Anos(mas3Anos.isChecked());
        Log.d("FourthFormActivity", "DeduccEstado: "+renta.getDeduccionesEstado()+", DeduccCA: "+renta.getDeduccionesComunidad()+", Intereses: "+renta.getInteresesBrutos()+", Donaciones: "+renta.getDonaciones()+", +3y: "+renta.isMas3Anos()+", MovilidadGeo: "+renta.isMovilidadGeografica());

            // next screen
            Intent intentStep5 = new Intent(FourthFormActivity.this, FifthFormActivity.class);
            intentStep5.putExtra("rentaModel", renta);
            startActivity(intentStep5);
    }

    private Float getFloatValue(EditText field){
        String value = field.getText().toString();
        if(TextUtils.isEmpty(value)){
            return 0f;
        }else{
            return Float.valueOf(value);
        }
    }

}
