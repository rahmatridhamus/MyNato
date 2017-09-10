package my.mynato.rahmatridham.mynato.Survey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailSurvey extends AppCompatActivity {
    TextView judul, tanggal, waktuPengerjaan, nilai;
    Button doIt;
    String id_survey, ket_survey, id_soal_survey;
    int durasiSurvey = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(my.mynato.rahmatridham.mynato.R.layout.activity_detail_survey);

        Intent intent = getIntent();
        id_survey = intent.getStringExtra("id_survey");
        ket_survey = intent.getStringExtra("keterangan_survey");

        judul = (TextView) findViewById(my.mynato.rahmatridham.mynato.R.id.detSurJudul);
        tanggal = (TextView) findViewById(my.mynato.rahmatridham.mynato.R.id.detSurTanggal);
        waktuPengerjaan = (TextView) findViewById(my.mynato.rahmatridham.mynato.R.id.detSurDurasi);
        nilai = (TextView) findViewById(my.mynato.rahmatridham.mynato.R.id.detSurNilai);
        doIt = (Button) findViewById(my.mynato.rahmatridham.mynato.R.id.detSurButDo);
        doIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ket_survey.equals("Closed")) {
                    startActivity(new Intent(DetailSurvey.this, Survey.class));
                    finish();
                } else {
                    Intent intent = new Intent(DetailSurvey.this, DoSurvey.class);
                    intent.putExtra("nama_ujian", judul.getText().toString());
                    intent.putExtra("durasiSurvey", durasiSurvey);
                    intent.putExtra("id_survey", id_survey);
                    intent.putExtra("id_soal_survey", id_soal_survey);
                    startActivity(intent);
                    finish();
                }

            }
        });


        Toast.makeText(this, ket_survey, Toast.LENGTH_SHORT).show();
        if (ket_survey.equals("Open")) {
            doIt.setText("Kerjakan Survey");
            doIt.setBackgroundResource(R.color.greenButton);
        } else {
            doIt.setText("Closed");
            doIt.setBackgroundResource(R.color.red);

        }
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
                                waktuPengerjaan.setText(object.optString("waktu_pengerjaan", "") + " Menit");
                                durasiSurvey = Integer.valueOf(object.optString("waktu_pengerjaan", ""));
                                id_soal_survey = object.optString("id_soal_survey", "");
                                nilai.setText(object.optString("nilai", ""));

//                                if (object.optString("hasil", "").equals("BELUM DILAKUKAN SURVEY")) {
//                                    doIt.setText("Kerjakan Survey");
//                                    doIt.setBackgroundResource(R.color.greenButton);
//                                } else {
//                                    doIt.setText("Closed");
//                                    doIt.setBackgroundResource(my.mynato.rahmatridham.mynato.R.color.red);
//                                }

                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                                startActivity(new Intent(DetailSurvey.this, Survey.class));
                                finish();
                            }
                        } catch (Exception e) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();
                            startActivity(new Intent(DetailSurvey.this, Survey.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                        snackbar.show();
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
