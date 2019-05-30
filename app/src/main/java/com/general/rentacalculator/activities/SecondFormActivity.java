package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.general.rentacalculator.R;
import com.general.rentacalculator.enumerators.DisabilityEnum;
import com.general.rentacalculator.model.Renta;
import com.general.rentacalculator.services.ActivityUtils;

public class SecondFormActivity extends AppCompatActivity {

    private Renta renta;
    private EditText edad;
    private Spinner discapacidad;
    private CheckBox ayudaMovilidadReducida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        renta = (Renta) getIntent().getSerializableExtra("rentaModel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLayoutElements();
        setUpElements();


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

    /**
     * Associates local variables with fields and prepares fields
     */
    private void getLayoutElements() {
        edad = findViewById(R.id.edad);
        // TODO put enum values in spinner
        discapacidad = findViewById(R.id.discapacidad);
        ayudaMovilidadReducida = findViewById(R.id.ayudaMovilidadReducida);
    }

    /**
     * Actions when Next button clicked
     * @param v
     */
    public void onSiguienteClick(View v){
        if(fieldsValidation()){
            // populate data
            renta.setEdad(Integer.valueOf(edad.getText().toString()));
            renta.setDiscapacidad(ActivityUtils.getEnumByText(discapacidad.getSelectedItem().toString(),DisabilityEnum.class,this));
            renta.setAyuda(ayudaMovilidadReducida.isChecked());
            Log.d("SecondFormActivity", "Edad: "+ renta.getEdad() + ", Discapacidad: "+ renta.getDiscapacidad()+", Ayuda: "+renta.isAyuda());

            // preparing next screen
            Intent intentStep3 = new Intent(SecondFormActivity.this, ThirdFormActivity.class);
            intentStep3.putExtra("rentaModel",renta);
            startActivity(intentStep3);
        }
    }

    /**
     * Validates screen. Only age is mandatory here
     * @return
     */
    public boolean fieldsValidation(){

        if(TextUtils.isEmpty(edad.getText())){
            edad.setError("La edad es un campo obligatorio");
            return false;
        }
        return true;
    }


    private void setUpElements(){
        ActivityUtils.setUpSpinner(DisabilityEnum.class, discapacidad, this);
    }

}
