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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saktikrupafurniture.Helper.Constant;
import com.example.saktikrupafurniture.Helper.MakeAlertDialog;
import com.example.saktikrupafurniture.Helper.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private ProgressDialog progressDialog;
    TextView doNotHaveAccount;
    EditText editTextEmail,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       //This is For Check User Login Or Not
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        progressDialog = new ProgressDialog(this);
            if(SaveSharedPreference.getUserName(Login.this).length() > 0)
            {
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setMessage("Do you Want to Use Admin Mode?");
                dialog.setTitle("Dialog Box");
                dialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent iLogin = new Intent(getApplicationContext(), AdminPanel.class);
                                startActivity(iLogin);
                            }
                        });
                dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
                        Intent iMain = new Intent(getApplicationContext(), RewardActivity.class);
                        iMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iMain);
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }
            else{
                doNotHaveAccount = findViewById(R.id.doNotHaveAccount);
                editTextEmail = findViewById(R.id.editTextEmail);
                editTextPassword = findViewById(R.id.editTextPassword);
            }
        /*Intent iMain = new Intent(this, DowloadActivity.class);
        iMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iMain);*/
        ////CheckForUpdate();
    }

    public void LoginButton(View view){
        //Login Code First
        if(editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"MobileNo or Password is not Empty",Toast.LENGTH_LONG).show();
        }
        else{
             if(isNetworkConnected()){
                 loadUsers();
             }
             else{
                 Toast.makeText(getApplicationContext(),"No Internet Connection..",Toast.LENGTH_LONG).show();
             }
        }
    }

    private void loadUsers() {
        progressDialog.setMessage("Searching...");
        progressDialog.show();

        StringRequest strreq = new StringRequest(Request.Method.POST, Constant.URL_STRING_LOGIN,
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
                                SavePreference(userJson.optInt("id"),userJson.optString("username"));
                                Toast.makeText(getApplicationContext(),"Login Successfully!!..",Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Your UserName/Password is Wrong");

                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
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
            params.put("mobileNo", editTextEmail.getText().toString());
            params.put("password", editTextPassword.getText().toString());
            return params;
        }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strreq);
    }

    private void SavePreference(int id,String userName){
        SaveSharedPreference.setData(this,id,userName);
        Intent iMain = new Intent(this, RewardActivity.class);
        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        iMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iMain);
        finish();
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

    public void ViewRegisterClicked(View view){
        Intent iRegister = new Intent(this, Register.class);
        startActivity(iRegister);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}