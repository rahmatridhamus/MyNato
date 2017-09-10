package my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;
import my.mynato.rahmatridham.mynato.StepCoCActivity.Step5Thematik;

public class Step2MotivasiAnggota extends AppCompatActivity {
    TextView motivasi, games, motivSeb, gemSeb;
    Button lanjut, filemot, filegem;
    String urlmot, urlgem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2_motivasi_anggota);

        motivasi = (TextView) findViewById(R.id.descVisi);
        games = (TextView) findViewById(R.id.descMisi);
        motivSeb = (TextView) findViewById(R.id.strMotivSebelum);
        gemSeb = (TextView) findViewById(R.id.strMotivSebelumsc);


        filemot = (Button) findViewById(R.id.butFileMotivasi);
        filemot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(urlmot); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        filegem = (Button) findViewById(R.id.butFileGames);
        filegem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(urlgem); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Step2MotivasiAnggota.this, Step3TataNilaiAnggota.class));
                finish();
            }
        });
        SharedPreferences sharedPreferences = Step2MotivasiAnggota.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        getMotivasi(sharedPreferences.getString(Config.IDCOCACTIVITY_SHARED_PREF, ""));
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Step2MotivasiAnggota.this, Step1VisiMisiAnggota.class));
        finish();
    }

    private void getMotivasi(String idCOCActivity) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step2MotivasiAnggota.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Anggota_CoC/cerita_motivasi/" + idCOCActivity, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(Step2MotivasiAnggota.this, response, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONObject data = jsonObject.getJSONObject("data");
//                        JSONObject eksisting = data.getJSONObject("eksisting");
                        if (data.optString("id_games", "").equals("")) {
                            Toast.makeText(Step2MotivasiAnggota.this, "Admin melum menyelesaikan halaman ini", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Step2MotivasiAnggota.this, Step1VisiMisiAnggota.class));
                            finish();
                        } else {
                            urlmot = data.optString("file_cerita_motivasi", "");
                            motivasi.setText(data.optString("title_cerita_motivasi", ""));

                            urlgem = data.optString("file_games", "");
                            games.setText(data.optString("title_games", ""));
                        }

                        JSONObject history = jsonObject.getJSONObject("history");

                        motivSeb.setText("Pertemuan sebelumnya: " + history.optString("title_cerita_motivasi"));
                        gemSeb.setText("Pertemuan sebelumnya: " + history.optString("title_games"));


                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(Step2MotivasiAnggota.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Step2MotivasiAnggota.this, Step1VisiMisiAnggota.class));
                        finish();
                        Snackbar snackbars = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbars.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step2MotivasiAnggota.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mendapatkan data", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
//                        Toast.makeText(Step2MotivasiAnggota.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mendapatkan data: \n" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step2MotivasiAnggota.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
//                    Toast.makeText(CocVerified.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error param: \n" + e.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                    return params;
                }
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
