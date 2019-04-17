package com.general.rentacalculator.activities;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.general.rentacalculator.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkAllLayoutFields(){
        MainActivity activity = rule.getActivity();
        View salarioBruto = activity.findViewById(R.id.salarioBruto);
        assertThat(salarioBruto, instanceOf(EditText.class));
    }
}
