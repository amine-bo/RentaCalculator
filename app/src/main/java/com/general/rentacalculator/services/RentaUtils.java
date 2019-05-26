package com.general.rentacalculator.services;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.general.rentacalculator.R;
import com.tooltip.Tooltip;

public class RentaUtils {

    public static void setTooltipText(String keyProperties, View view, Context context) {
        final String text = ConfigurationHolder.getProperty(keyProperties, context);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(20f)
                        .setGravity(Gravity.BOTTOM)
                        .setText(text);
                builder.show();
                return false;
            }
        });
    }
}
