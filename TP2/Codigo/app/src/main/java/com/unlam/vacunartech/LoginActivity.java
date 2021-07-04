package com.unlam.vacunartech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unlam.vacunartech.models.Login;
import com.unlam.vacunartech.services.RetrofitClient;
import com.unlam.vacunartech.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://so-unlam.net.ar/api/api/";
    EditText txtEmail;
    EditText txtPassword;
    Button btnLogin;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        Call call = RetrofitClient.getClient(BASE_URL)
                    .create(UserService.class)
                    .loginUser(email,password);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    //Login loginResponse = (Login) response.body();
                    //if(loginResponse.getSuccess().equals("true")){
                        Intent intent = new Intent(LoginActivity.this, MiVacunaActivity.class);
                        startActivity(intent);
                    //}
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
}
