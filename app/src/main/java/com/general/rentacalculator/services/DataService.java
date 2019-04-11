package com.general.rentacalculator.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.general.rentacalculator.model.Renta;

/**
 * Servicio para recogida de datos de las diferentes pantallas
 */
public class DataService extends Service {

    public Renta getRenta() {
        return renta;
    }

    public void setRenta(Renta renta) {
        this.renta = renta;
    }

    private Renta renta;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setRenta(new Renta());
        return START_STICKY;
    }
}
