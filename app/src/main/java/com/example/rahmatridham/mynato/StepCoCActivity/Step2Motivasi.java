package com.example.rahmatridham.mynato.StepCoCActivity;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Model.CeritaMotivasi;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Step2Motivasi extends AppCompatActivity {
    Button lanjut;
    RelativeLayout screen;
    CheckBox boxDoIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2_motivasi);

        screen = (RelativeLayout) findViewById(R.id.activity_step1_visi_misi);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        boxDoIt = (CheckBox) findViewById(R.id.checkBoxMotivasi);
        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boxDoIt.isChecked()) {
                    Intent intent = new Intent(Step2Motivasi.this, Step3TataNilai.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Step2Motivasi.this, "Checklist untuk melanjutkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 2: Motivasi");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getMotivasi();
    }


    private void getMotivasi() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Cerita_Motivasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<CeritaMotivasi> ceritaMotivasiArrayList = new ArrayList<>();
                            ArrayList<String> games = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");

                                //array of cerita motivasi
                                JSONArray cerMot = data.getJSONArray("cerita_motivasi");
                                for (int i = 0; i < cerMot.length(); i++) {
                                    JSONObject object = cerMot.getJSONObject(i);
                                    CeritaMotivasi motivasi = new CeritaMotivasi(object.optString("id", ""), object.optString("title", ""), object.optString("file", ""), object.optString("keterangan", ""));
                                    ceritaMotivasiArrayList.add(motivasi);
                                }

                                //array of games
                                JSONArray gamesList = data.getJSONArray("games");
                                for (int i = 0; i < gamesList.length(); i++) {
                                    games.add(gamesList.getString(i));
                                }

                                Toast.makeText(Step2Motivasi.this, "Data berhasil di parse", Toast.LENGTH_SHORT).show();

                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step2Motivasi.this, error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step2Motivasi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step2Motivasi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step2Motivasi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step2Motivasi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
