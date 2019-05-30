package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.general.rentacalculator.R;
import com.general.rentacalculator.model.Person;
import com.general.rentacalculator.model.Renta;

public class ThirdFormActivity extends AppCompatActivity {

    private Renta renta;
    private Spinner hijosMenores3Anos;
    private Spinner hijosMenores25Anos;
    private Spinner ascendientesMayores65Anos;
    private Spinner ascendientesMayores75Anos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        renta = (Renta) getIntent().getSerializableExtra("rentaModel");
        getLayoutElements();
    }

    public void onSiguienteClick(View v) {
        // populate data
        if(!TextUtils.isEmpty(hijosMenores3Anos.getSelectedItem().toString())){
            renta.setHijosMenores3Anos(Integer.valueOf(hijosMenores3Anos.getSelectedItem().toString()));
        }
        if(!TextUtils.isEmpty(hijosMenores25Anos.getSelectedItem().toString())){
            renta.setHijosMenores25Anos(Integer.valueOf(hijosMenores25Anos.getSelectedItem().toString()));
        }
        if(!TextUtils.isEmpty(ascendientesMayores65Anos.getSelectedItem().toString())){
            renta.setAscendientesMayores65Anos(Integer.valueOf(ascendientesMayores65Anos.getSelectedItem().toString()));
        }
        if(!TextUtils.isEmpty(ascendientesMayores75Anos.getSelectedItem().toString())){
            renta.setAscendientesMayores75Anos(Integer.valueOf(ascendientesMayores75Anos.getSelectedItem().toString()));
        }
        Log.d("ThirdFormActivity", "Hijos<3y: "+renta.getHijosMenores3Anos()+", Hijos<25y: "+renta.getHijosMenores25Anos()+", Ascend>65y: "+renta.getAscendientesMayores65Anos()+", Ascend>75y: "+renta.getAscendientesMayores75Anos());

        // next screen
        Intent intentStep4 = new Intent(ThirdFormActivity.this, FourthFormActivity.class);
        intentStep4.putExtra("rentaModel", renta);
        startActivity(intentStep4);
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

    private void getLayoutElements() {
        hijosMenores3Anos = findViewById(R.id.hijosMenores3Anos);
        hijosMenores25Anos = findViewById(R.id.hijosMenores25AnosDiscapacidad);
        ascendientesMayores65Anos = findViewById(R.id.ascendientesMayores65AnosDiscapacidad);
        ascendientesMayores75Anos = findViewById(R.id.ascendientesMayores75Anos);
    }
}
