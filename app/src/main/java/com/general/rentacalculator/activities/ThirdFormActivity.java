package com.general.rentacalculator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.general.rentacalculator.R;

public class ThirdFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
    }

    public void onButtonResultado(View v){
        Intent Step4 = new Intent(ThirdFormActivity.this,   FourthFormActivity.class);
        startActivity(Step4);
    }

    public void onButtonAnterior(View v){
        Intent Step2 = new Intent(ThirdFormActivity.this,   SecondFormActivity.class);
        startActivity(Step2);
    }
}
