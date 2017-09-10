package my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import my.mynato.rahmatridham.mynato.LandingPage;
import my.mynato.rahmatridham.mynato.R;

import static my.mynato.rahmatridham.mynato.R.id.parent;

public class Step5ThematikAnggota extends AppCompatActivity {
    TextView thematik, hist, cont;
    Button lanjut, buka;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step5_thematik_anggota);

        thematik = (TextView) findViewById(R.id.descVisi);
        cont = (TextView) findViewById(R.id.descVissi);
        hist = (TextView) findViewById(R.id.strMotivSebelum);

        buka = (Button) findViewById(R.id.bukfil);
        buka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.thematik_weburl);

                WebView wv = (WebView) dialog.findViewById(R.id.webthematik);
                wv.loadUrl(url);
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });

                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            dialog.dismiss();
                        }
                        return false;
                    }

                });
                dialog.show();
            }
        });

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Step5ThematikAnggota.this, LandingPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        SharedPreferences sharedPreferences = Step5ThematikAnggota.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getThematik(sharedPreferences.getString(Config.IDCOCACTIVITY_SHARED_PREF, ""));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Step5ThematikAnggota.this, Step4DoAndDontAnggota.class));
        finish();
    }

    private void getThematik(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step5ThematikAnggota.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Anggota_CoC/thematik/" + idGroup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONObject data = jsonObject.getJSONObject("data");
//                        JSONObject eksisting = data.getJSONObject("eksisting");
                        if (data.optString("id_content_thematik", "").equals("")) {
//                            Toast.makeText(Step5ThematikAnggota.this, "Admin melum menyelesaikan halaman ini", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Step5ThematikAnggota.this, Step4DoAndDontAnggota.class));
                            finish();
                        } else {

                            thematik.setText(data.optString("title", ""));
                            cont.setText(data.optString("sub_title", ""));
                            url = data.optString("url", "");

                            JSONObject history = jsonObject.getJSONObject("history");
                            hist.setText("Pertemuan sebelumnya: " + history.optString("title", ""));
                        }
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(CocVerified.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Step5ThematikAnggota.this, Step4DoAndDontAnggota.class));
                        finish();
                        Snackbar snackbars = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbars.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step5ThematikAnggota.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(CocVerified.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error getting response: \n" + error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step5ThematikAnggota.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

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
