package com.example.saktikrupafurniture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saktikrupafurniture.Helper.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private ProgressDialog progressDialog;
    EditText ETname,ETMobileNo,ETEmail,ETPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
        ETname = findViewById(R.id.name);
        ETMobileNo = findViewById(R.id.mobileNo);
        ETPass = findViewById(R.id.password);
    }

    public void SignUpClicked(View view){
        if(ETname.getText().toString().isEmpty() || ETMobileNo.getText().toString().isEmpty() || ETPass.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Fill All the Information",Toast.LENGTH_LONG).show();
        }
        else{
            if (!ETMobileNo.getText().toString().matches("[0-9]+")){
                Toast.makeText(getApplicationContext(),"Mobile Number is not a Character value",Toast.LENGTH_LONG).show();
            }
            else if(ETMobileNo.getText().toString().length() != 10){
                Toast.makeText(getApplicationContext(),"Mobile Number is Wrong",Toast.LENGTH_LONG).show();
            }
            else {
                if(isNetworkConnected()){
                    loadUsers();
                }
                else{
                    Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void ViewLoginClicked(View view){
        Intent iLogin = new Intent(this, Login.class);
        startActivity(iLogin);
    }

    private void loadUsers() {
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        StringRequest strreq = new StringRequest(Request.Method.POST, Constant.URL_STRING_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        // get response
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(Response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                progressDialog.dismiss();
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                Toast.makeText(getApplicationContext(),"You are Register Successfully",Toast.LENGTH_LONG).show();
                                Intent iLogin = new Intent(getApplicationContext(),Login.class);
                                startActivity(iLogin);
                                //alertDialog("You are Register Successfully");
                            } else {
                                progressDialog.dismiss();
                                alertDialog(obj.getString("EM"));
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            alertDialog("Please Contact Your Authorize Person" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                alertDialog("Please Contact Your Authorize Person" + e.getMessage());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("userName", ETname.getText().toString());
                params.put("mobileNo", ETMobileNo.getText().toString());
                params.put("email", "");
                params.put("password", ETPass.getText().toString());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strreq);
    }

    private void alertDialog(String txt) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(txt);
        dialog.setTitle("Dialog Box");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}