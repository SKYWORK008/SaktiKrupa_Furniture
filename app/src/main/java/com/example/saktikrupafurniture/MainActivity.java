package com.example.saktikrupafurniture;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saktikrupafurniture.Helper.SaveSharedPreference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    //qr code scanner object
    private IntentIntegrator qrScan;

    @SuppressLint("StaticFieldLeak")
    public static TextView scanTagText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanTagText = findViewById(R.id.ScanTag);
        //Intent i = new Intent(this, Login.class);
        //startActivity(i);
        qrScan = new IntentIntegrator(this);
    }

    public void LogoutClick(View view)
    {
        SaveSharedPreference.removeUserName(this);
        Intent iLogin = new Intent(this, Login.class);
        iLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        iLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iLogin);
        finish();
    }

    public void ScanClick(View view)
    {
        //initiating the qr code scan
        qrScan.initiateScan();
        //startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
        //startActivity(iScan);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (((IntentResult) result).getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {

                    scanTagText.setText(result.getContents());

                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}