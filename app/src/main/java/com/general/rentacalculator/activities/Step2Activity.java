package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.general.rentacalculator.R;

public class Step2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
    }

    public void onButtonSiguiente(View v){
        Intent Step3 = new Intent(Step2Activity.this,   Step3Activity.class);
        startActivity(Step3);
    }

    public void onButtonAnterior(View v){
        Intent Step1 = new Intent(Step2Activity.this,   MainActivity.class);
        startActivity(Step1);
    }
}
