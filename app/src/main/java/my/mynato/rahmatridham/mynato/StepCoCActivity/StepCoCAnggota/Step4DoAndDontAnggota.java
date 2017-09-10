package my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

public class Step4DoAndDontAnggota extends AppCompatActivity {
    TextView does, dont, hist;
    Button lanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4_do_and_dont_anggota);

        does = (TextView) findViewById(R.id.descDo);
        dont = (TextView) findViewById(R.id.descDont);
        hist = (TextView) findViewById(R.id.strMotivSebelum);

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Step4DoAndDontAnggota.this, Step5ThematikAnggota.class));
                finish();

            }
        });
        SharedPreferences sharedPreferences = Step4DoAndDontAnggota.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getDoDont(sharedPreferences.getString(Config.IDCOCACTIVITY_SHARED_PREF, ""));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Step4DoAndDontAnggota.this, Step3TataNilaiAnggota.class));
        finish();
    }

    private void getDoDont(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step4DoAndDontAnggota.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Anggota_CoC/do_and_dont/" + idGroup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONObject data = jsonObject.getJSONObject("data");
//                        JSONObject eksisting = data.getJSONObject("eksisting");
                        if (data.optString("id_do_and_dont", "").equals("")) {
//                            Toast.makeText(Step4DoAndDontAnggota.this, "Admin melum menyelesaikan halaman ini", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Step4DoAndDontAnggota.this, Step3TataNilaiAnggota.class));
                            finish();
                        } else {
                            does.setText(data.optString("do_and_dont", "") + " (" + data.optString("sub_do_and_dont", "") + ")");
                            dont.setText(Html.fromHtml("<b>Do</b>:\n" + data.optString("content_do", "null") + "<br></br>" + "<br><b>Don't</b>:</br>\n" + data.optString("content_dont", "null")));

                            JSONObject history = jsonObject.getJSONObject("history");
                            hist.setText("Pertemuan sebelumnya: " + history.optString("do_and_dont", ""));
                        }
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(CocVerified.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Step4DoAndDontAnggota.this, Step3TataNilaiAnggota.class));
                        finish();
                        Snackbar snackbars = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbars.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step4DoAndDontAnggota.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mendapatkan data \n", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step4DoAndDontAnggota.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

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
