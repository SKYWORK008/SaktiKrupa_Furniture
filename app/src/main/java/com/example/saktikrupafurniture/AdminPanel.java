package com.example.saktikrupafurniture;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saktikrupafurniture.Helper.Constant;
import com.example.saktikrupafurniture.Helper.MakeAlertDialog;
import com.example.saktikrupafurniture.Model.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminPanel extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ProgressDialog progressDialog;
    private ArrayList<CustomerModel> customerList;
    private Spinner customerDataSpinner;
    private TextView rewardPointTextView;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        customerDataSpinner = findViewById(R.id.customer_Spinner);
        // Spinner click listener
        customerDataSpinner.setOnItemSelectedListener(this);
        customerList = new ArrayList<CustomerModel>();
        rewardPointTextView = findViewById(R.id.rewardPoint);
        progressDialog = new ProgressDialog(this);
        loadUsers();
    }

    private ArrayList<CustomerModel> refreshProductList(JSONObject jsonRootObject) throws JSONException {
        //clearing previous heroes
        customerList.clear();
        JSONArray heroes = jsonRootObject.optJSONArray("result");
        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);

            CustomerModel temp = new CustomerModel();
            temp.setId(obj.optInt("id"));
            temp.setUserName(obj.optString("userName"));
            customerList.add(temp);
        }
        return customerList;
    }

    private void loadUsers() {
        progressDialog.setMessage("Register User...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_STRING_CHECK_CUSTOMERDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject array = new JSONObject(response);
                            SetSpinnerData(refreshProductList(array));

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void SetSpinnerData(ArrayList<CustomerModel> arrayList){
        List<String> col = new ArrayList<String>();
        for(CustomerModel si : arrayList ){
            col.add(si.getUserName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, col);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        customerDataSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        // On selecting a spinner item
        //String label = parent.getItemAtPosition(position).toString();

        customerId = customerList.get(position).getId();
        SumOfReward();
        //Toast.makeText(parent.getContext(), "You selected: " + customerList.get(position).getId() + ", UserName:" + customerList.get(position).getUserName(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
                                }
                                else{
                                    rewardPointTextView.setText(obj.getString("result"));
                                }
                            }
                            else {
                                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information",obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person \n"+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person");
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customerId", String.valueOf(customerId));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strreq);
    }

    public void DeleteClicked(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you Want to Delete Record?");
        dialog.setTitle("Dialog Box");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        DeleteReward();
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void DeleteReward(){
        StringRequest strreq = new StringRequest(Request.Method.POST,Constant.URL_STRING_CHECK_DELETEREWARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        try {
                            JSONObject obj = new JSONObject(Response);
                            MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Reward Delete Successfully");
                            SumOfReward();

                        } catch (JSONException e) {
                            MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person\n"+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                MakeAlertDialog.createAlertDialogWithOKButton(getApplicationContext(),"Information","Please Contact Your Authorize Person");
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customerId", String.valueOf(customerId));
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(strreq);
    }
}