package com.unlam.vacunartech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationActivity extends AppCompatActivity {

    String verificationCode;
    EditText txtVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        verificationCode = intent.getStringExtra("EXTRA_VERIFICATION_CODE");
        txtVerificationCode   = findViewById(R.id.txtVerificationCode);
    }

    public void onButtonClickOk(View view) {
        if(verificationCode.equals(txtVerificationCode.getText().toString())) {
            Toast.makeText(this, "Codigó verificado exitosamente!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Codigó inválido, intentelo nuevamente!", Toast.LENGTH_LONG).show();
        }
    }
}
