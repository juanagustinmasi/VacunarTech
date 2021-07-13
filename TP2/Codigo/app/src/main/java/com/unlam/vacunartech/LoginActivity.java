package com.unlam.vacunartech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unlam.vacunartech.Receivers.NetworkChangeReceiver;
import com.unlam.vacunartech.models.EventResponse;
import com.unlam.vacunartech.models.LoginResponse;
import com.unlam.vacunartech.models.SessionManager;
import com.unlam.vacunartech.services.EventService;
import com.unlam.vacunartech.services.RetrofitClient;
import com.unlam.vacunartech.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private BroadcastReceiver networkReceiver;
    static TextView check_connection;
    SessionManager sessionManager;
    EditText txtEmail;
    EditText txtPassword;
    Button btnLogin;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        networkReceiver = new NetworkChangeReceiver();
        sessionManager = new SessionManager(this);

        Log.i("isUserLoggedIn", String.valueOf(sessionManager.isUserLoggedIn()));

        check_connection=(TextView) findViewById(R.id.check_connection);
        networkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                //validateLogin
                doLogin(username, password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void doLogin(final String email,final String password){

        Call loginCall = RetrofitClient.getClient(getString(R.string.base_url))
                    .create(UserService.class)
                    .loginUser(email,password);

        loginCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){

                    LoginResponse loginResponse = (LoginResponse) response.body();
                    String userToken = loginResponse.getToken();
                    Log.i("Informacion", userToken);

                    sessionManager.saveAuthToken(userToken);

                    Log.i("Token Saved", userToken);


                    Call eventCall = RetrofitClient.getClient(getString(R.string.base_url))
                            .create(EventService.class)
                            .registerEvent("Bearer " + loginResponse.getToken(),
                                    "TEST", "Login", "Usuario logueado correctamente");
                    eventCall.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            EventResponse eventResponse = (EventResponse) response.body();
                            if(eventResponse.getSuccess().equals("true")) {
                                Toast.makeText(LoginActivity.this, eventResponse.getEvent().getDescription(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MiVacunaActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Hubo un error al Registrar Evento", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            check_connection.setText("Sin conexion a internet :(");
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
