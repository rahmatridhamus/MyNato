package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
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
import com.example.rahmatridham.mynato.LoginActivity;
import com.example.rahmatridham.mynato.Model.GroupCoc;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrepareAddCoc extends AppCompatActivity {
    TextView kontenCOChari;
    CardView adminInput, AnggotaCoc;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_add_coc);
        sharedPreferences = PrepareAddCoc.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        adminInput = (CardView) findViewById(R.id.adminInput);
        AnggotaCoc = (CardView) findViewById(R.id.anggotaCoc);
        kontenCOChari = (TextView) findViewById(R.id.kontenCocHari);
        if(sharedPreferences.getString(Config.KETERANGAN_SHARED_PREF,"").equals("INSIDENTAL")){
            kontenCOChari.setText("CoC Insidental");
        }else {
            kontenCOChari.setText("CoC Thematik");
        }

//        Intent iin = getIntent();
//        Bundle b = iin.getExtras();
//        if (b != null) {
//            String day = (String) b.get("menuClicked");
//            kontenCOChari.setText("Konten CoC " + day);
//        }

        //Creating a shared preference

        final String idGroup = sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, "");

        adminInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisi.class));

                postDataBeforeStep1("ADMIN", idGroup);
            }
        });

        AnggotaCoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataBeforeStep1("ANGGOTA", idGroup);
//                startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisi.class));

            }
        });
    }

    private void postDataBeforeStep1(final String role, String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(PrepareAddCoc.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_group/" + idGroup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PrepareAddCoc.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();
                    if (status.equals(String.valueOf(1))) {
                        dialog.dismiss();
                        Toast.makeText(PrepareAddCoc.this, "Anda masih memiliki data CoC yang belum selesai", Toast.LENGTH_SHORT).show();
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject exist = data.getJSONObject("page_eksisting");
                        int i = exist.optInt("id", 0);
                        switch (i) {
                            case 1:
                                startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisi.class));
                                break;
                            case 2:
                                startActivity(new Intent(PrepareAddCoc.this, Step2Motivasi.class));
                                break;
                            case 3:
                                startActivity(new Intent(PrepareAddCoc.this, Step3TataNilai.class));
                                break;
                            case 4:
                                startActivity(new Intent(PrepareAddCoc.this, Step4DoAndDont.class));
                                break;
                            case 5:
                                startActivity(new Intent(PrepareAddCoc.this, Step5Thematik.class));
                                break;
                            case 6:
                                startActivity(new Intent(PrepareAddCoc.this, Step6Absensi.class));
                                break;
                            default:
                                Toast.makeText(PrepareAddCoc.this, "ID didn't match", Toast.LENGTH_SHORT).show();
                                break;
                        }


                    } else {
                        String error = jsonObject.optString("message");
                        Toast.makeText(PrepareAddCoc.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(PrepareAddCoc.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(PrepareAddCoc.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = PrepareAddCoc.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.IDGROUPCOC_SHARED_PREF, sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
                    params.put(Config.KETERANGAN_SHARED_PREF, sharedPreferences.getString(Config.KETERANGAN_SHARED_PREF, ""));
                    params.put("role", role);
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

//                    final String s = "mantap: " + sharedPreferences.getString(Config.KETERANGAN_SHARED_PREF, "");
//                    PrepareAddCoc.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(PrepareAddCoc.this, s, Toast.LENGTH_SHORT).show();
//
//                            dialog.dismiss();
//
//                        }
//                    });


                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(PrepareAddCoc.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
