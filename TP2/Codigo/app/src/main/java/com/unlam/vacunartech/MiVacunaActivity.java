package com.unlam.vacunartech;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unlam.vacunartech.Receivers.NetworkChangeReceiver;
import com.unlam.vacunartech.models.EventResponse;
import com.unlam.vacunartech.models.SessionManager;
import com.unlam.vacunartech.services.EventService;
import com.unlam.vacunartech.services.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//SensorEventListener


public class MiVacunaActivity extends AppCompatActivity {
    private BroadcastReceiver networkReceiver;
    static TextView check_connection;
    SessionManager sessionManager;
    Button btnLogout;
    public float curX = 0, curY = 0, curZ = 0;
    private Display mDisplay;
    private SensorManager sm;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_vacuna);
        sessionManager = new SessionManager(this);

        String userToken = sessionManager.getAuthToken();

        networkReceiver = new NetworkChangeReceiver();
        sessionManager = new SessionManager(this);

        Log.i("isUserLoggedIn", String.valueOf(sessionManager.isUserLoggedIn()));

        check_connection=(TextView) findViewById(R.id.check_connection);
        networkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

        //Log.i("Token Saved", userToken);
        //Log.i("isUserLoggedIn", String.valueOf(sessionManager.isUserLoggedIn()));

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sessionManager.logoutUser();
            }
        });

        Call eventCall = RetrofitClient.getClient(getString(R.string.base_url))
                .create(EventService.class)
                .registerEvent("Bearer " + userToken,
                        "TEST", "Turno de Vacunación", "Consulta de Turno");
        eventCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                EventResponse eventResponse = (EventResponse) response.body();
                if(eventResponse.getSuccess().equals("true")) {
                    Toast.makeText(MiVacunaActivity.this, eventResponse.getEvent().getDescription(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MiVacunaActivity.this, "Hubo un error al Registrar Evento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(MiVacunaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToActivity2 (View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("Changed()", newConfig + "");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(MiVacunaActivity.this, "ORIENTATION LANDSCAPE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MiVacunaActivity.this, "ORIENTATION PORTRAIT", Toast.LENGTH_SHORT).show();
        }
    }

    public static void dialog(boolean value){

        if(value){
            check_connection.setText("Conectado a Internet !!!");
            check_connection.setBackgroundColor(Color.GREEN);
            check_connection.setTextColor(Color.WHITE);

            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    check_connection.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);
        }else {
            check_connection.setVisibility(View.VISIBLE);
            check_connection.setText("Sin conexion a interNet :(");
            check_connection.setBackgroundColor(Color.RED);
            check_connection.setTextColor(Color.WHITE);
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }


}
