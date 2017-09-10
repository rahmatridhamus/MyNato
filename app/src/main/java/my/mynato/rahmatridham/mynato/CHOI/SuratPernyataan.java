package my.mynato.rahmatridham.mynato.CHOI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Adapter.CHOIAdapter.ChoiAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.LandingPage;
import my.mynato.rahmatridham.mynato.Model.ChoiModel;
import my.mynato.rahmatridham.mynato.Model.PakKadirModel;
import my.mynato.rahmatridham.mynato.PakKadir.PakKadir;
import my.mynato.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;
import my.mynato.rahmatridham.mynato.R;

public class SuratPernyataan extends AppCompatActivity {
    ListView listSurPer;
    ChoiAdapter adapter;
    ArrayList<ChoiModel> choiModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat_pernyataan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Surat Pernyataan (CHOI)");

        choiModelArrayList = new ArrayList<>();
        listSurPer = (ListView) findViewById(R.id.listChoi);
        listSurPer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChoiModel model = choiModelArrayList.get(position);
                try {
                    Intent intent = new Intent(SuratPernyataan.this, PernyataanBudayaPerilaku.class);
                    intent.putExtra("id_aktivasi_choi", model.getId_aktivasi_choi());
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(SuratPernyataan.this, "Gagal ke halaman koreksi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter = new ChoiAdapter(this, choiModelArrayList);
        listSurPer.setAdapter(adapter);

        getChoi();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(SuratPernyataan.this, LandingPage.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuratPernyataan.this, LandingPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void getChoi() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(SuratPernyataan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Choi/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            ChoiModel model = new ChoiModel(object.optString("id_aktivasi_choi", ""), object.optString("nama_choi", ""), object.optString("tanggal_mulai", ""), object.optString("tanggal_berakhir", ""), object.optString("status", ""));
                            choiModelArrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
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
                    SharedPreferences sharedPreferences = SuratPernyataan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(SuratPernyataan.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
