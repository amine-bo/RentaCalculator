package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.general.rentacalculator.R;

public class Step3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
    }

    public void onButtonResultado(View v){
        Intent Step4 = new Intent(Step3Activity.this,   Step4Activity.class);
        startActivity(Step4);
    }

    public void onButtonAnterior(View v){
        Intent Step2 = new Intent(Step3Activity.this,   Step2Activity.class);
        startActivity(Step2);
    }
}
