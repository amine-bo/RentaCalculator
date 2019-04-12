package com.general.rentacalculator.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.general.rentacalculator.R;
import com.general.rentacalculator.services.DataService;
import com.general.rentacalculator.services.ResultService;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

        // Duración en milisegundos del mantenimiento de la splash screen
        private static final long SPLASH_SCREEN_RETARDO = 2500;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Poner orientación vertical
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
            // Esconder la barra de título
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.activity_splash_screen);

            //Iniciar el servicio de recogida de datos de las pantallas
            startService(new Intent(this, DataService.class));

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    // Iniciar la activity de menu
                    Intent menuIntent = new Intent().setClass(
                            SplashScreenActivity.this, MainActivity.class);
                    startActivity(menuIntent);

                    // Cerrar la actividad para que el usuario no pueda volver pulsando el botón atrás
                    finish();
                }
            };

            // Establecimiento del timer para retrasar la actividad 5 segundos
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_RETARDO);
        }
}
