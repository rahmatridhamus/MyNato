package my.mynato.rahmatridham.mynato.Pemberitahuan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class pemberitahuanDetil extends AppCompatActivity {
    TextView judul, content;
    CheckBox isMengerti;
    Button okBut;
    boolean isRead = false;
    String idPemb = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan_detil);
        Intent intent = getIntent();

        idPemb = intent.getStringExtra("id_pemberitahuan");

        judul = (TextView) findViewById(R.id.judulpemberitahuan);
        content = (TextView) findViewById(R.id.contentDetilPemb);
        isMengerti = (CheckBox) findViewById(R.id.mengertiBox);
        okBut = (Button) findViewById(R.id.butSubmit);
        okBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRead) {
                    startActivity(new Intent(pemberitahuanDetil.this, Pemberitahuan.class));
                    finish();
                } else {
                    pushUpdatePemberitahuan(idPemb);
                }
            }
        });


        getDetilPemberitahuan(idPemb);
    }

    private void getDetilPemberitahuan(String idPemberitahuan) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(pemberitahuanDetil.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/detail/" + idPemberitahuan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                judul.setText(data.optString("title", ""));
                                content.setText(data.optString("content", ""));

                                if (data.optString("status", "").equals("READ")) {
                                    isMengerti.setEnabled(false);
                                    okBut.setText("Kembali");
                                    isRead = true;
                                }

                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(pemberitahuanDetil.this, "errorMessage: \n"+error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(pemberitahuanDetil.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(pemberitahuanDetil.this, "errorResponse: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = pemberitahuanDetil.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(pemberitahuanDetil.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pushUpdatePemberitahuan(String idPemberitahuan) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(pemberitahuanDetil.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/detail/" + idPemberitahuan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                if (data.optString("status", "").equals("READ")) {
                                    startActivity(new Intent(pemberitahuanDetil.this, Pemberitahuan.class));
                                    finish();
                                }
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(pemberitahuanDetil.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(pemberitahuanDetil.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(pemberitahuanDetil.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = pemberitahuanDetil.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("status", "READ");
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(pemberitahuanDetil.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
