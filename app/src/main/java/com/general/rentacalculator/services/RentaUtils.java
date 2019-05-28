package com.general.rentacalculator.services;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.general.rentacalculator.R;
import com.tooltip.Tooltip;

public class RentaUtils {

    public static void setTooltipText(String keyProperties, View view, final Context context) {
        final String text = ConfigurationHolder.getProperty(keyProperties, context);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0,0);
                toast.show();
                return false;
            }
        });
    }
}
