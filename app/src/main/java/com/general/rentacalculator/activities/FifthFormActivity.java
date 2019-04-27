package com.general.rentacalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.general.rentacalculator.R;
import com.general.rentacalculator.model.Renta;
import com.general.rentacalculator.model.ResultRenta;
import com.general.rentacalculator.services.ConfigurationHolder;
import com.general.rentacalculator.services.ResultService;

import java.util.HashMap;
import java.util.Map;

public class FifthFormActivity extends AppCompatActivity {
    private Renta renta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        renta = (Renta) getIntent().getSerializableExtra("rentaModel");
        ResultRenta resultRenta = new ResultRenta();
        ResultService resultService = new ResultService();
        resultService.rentaResultCalculator(resultRenta, renta, this);
    }


}
