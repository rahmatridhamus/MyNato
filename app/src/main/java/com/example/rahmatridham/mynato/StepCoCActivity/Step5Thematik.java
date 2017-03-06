package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
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
import com.example.rahmatridham.mynato.Adapter.ThematikAdapter;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Model.DoAndDont;
import com.example.rahmatridham.mynato.Model.SubtitleThematik;
import com.example.rahmatridham.mynato.Model.Thematik;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step5Thematik extends AppCompatActivity {
    Button lanjut;
    TextView descThemBef,dipilihThematik;
    ArrayList<Thematik> thematikArrayList = new ArrayList<>();
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpandableListView listView;
    ThematikAdapter adapter;
    String themSel,subThemSel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step5_thematik);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listView = (ExpandableListView) findViewById(R.id.listThematik);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        descThemBef = (TextView) findViewById(R.id.strThematikSebelum);
        dipilihThematik = (TextView) findViewById(R.id.textViewDipilihThematik);
        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = Step5Thematik.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                pushThematik(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 5: Thematik");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                themSel = thematikArrayList.get(groupPosition).getId_content_list();
                subThemSel = thematikArrayList.get(groupPosition).getSubtitle().get(childPosition).getId_sub_content_list();
                dipilihThematik.setText("dipilih: "+thematikArrayList.get(groupPosition).getSubtitle().get(childPosition).getSub_title());
                dipilihThematik.setTextColor(getResources().getColor(R.color.holo_green));
                final Dialog dialog = new Dialog(parent.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.thematik_weburl);

                WebView wv = (WebView) dialog
                        .findViewById(R.id.webthematik);

//                wv.loadUrl("http://stackoverflow.com/questions/26030885/android-how-can-i-open-a-webview-in-a-popup-window");
                wv.loadUrl(thematikArrayList.get(groupPosition).getSubtitle().get(childPosition).getUrl());
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                dialog.setOnKeyListener(new  DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            dialog.dismiss();
                        }
                        return false;
                    }

                });
                dialog.show();

                return false;
            }
        });

        SharedPreferences sharedPreferences = Step5Thematik.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getThematik(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));
        adapter = new ThematikAdapter(this,listDataHeader,listDataChild);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    void prepare(ArrayList<Thematik> thematiks) {

        for (int i = 0; i < thematiks.size(); i++) {
            listDataHeader.add(thematiks.get(i).getTitle());
        }
        for (int i = 0; i < listDataHeader.size(); i++) {
            listDataChild.put(listDataHeader.get(i), thematiks.get(i).getSubtit());
        }
        adapter.notifyDataSetChanged();
    }

    private void getThematik(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step5Thematik.this, "", "Loading. Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Thematik/get_data/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                //array of Do and Dont
                                JSONArray list = data.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    JSONArray subtit = object.getJSONArray("subtitle");
                                    ArrayList<SubtitleThematik> subtitleArrayList = new ArrayList<>();

                                    if (!object.isNull("subtitle")) {
                                        for (int j = 0; j < subtit.length(); j++) {
                                            JSONObject objSubtit = subtit.getJSONObject(j);
                                            subtitleArrayList.add(new SubtitleThematik(objSubtit.optString("id_sub_content_list", ""), objSubtit.optString("sub_title", ""), objSubtit.optString("content", ""), objSubtit.optString("url", "")));
                                        }
                                    }
                                    Thematik thematik = new Thematik(object.optString("id_content_list", ""), object.optString("title", ""), subtitleArrayList);
                                    thematikArrayList.add(thematik);
                                }
                                prepare(thematikArrayList);
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step5Thematik.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step5Thematik.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step5Thematik.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step5Thematik.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step5Thematik.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void pushThematik(String idGroup) {
        final ProgressDialog dialog = ProgressDialog.show(Step5Thematik.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_thematik/set_group/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                Intent intent = new Intent(Step5Thematik.this, Step6Absensi.class);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step5Thematik.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step5Thematik.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step5Thematik.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step5Thematik.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_content_thematik",themSel);
                    params.put("id_sub_content_thematik",subThemSel);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step5Thematik.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
