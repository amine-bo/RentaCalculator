package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.general.rentacalculator.R;

public class Step4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);
    }

    public void onButtonAnterior(View v){
        Intent Step3 = new Intent(Step4Activity.this,   Step3Activity.class);
        startActivity(Step3);
    }
}
