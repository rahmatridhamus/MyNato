package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahmatridham.mynato.Adapter.GamesAdapter;
import com.example.rahmatridham.mynato.Adapter.MotivasiAdapter;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Model.CeritaMotivasi;
import com.example.rahmatridham.mynato.Model.Games;
import com.example.rahmatridham.mynato.OnClickListenerGames;
import com.example.rahmatridham.mynato.OnItemClickListenerMotivasi;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Step2Motivasi extends AppCompatActivity {
    Button lanjut;
    ArrayList<CeritaMotivasi> ceritaMotivasiArrayList = new ArrayList<>();
    ArrayList<Games> games = new ArrayList<>();
    MotivasiAdapter mAdapter;
    GamesAdapter gAdapter;
    RecyclerView motivas, gem;
    TextView descBefMot, descBefGem, selectMotivasi, selectGames;
    String motSel = "", gemSel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2_motivasi);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);

        descBefMot = (TextView) findViewById(R.id.strMotivSebelum);
        descBefGem = (TextView) findViewById(R.id.strGamesSebelum);
        selectMotivasi = (TextView) findViewById(R.id.txtDipilihMotiv);
        selectGames = (TextView) findViewById(R.id.txtDipilihGam);
        selectMotivasi.setTextColor(getResources().getColor(R.color.red));
        selectGames.setTextColor(getResources().getColor(R.color.red));
        mAdapter = new MotivasiAdapter(ceritaMotivasiArrayList, new OnItemClickListenerMotivasi() {
            @Override
            public void onItemClick(CeritaMotivasi motivasi) {
//                Toast.makeText(Step2Motivasi.this, "Motivasi dipilih:\n"+motivasi.getTitle(), Toast.LENGTH_SHORT).show();
                selectMotivasi.setText("dipilih: " + motivasi.getTitle());
                selectMotivasi.setTextColor(getResources().getColor(R.color.holo_green));
                selectMotivasi.setTypeface(Typeface.DEFAULT_BOLD);

                motSel = motivasi.getId();
            }
        });
        motivas = (RecyclerView) findViewById(R.id.recyclerMotivasi);
        gem = (RecyclerView) findViewById(R.id.recyclerGames);
        gAdapter = new GamesAdapter(games, new OnClickListenerGames() {
            @Override
            public void onItemClick(Games games) {
//                Toast.makeText(Step2Motivasi.this, "Games dipilih:\n"+games.getTitle(), Toast.LENGTH_SHORT).show();
                selectGames.setText("dipilih: " + games.getTitle());
                selectGames.setTextColor(getResources().getColor(R.color.holo_green));
                selectGames.setTypeface(Typeface.DEFAULT_BOLD);

                gemSel = games.getId();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        motivas.setLayoutManager(layoutManager);
        motivas.setItemAnimator(new DefaultItemAnimator());
        motivas.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        gem.setLayoutManager(mLayoutManager);
        gem.setItemAnimator(new DefaultItemAnimator());
        gem.setAdapter(gAdapter);

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!motSel.equals("") && !gemSel.equals("")) {
                    SharedPreferences sharedPreferences = Step2Motivasi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    pushCeritaMotivasi(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
                } else {
                    Toast.makeText(Step2Motivasi.this, "Lengkapi pilihan Motivasi ataupun Games Anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 2: Motivasi");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        SharedPreferences sharedPreferences = Step2Motivasi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        getMotivasi(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
        mAdapter.notifyDataSetChanged();
        gAdapter.notifyDataSetChanged();
    }


    private void getMotivasi(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step2Motivasi.this, "", "Loading. Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Cerita_Motivasi" + "/get_data/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Step2Motivasi.this, response.trim(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");

                                //array of cerita motivasi
                                JSONArray cerMot = data.getJSONArray("cerita_motivasi");
                                for (int i = 0; i < cerMot.length(); i++) {
                                    JSONObject object = cerMot.getJSONObject(i);
                                    CeritaMotivasi motivasi = new CeritaMotivasi(object.optString("id", ""), object.optString("title", ""), object.optString("file", ""), object.optString("keterangan", ""));
                                    ceritaMotivasiArrayList.add(motivasi);
                                }

                                //array of games
                                JSONArray gamesList = data.getJSONArray("games");
                                if (!data.isNull("games")) {
                                    for (int i = 0; i < gamesList.length(); i++) {
                                        JSONObject gem = gamesList.getJSONObject(i);
                                        games.add(new Games(gem.optString("id", ""), gem.optString("title", ""), gem.optString("file", ""), gem.optString("keterangan", "")));
                                    }
                                }

                                JSONObject object = data.getJSONObject("history");
                                String id_games = object.optString("id_games", "");
                                String title_games = object.optString("title_games", "");
                                String id_cerita_motivasi = object.optString("id_cerita_motivasi", "");
                                String title_cerita_motivasi = object.optString("title_cerita_motivasi", "");
                                String date = object.optString("date", "");

                                descBefMot.setText("Pertemuan sebelumnya, Cerita Motivasi: " + title_cerita_motivasi + "\nTanggal: " + date);
                                descBefGem.setText("Pertemuan sebelumnya, Games: " + title_games + "\nTanggal: " + date);

                                mAdapter.notifyDataSetChanged();
                                gAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step2Motivasi.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step2Motivasi.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step2Motivasi.this, "errorResponse: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step2Motivasi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
//                    params.put(Config.IDGROUPCOC_SHARED_PREF, sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, "")+"03");
//                    final String s = sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, "");
//                    Step2Motivasi.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(Step2Motivasi.this, s, Toast.LENGTH_SHORT).show();
//
//                            dialog.dismiss();
//
//                        }
//                    });

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step2Motivasi.this, "errorParam: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void pushCeritaMotivasi(String idGroup) {
        final ProgressDialog dialog = ProgressDialog.show(Step2Motivasi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_cerita_motivasi/set_group/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Step2Motivasi.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {

                                Intent intent = new Intent(Step2Motivasi.this, Step3TataNilai.class);
                                startActivity(intent);

                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step2Motivasi.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step2Motivasi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step2Motivasi.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step2Motivasi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_cerita_motivasi", motSel);
                    params.put("id_games", gemSel);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step2Motivasi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
