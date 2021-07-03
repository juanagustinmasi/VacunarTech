package com.unlam.vacunartech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unlam.vacunartech.services.RetrofitClient;
import com.unlam.vacunartech.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://so-unlam.net.ar/api/api/";
    EditText txtName;
    EditText txtLastname;
    EditText txtEmail;
    EditText txtDni;
    EditText txtPassword;
    EditText txtCommission;
    EditText txtGroup;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = findViewById(R.id.txtName);
        txtLastname = findViewById(R.id.txtLastname);
        txtEmail = findViewById(R.id.txtEmail);
        txtDni = findViewById(R.id.txtDni);
        txtPassword = findViewById(R.id.txtPassword);
        txtCommission = findViewById(R.id.txtCommission);
        txtGroup = findViewById(R.id.txtGroup);


        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtName.getText().toString();
                String lastName = txtLastname.getText().toString();
                String email = txtEmail.getText().toString();
                Long dni = Long.parseLong(txtDni.getText().toString());
                String password = txtPassword.getText().toString();
                Integer commission = Integer.parseInt(txtCommission.getText().toString());
                Integer group = Integer.parseInt(txtGroup.getText().toString());
                //validateRegister
                doRegister(name, lastName, email, dni, password, commission, group);
            }
        });
    }

    private void doRegister(final String name, final String lastName, final String email, final Long dni,
                            final String password, final Integer commission, final Integer group){
        Call call = RetrofitClient.getClient(BASE_URL)
                .create(UserService.class)
                .registerUser(getString(R.string.env), name, lastName, email, dni, password, commission, group);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registración exitosa!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error de registración", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

