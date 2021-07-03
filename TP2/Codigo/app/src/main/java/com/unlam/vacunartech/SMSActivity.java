package com.unlam.vacunartech;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class SMSActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private EditText txtNumber;
    private Button btnSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        txtNumber = findViewById(R.id.txtNumber);
        btnSms = findViewById(R.id.btnSend);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                        Log.d("permission", "permission denied to SEND_SMS - requesting it");
                        String[] permissions = {Manifest.permission.SEND_SMS};
                        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                    }
                }
                Random random = new Random();
                String verificationCode = String.format("%04d", random.nextInt(10000));
                String smsMessage = "Código de inicio de sesión de VacunarTech: " + verificationCode;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(txtNumber.getText().toString(),null,smsMessage,null,null);
                Toast.makeText(SMSActivity.this, "SMS enviado exitosamente", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SMSActivity.this,VerificationActivity.class);
                intent.putExtra("EXTRA_VERIFICATION_CODE", verificationCode.toString());
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(SMSActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
