package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Step1VisiMisi extends AppCompatActivity {

    RelativeLayout screen;
    Button lanjut;
    CheckBox cbVisi, cbMisi;
    TextView txtVisi, txtMisi;
    String vis = "";
    String mis = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1_visi_misi);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 1: Visi & Misi");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        screen = (RelativeLayout) findViewById(R.id.activity_step1_visi_misi);
        cbVisi = (CheckBox) findViewById(R.id.checkBoxMotivasi);
        cbMisi = (CheckBox) findViewById(R.id.checkBoxMisi);
        txtVisi = (TextView) findViewById(R.id.descVisi);
        txtMisi = (TextView) findViewById(R.id.descMisi);

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbMisi.isChecked() && cbVisi.isChecked()) {
                    SharedPreferences sharedPreferences = Step1VisiMisi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    pushVisiMisi(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
                } else {
                    Toast.makeText(Step1VisiMisi.this, "Checklist untuk melanjutkan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getVisiMisi();

    }

    private void getVisiMisi() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step1VisiMisi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Visi_Misi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                vis = data.optString("visi");
                                mis = data.optString("misi");
                                txtVisi.setText(vis);
                                txtMisi.setText(mis);
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step1VisiMisi.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step1VisiMisi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step1VisiMisi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step1VisiMisi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step1VisiMisi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void pushVisiMisi(String idGroup) {
        final ProgressDialog dialog = ProgressDialog.show(Step1VisiMisi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_visi_misi/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                Intent intent = new Intent(Step1VisiMisi.this, Step2Motivasi.class);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step1VisiMisi.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step1VisiMisi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step1VisiMisi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step1VisiMisi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("visi", "MELAKSANAKAN");
                    params.put("misi", "MELAKSANAKAN");
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step1VisiMisi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
