package com.example.saktikrupafurniture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DowloadActivity extends AppCompatActivity {

    private DownloadManager downloadManager;
    private long downloadReference;
    String link = "";
    long queId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dowload);
        getSupportActionBar().hide();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        // create the get Intent object
        Intent intent = getIntent();
        link = intent.getStringExtra("link");

    }

    public void Dowload_Click(View v){
        Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        /*downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://drive.google.com/file/d/19Yg89xg41L20W7Bp5Ib9DEaCfkXS3dgE/view?usp=sharing"));
        queId = downloadManager.enqueue(request);
        */
    }
}