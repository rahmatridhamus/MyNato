package my.mynato.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
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

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota.Step1VisiMisiAnggota;
import my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota.Step2MotivasiAnggota;
import my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota.Step3TataNilaiAnggota;
import my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota.Step4DoAndDontAnggota;
import my.mynato.rahmatridham.mynato.StepCoCActivity.StepCoCAnggota.Step5ThematikAnggota;

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
        setContentView(my.mynato.rahmatridham.mynato.R.layout.activity_prepare_add_coc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PrepareAddCoc.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        adminInput = (CardView) findViewById(my.mynato.rahmatridham.mynato.R.id.adminInput);
        AnggotaCoc = (CardView) findViewById(my.mynato.rahmatridham.mynato.R.id.anggotaCoc);
        kontenCOChari = (TextView) findViewById(my.mynato.rahmatridham.mynato.R.id.kontenCocHari);
        if (sharedPreferences.getString(Config.KETERANGAN_SHARED_PREF, "").equals("INSIDENTAL")) {
            kontenCOChari.setText("CoC Insidental");
        } else {
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

                Config.isAnggota = false;
                postDataBeforeStep1("ADMIN", idGroup, v);
            }
        });

        AnggotaCoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.isAnggota = true;
                postDataBeforeStep1("ANGGOTA", idGroup, v);
//                startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisi.class));

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void postDataBeforeStep1(final String role, String idGroup, final View view) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(PrepareAddCoc.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_group/" + idGroup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(PrepareAddCoc.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();
                    if (status.equals(String.valueOf(1))) {
                        dialog.dismiss();
                        Toast.makeText(PrepareAddCoc.this, "Anda masih memiliki data CoC yang belum selesai", Toast.LENGTH_SHORT).show();
//                        Snackbar snackbars = Snackbar.make(findViewById(android.R.id.content), "Anda masih memiliki data CoC yang belum selesai", Snackbar.LENGTH_LONG);
//                        snackbars.show();

                        JSONObject data = jsonObject.getJSONObject("data");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.IDCOCACTIVITY_SHARED_PREF, data.optString("id_coc_activity",""));
                        editor.commit();

                        JSONObject exist = jsonObject.getJSONObject("page_eksisting");
                        int i = exist.optInt("id", 0);
                        switch (i) {
                            case 0:
//                                Toast.makeText(PrepareAddCoc.this, "CoC sudah dilakukan", Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "CoC sudah dilakukan", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            case 1:
                                if (Config.isAnggota) {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisiAnggota.class));

                                } else {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisi.class));
                                }
                                break;
                            case 2:
                                if (Config.isAnggota) {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisiAnggota.class));

                                } else {
                                    startActivity(new Intent(PrepareAddCoc.this, Step2Motivasi.class));
                                }
                                break;
                            case 3:
                                if (Config.isAnggota) {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisiAnggota.class));
                                } else {
                                    startActivity(new Intent(PrepareAddCoc.this, Step3TataNilai.class));
                                }
                                break;
                            case 4:
                                if (Config.isAnggota) {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisiAnggota.class));

                                } else {
                                    startActivity(new Intent(PrepareAddCoc.this, Step4DoAndDont.class));
                                }
                                break;
                            case 5:
                                if (Config.isAnggota) {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisiAnggota.class));

                                } else {
                                    startActivity(new Intent(PrepareAddCoc.this, Step5Thematik.class));
                                }
                                break;
                            case 6:
                                if (Config.isAnggota) {
                                    startActivity(new Intent(PrepareAddCoc.this, Step1VisiMisiAnggota.class));
                                } else {
                                    startActivity(new Intent(PrepareAddCoc.this, Step6Absensi.class));
                                }
                                break;
                            default:
                                Toast.makeText(PrepareAddCoc.this, "ID didn't match", Toast.LENGTH_SHORT).show();
                                break;
                        }


                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(PrepareAddCoc.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
//                        Toast.makeText(PrepareAddCoc.this, "Gagal mengirim data, mohon ulangi.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(PrepareAddCoc.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                    snackbar.show();
//                    Toast.makeText(PrepareAddCoc.this, "Gagal mengirim data, mohon ulangi. Pastikan internet Anda aktif.", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
//                        Toast.makeText(PrepareAddCoc.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                        snackbar.show();
//                        Toast.makeText(PrepareAddCoc.this, "Gagal mengirim data, mohon ulangi. Pastikan internet Anda aktif.", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(PrepareAddCoc.this, "Gagal mengirim data, mohon ulangi. Pastikan internet Anda aktif.", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data, mohon ulangi. Pastikan internet Anda aktif.", Snackbar.LENGTH_LONG);
                    snackbar.show();
// Toast.makeText(PrepareAddCoc.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
