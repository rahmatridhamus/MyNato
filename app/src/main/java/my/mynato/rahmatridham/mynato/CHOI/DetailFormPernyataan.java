package my.mynato.rahmatridham.mynato.CHOI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.ChoiDetailFormsModel;
import my.mynato.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;
import my.mynato.rahmatridham.mynato.Pemberitahuan.pemberitahuanDetil;
import my.mynato.rahmatridham.mynato.R;

public class DetailFormPernyataan extends AppCompatActivity {
    WebView webView;
    CheckBox mengerti;
    Button submit;
    String id_formulir, id_aktivasi_choi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_form_pernyataan);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id_formulir = (String) b.get("id_formulir");
            id_aktivasi_choi = (String) b.get("id_aktivasi_choi");
        }

        webView = (WebView) findViewById(R.id.webviewPernyataan);
        webView.getSettings().setJavaScriptEnabled(true);
        mengerti = (CheckBox) findViewById(R.id.checkBox);
        submit = (Button) findViewById(R.id.buttonSummition);

        mengerti.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mengerti.isChecked()) {
                    pushUpdateForm(id_aktivasi_choi, id_formulir);
                } else {
                    Toast.makeText(v.getContext(), "Checklist untuk melakukan submit", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getSuratPernyataan(id_aktivasi_choi, id_formulir);

    }

    private void getSuratPernyataan(String id_aktivasi_choi, String id_formulir) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(DetailFormPernyataan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Choi/detail_formulir/" + id_aktivasi_choi + "/" + id_formulir, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        if (data.optString("status", "null").equals("PENDING")) {
                            mengerti.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                        } else {
                            mengerti.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
                        }

                        webView.loadDataWithBaseURL(null, data.optString("content", "null"), "text/html", "utf-8", null);

                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(DetailFormPernyataan.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(DetailFormPernyataan.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
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
//                        Toast.makeText(DetailFormPernyataan.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data ", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DetailFormPernyataan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DetailFormPernyataan.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return params;
                }
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pushUpdateForm(String id_aktivasi_choi, String id_formulir) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(DetailFormPernyataan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Choi/submit_ttd/" + id_aktivasi_choi + "/" + id_formulir,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(DetailFormPernyataan.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                startActivity(new Intent(DetailFormPernyataan.this, SuratPernyataan.class));
                                finish();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(DetailFormPernyataan.this, error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
//                            Toast.makeText(DetailFormPernyataan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
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
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DetailFormPernyataan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("status", "ACC");
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DetailFormPernyataan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
