package my.mynato.rahmatridham.mynato.PakKadir;

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

import my.mynato.rahmatridham.mynato.Adapter.PakKadirAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.LandingPage;
import my.mynato.rahmatridham.mynato.Model.PakKadirModel;
import my.mynato.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PakKadir extends AppCompatActivity {
    ArrayList<PakKadirModel> pakKadirArrayList;
    PakKadirAdapter adapter;
    ListView listPakKadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(my.mynato.rahmatridham.mynato.R.layout.activity_pak_kadir);
        pakKadirArrayList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setTitle("Pak Kadir");


        listPakKadir = (ListView) findViewById(my.mynato.rahmatridham.mynato.R.id.listPakkadir);
        listPakKadir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PakKadirModel model = pakKadirArrayList.get(position);
                Intent intent = new Intent(PakKadir.this, DetailPakKadir.class);
                intent.putExtra("getId_pak_kadir", model.getId_pak_kadir());
                intent.putExtra("getContent", model.getContent());
                intent.putExtra("getTitle", model.getTitle());
                if (model.getStatus().equals("READ")) {
                    intent.putExtra("getStatus", true);
                } else {
                    intent.putExtra("getStatus", false);
                }
                startActivity(intent);
                finish();
            }
        });
        adapter = new PakKadirAdapter(pakKadirArrayList, this);
        listPakKadir.setAdapter(adapter);
        getPakKadir();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(PakKadir.this, LandingPage.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PakKadir.this, LandingPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private void getPakKadir() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(PakKadir.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "PakKadir", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            PakKadirModel model = new PakKadirModel(object.optString("id_pak_kadir", ""), object.optString("title", ""), object.optString("content", ""), object.optString("sending_date", ""), object.optString("received_date", ""), object.optString("reading_time", ""), object.optString("status", ""));
                            pakKadirArrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
                        if (error.equals("empty data")) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Belum ada data", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
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
                    SharedPreferences sharedPreferences = PakKadir.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(PakKadir.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
