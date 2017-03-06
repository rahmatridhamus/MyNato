package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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
import com.example.rahmatridham.mynato.Model.SurveyModel;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailSurvey extends AppCompatActivity {
    TextView judul, tanggal, waktuPengerjaan, nilai;
    Button doIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_survey);

        judul = (TextView) findViewById(R.id.detSurJudul);
        tanggal = (TextView) findViewById(R.id.detSurTanggal);
        waktuPengerjaan = (TextView) findViewById(R.id.detSurDurasi);
        nilai = (TextView) findViewById(R.id.detSurNilai);
        doIt = (Button) findViewById(R.id.detSurButDo);

        Intent intent = getIntent();
        String id_survey = intent.getStringExtra("id_survey");

        getDetSurvey(id_survey);
    }

    public void getDetSurvey(String id_survey) {
        final ProgressDialog dialog = ProgressDialog.show(DetailSurvey.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Survey_Pemahaman/detail/" + id_survey,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                judul.setText(object.optString("nama_ujian", ""));
                                tanggal.setText(object.optString("tanggal_mulai", "") + " s/d " + object.optString("tanggal_berakhir", ""));
                                waktuPengerjaan.setText(object.optString("waktu_pengerjaan", "")+" Menit");
                                nilai.setText(object.optString("nilai", ""));
                                if (object.optString("hasil", "").equals("BELUM DILAKUKAN SURVEY")) {
                                    doIt.setText("Kerjakan Survey");
                                    doIt.setBackgroundResource(R.color.holo_green);
                                } else {
                                    doIt.setText("Closed");
                                    doIt.setBackgroundResource(R.color.red);
                                }
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(DetailSurvey.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DetailSurvey.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(DetailSurvey.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DetailSurvey.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DetailSurvey.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSurvey.this);
        requestQueue.add(stringRequest);
    }
}
