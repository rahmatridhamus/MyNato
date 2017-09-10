package my.mynato.rahmatridham.mynato.LandingPageMenus;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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

import my.mynato.rahmatridham.mynato.Absensi.AbsensiPage;
import my.mynato.rahmatridham.mynato.CHOI.SuratPernyataan;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.CoC;
import my.mynato.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;
import my.mynato.rahmatridham.mynato.R;
import my.mynato.rahmatridham.mynato.StepCoCActivity.CocVerified;
import my.mynato.rahmatridham.mynato.PakKadir.PakKadir;
import my.mynato.rahmatridham.mynato.Survey.Survey;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    CardView cocThematik, cocinsidental, absensi, pemberitahuan, survey, pakKadirun, suratPernyataan;
    ImageView isReadSurvey, isReadabsensi, isReadpemberitahuan, isReadpakKadirun, isReadSurat;
    WebView wv;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        isReadSurvey = (ImageView) view.findViewById(R.id.isReadSurvey);
        isReadabsensi = (ImageView) view.findViewById(R.id.isReadabsensi);
        isReadpemberitahuan = (ImageView) view.findViewById(R.id.isReadpemberitahuan);
        isReadpakKadirun = (ImageView) view.findViewById(R.id.isReadpakKadir);
        isReadSurat = (ImageView) view.findViewById(R.id.isReadsuratPernyataan);


        cocThematik = (CardView) view.findViewById(R.id.cocThematik);
        cocinsidental = (CardView) view.findViewById(R.id.insidental);
        absensi = (CardView) view.findViewById(R.id.absensi);
        pemberitahuan = (CardView) view.findViewById(R.id.pemberitahuan);
        survey = (CardView) view.findViewById(R.id.survey);
        pakKadirun = (CardView) view.findViewById(R.id.pakKadir);
        suratPernyataan = (CardView) view.findViewById(R.id.suratPernyataan);

        cocThematik.setOnClickListener(this);
        cocinsidental.setOnClickListener(this);
        absensi.setOnClickListener(this);
        pemberitahuan.setOnClickListener(this);
        survey.setOnClickListener(this);
        pakKadirun.setOnClickListener(this);
        suratPernyataan.setOnClickListener(this);

        wv = (WebView) view.findViewById(R.id.chart1);
        WebSettings settings = wv.getSettings();
        settings.setMinimumFontSize(18);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        wv.loadUrl("http://119.252.170.4:8089/mynato/mastercoc/chart/chartview/101025");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        getHome();
        return view;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = Home.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int i = v.getId();
        switch (i) {
            case my.mynato.rahmatridham.mynato.R.id.cocThematik:
                //Creating a shared preference
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.KETERANGAN_SHARED_PREF, "THEMATIK");
                editor.commit();

                Intent intent5 = new Intent(v.getContext(), CocVerified.class);
                startActivity(intent5);
                break;

            case my.mynato.rahmatridham.mynato.R.id.insidental:
                //Creating a shared preference
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(Config.KETERANGAN_SHARED_PREF, "INSIDENTAL");
                edit.commit();

                Intent intent6 = new Intent(v.getContext(), CocVerified.class);
                startActivity(intent6);
                break;

            case my.mynato.rahmatridham.mynato.R.id.absensi:
//                Toast.makeText(v.getContext(), "menu absensi clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this.getContext(), AbsensiPage.class));

                break;

            case my.mynato.rahmatridham.mynato.R.id.pemberitahuan:
                startActivity(new Intent(Home.this.getContext(), Pemberitahuan.class));
                break;

            case my.mynato.rahmatridham.mynato.R.id.pakKadir:
                startActivity(new Intent(Home.this.getContext(), PakKadir.class));
                break;

            case my.mynato.rahmatridham.mynato.R.id.survey:
                startActivity(new Intent(Home.this.getContext(), Survey.class));
                break;

            case R.id.suratPernyataan:
//                Toast.makeText(Home.this.getContext(), "surat pernyataan", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this.getContext(), SuratPernyataan.class));
                break;
        }

    }

    private void getHome() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Home",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Home.this.getContext(), response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                //array of Do and Dont
                                JSONObject data = jsonObject.getJSONObject("data");

                                if (data.optInt("data_pemberitahuan") != 0) {
                                    isReadpemberitahuan.setVisibility(View.VISIBLE);
                                }

                                if (data.optInt("data_pakkadir") != 0) {
                                    isReadpakKadirun.setVisibility(View.VISIBLE);
                                }

                                if (data.optInt("data_survey") != 0) {
                                    isReadSurvey.setVisibility(View.VISIBLE);
                                }

                                if (data.optInt("data_absensi") != 0) {
                                    isReadabsensi.setVisibility(View.VISIBLE);
                                }

                                if (data.optInt("data_choi") != 0) {
                                    isReadSurat.setVisibility(View.VISIBLE);
                                }

                            } else {
                                String error = jsonObject.optString("message");
                                Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data \n" + error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (Exception e) {
//                            Toast.makeText(Home.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data ", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data ", Snackbar.LENGTH_LONG);
                        snackbar.show();
//                        refreshLayout.setRefreshing(false);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Home.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Home.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this.getContext());
        requestQueue.add(stringRequest);
    }
}
