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
import com.example.rahmatridham.mynato.Model.TataNilai;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Step3TataNilai extends AppCompatActivity {
    Button lanjut;
    RelativeLayout screen;
    CheckBox boxTataNilai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3_tata_nilai);

        screen = (RelativeLayout) findViewById(R.id.activity_step1_visi_misi);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        boxTataNilai = (CheckBox) findViewById(R.id.checkBoxLanjutkanTataNilai);
        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boxTataNilai.isChecked()) {
                    Intent intent = new Intent(Step3TataNilai.this, Step4DoAndDont.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Step3TataNilai.this, "Checklist untuk melanjutkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 3: Tata Nilai");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        getTataNilai();
    }

    private void getTataNilai() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Tata_Nilai",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<TataNilai> tataNilaiArrayList = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                //array of cerita TataNilai
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    TataNilai tataNilai = new TataNilai(object.optString("id_tata_nilai", ""), object.optString("title", ""), object.optString("content", ""));
                                    tataNilaiArrayList.add(tataNilai);
                                }

                                Toast.makeText(Step3TataNilai.this, "Data berhasil di parse", Toast.LENGTH_SHORT).show();

                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step3TataNilai.this, error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step3TataNilai.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step3TataNilai.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step3TataNilai.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step3TataNilai.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
