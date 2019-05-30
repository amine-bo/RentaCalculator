package com.general.rentacalculator.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.general.rentacalculator.interfaces.IdI18nKeyEnumQualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityUtils {

    public static <T extends Enum<T>> void setUpSpinner(Class<T> enumerator, Spinner spinner, Context context){
        List<String> arrayList = new ArrayList<>();

        if(Arrays.asList(enumerator.getInterfaces()).contains(IdI18nKeyEnumQualifier.class)) {
            for (T value : enumerator.getEnumConstants()) {
                int key =((IdI18nKeyEnumQualifier) value).getIdI18nKey();
                arrayList.add(context.getString(key));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList.toArray(new String[arrayList.size()]));
        spinner.setAdapter(adapter);

    }


    public static <T extends Enum<T>> T getEnumByText(String text, Class<T> enumerator, Context context){

        if(Arrays.asList(enumerator.getInterfaces()).contains(IdI18nKeyEnumQualifier.class)) {
            for (T value : enumerator.getEnumConstants()) {
                int key = ((IdI18nKeyEnumQualifier) value).getIdI18nKey();
                if(context.getString(key).equals(text)){
                    return value;
                }
            }
        }


        return null;
    }

}
