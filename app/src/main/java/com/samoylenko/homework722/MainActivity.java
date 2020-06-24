package com.samoylenko.homework722;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_SEND_SMS = 10;
    public static final int REQUEST_CODE_PERMISSION_CALL = 11;
    public static final String PERMISSION_SMS = "SMS";
    public static final String PERMISSION_CALL = "CALL";

    public EditText textNumber;
    public EditText textSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        Button btnCall = findViewById(R.id.button_call);
        Button btnSms = findViewById(R.id.button_send_sms);
        textNumber = findViewById(R.id.number_tel);
        textSms = findViewById(R.id.text_sms);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions(PERMISSION_SMS)) {
                    call();
                } else {
                    //просим разрешение
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_PERMISSION_CALL);
                }
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions(PERMISSION_CALL)) {
                    sendSms();
                } else {
                    //просим разрешение
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_PERMISSION_SEND_SMS);
                }
            }
        });
    }

    private boolean hasPermissions(String perm) {
        if (perm.equals(PERMISSION_CALL)) {
            int permissionsStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            return permissionsStatus == PackageManager.PERMISSION_GRANTED;
        } else if (perm.equals(PERMISSION_SMS)) {
            int permissionsStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            return permissionsStatus == PackageManager.PERMISSION_GRANTED;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(MainActivity.this, R.string.permission_no_call
                            , Toast.LENGTH_LONG)
                            .show();
                }
                break;
            }
            case REQUEST_CODE_PERMISSION_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    Toast.makeText(MainActivity.this, R.string.permission_no_sms
                            , Toast.LENGTH_LONG)
                            .show();
                }
                break;
            }
            default:
                break;
        }
    }

    public void sendSms() {
        SmsManager smgr = SmsManager.getDefault();
        String telNumber = textNumber.getText().toString();
        String sms = textSms.getText().toString();
        if (!sms.isEmpty() && !telNumber.isEmpty()){
            smgr.sendTextMessage(telNumber, null, sms, null, null);
            Toast.makeText(MainActivity.this, R.string.sms_sending
                    , Toast.LENGTH_LONG)
                    .show();
        }else{
            Toast.makeText(MainActivity.this, R.string.no_text_or_number
                    , Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void call() {
        String telNumber = textNumber.getText().toString();
        if (!telNumber.isEmpty()){
            Uri uri = Uri.parse("tel:" + telNumber);
            Intent call = new Intent(Intent.ACTION_DIAL, uri);
            if (call.resolveActivity(getPackageManager()) != null) {
                startActivity(call);
            } else {
                Toast.makeText(this, R.string.no_app_to_call
                        , Toast.LENGTH_LONG)
                        .show();
            }
        }else{
            Toast.makeText(this, R.string.no_number_for_call
                    , Toast.LENGTH_LONG)
                    .show();
        }
    }
}
