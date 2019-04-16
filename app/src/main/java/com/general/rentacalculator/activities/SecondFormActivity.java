package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.general.rentacalculator.R;
import com.general.rentacalculator.model.Renta;

public class SecondFormActivity extends AppCompatActivity {
    private Renta renta;
    private TextView prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        renta = (Renta) getIntent().getSerializableExtra("rentaModel");
        prueba = findViewById(R.id.textExample);
        prueba.setText("El salario bruto es: "+renta.getSalarioBruto()+", y la retención es: "+renta.getRetencion()+"%");
    }

    public void onSiguienteClick(View v) {
        Intent intentStep3 = new Intent(SecondFormActivity.this, ThirdFormActivity.class);
        intentStep3.putExtra("rentaModel", renta);
        startActivity(intentStep3);
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
}
