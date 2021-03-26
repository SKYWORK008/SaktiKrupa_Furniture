package com.example.saktikrupafurniture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saktikrupafurniture.Helper.Constant;
import com.example.saktikrupafurniture.Helper.MakeAlertDialog;
import com.example.saktikrupafurniture.Helper.SaveSharedPreference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class RewardActivity extends AppCompatActivity {

    //qr code scanner object
    private IntentIntegrator qrScan;
    private TextView rewardPointTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(true);
        qrScan.setPrompt("");
        rewardPointTextView = findViewById(R.id.rewardPoint);
        SumOfReward();
        CheckForUpdate();
    }

    public void ScanClick(View view)
    {
        //initiating the qr code scan
        qrScan.initiateScan();
    }

    public void AboutShopButton(View view)
    {
        Intent iShopInfoActivity = new Intent(this, ShopInfoActivity.class);
        startActivity(iShopInfoActivity);
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

                    String rNo="";
                    int rPoint = 0;

                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    String lines[] = result.getContents().split("\\r?\\n");

                    List<String> result1 = new ArrayList<>();

                    StringTokenizer st = new StringTokenizer(lines[0],":");

                    while (st.hasMoreTokens())
                    {
                        result1.add(st.nextToken().trim());
                    }

                    st = new StringTokenizer(lines[1],":");

                    while (st.hasMoreTokens())
                    {
                        result1.add(st.nextToken().trim());
                    }

                    int i = 0;
                    for (String s : result1) {
                        i++;
                        if(i == 2){
                            rNo = s.trim();
                        }
                        if(i == 4){
                            rPoint = Integer.parseInt(s.trim());
                        }
                    }
                    StoreReward(rNo,rPoint);
                    SumOfReward();

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

    public void StoreReward(final String rNo, final int rPoint){
        StringRequest strreq = new StringRequest(Request.Method.POST, Constant.URL_STRING_REWARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        // get response
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(Response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //getting the user from the response
                                //rewardPointTextView.setText(obj.getString("result"));
                            } else {
                                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+e.getMessage());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customerId", String.valueOf(SaveSharedPreference.getId(RewardActivity.this)));
                params.put("rewardNo", rNo);
                params.put("rewardPoint", String.valueOf(rPoint));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strreq);
    }

    public void SumOfReward(){
        StringRequest strreq = new StringRequest(Request.Method.POST,Constant.URL_STRING_REWARD_SUM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                            JSONObject obj = new JSONObject(Response);

                            if (!obj.getBoolean("error")) {
                                if(obj.getString("result") == "null"){
                                    rewardPointTextView.setText("0");
                                }else{
                                    rewardPointTextView.setText(obj.getString("result"));
                                }
                            } else {
                                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+e.getMessage());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customerId", String.valueOf(SaveSharedPreference.getId(RewardActivity.this)));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strreq);
    }

    private void alertDialog(String txt) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(txt);
        dialog.setTitle("Dialog Box");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void CheckForUpdate() {

        PackageInfo pinfo = null;
        try
        {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final int vcode = pinfo.versionCode;
            final String vname = pinfo.versionName;
            System.out.println("Please Contact Your Authorize Person");
            //progressDialog.setMessage("Check For Update...");
            //progressDialog.show();
            StringRequest strreq = new StringRequest(Request.Method.POST,Constant.URL_STRING_CHECK_UPDATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String Response) {
                            // get response
                            try {
                                System.out.println("Please Contact Your Authorize Person");
                                //converting response to json object
                                JSONObject obj = new JSONObject(Response);
                                //if no error in response
                                if (obj.getBoolean("error")) {
                                    OpenDowloadPopUp(obj.getString("link"));
                                }
                                else
                                {
                                    NormalFlow();
                                }
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                //alertDialog("Please Contact Your Authorize Person");
                                System.out.println("Please Contact Your Authorize Person");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    //alertDialog("Please Contact Your Authorize Person");
                }
            }){
                @Override
                public Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("aName", String.valueOf(vcode));
                    params.put("aCode", vname);
                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(strreq);

        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }
    }

    public void OpenDowloadPopUp(String link){
        Intent iDownload = new Intent(this, DowloadActivity.class);
        iDownload.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iDownload.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        iDownload.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        iDownload.putExtra("link",link);
        startActivity(iDownload);
    }

    public void NormalFlow(){
        System.out.println("Normal Flow.....");
    }
}