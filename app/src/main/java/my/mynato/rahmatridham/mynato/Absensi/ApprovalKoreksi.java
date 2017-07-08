package my.mynato.rahmatridham.mynato.Absensi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;

public class ApprovalKoreksi extends AppCompatActivity {
    TextView luarUnit;
    EditText masuk, keluar, tanggal;
    Button submit;
    TextView nama, nipeg, kantor, atasan;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_koreksi);
        this.setTitle("Approval Koreksi Absensi");
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            id = (String) b.get("idApproval");
        }

        luarUnit = (TextView) findViewById(R.id.radioButtonUnitLuar);
        nama = (TextView) findViewById(R.id.textViewNamaPemohon);
        nipeg = (TextView) findViewById(R.id.textViewNipegPemohon);
        kantor = (TextView) findViewById(R.id.textViewKantorPemohon);
        atasan = (TextView) findViewById(R.id.textViewAtasanPemohon);
        masuk = (EditText) findViewById(R.id.editTextMasuk);
        keluar = (EditText) findViewById(R.id.editTextKeluar);
        tanggal = (EditText) findViewById(R.id.editTextTanggalKoreksi);
        submit = (Button) findViewById(R.id.buttonSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitRequest(masuk.getText().toString(), keluar.getText().toString(), tanggal.getText().toString());
            }
        });

        getData(id);
    }

    private void getData(String id) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(ApprovalKoreksi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/detail_approval/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                nama.setText(": " + data.optString("nama", "null"));
                                nipeg.setText(": " + data.optString("nipeg", "null"));
                                kantor.setText(": " + data.optString("nama_kantor", "null"));
                                atasan.setText(": " + data.optString("nama_atasan", "null"));
                                masuk.setText(": " + data.optString("masuk_seharusnya", "null"));
                                keluar.setText(": " + data.optString("pulang_seharusnya", "null"));
                                tanggal.setText(": " + data.optString("tanggal_dikoreksi", "null"));
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(ApprovalKoreksi.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ApprovalKoreksi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(ApprovalKoreksi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = ApprovalKoreksi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(ApprovalKoreksi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void submitRequest(final String masuk, final String keluar, final String tanggal) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(ApprovalKoreksi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/send_approval",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                Toast.makeText(ApprovalKoreksi.this, "Submit data berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ApprovalKoreksi.this, AbsensiPage.class));
                                finish();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(ApprovalKoreksi.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ApprovalKoreksi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(ApprovalKoreksi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = ApprovalKoreksi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_koreksi_absensi", id);
                    params.put("pulang_seharusnya", keluar);
                    params.put("masuk_seharusnya", masuk);
                    params.put("tanggal_dikoreksi", tanggal);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(ApprovalKoreksi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
