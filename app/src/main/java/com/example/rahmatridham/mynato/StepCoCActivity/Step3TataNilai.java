package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.rahmatridham.mynato.Adapter.TataNilaiAdapter;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Model.GroupCoc;
import com.example.rahmatridham.mynato.Model.TataNilai;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Step3TataNilai extends AppCompatActivity {
    Button lanjut;
    ArrayList<TataNilai> tataNilaiArrayList;
    RadioGroup radioGroup;
    TataNilai tataNilais;
    boolean isCheck = false;
    TextView descTanilBef;
    String tanilSel="";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3_tata_nilai);

        tataNilaiArrayList = new ArrayList<>();
        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        radioGroup = (RadioGroup) findViewById(R.id.listTataNilai);
        descTanilBef =(TextView) findViewById(R.id.strTanilSebelum);

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setEnabled(isCheck);
        lanjut.setClickable(isCheck);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences sharedPreferences = Step3TataNilai.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    pushTataNilai(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF,""));
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 3: Tata Nilai");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Toast.makeText(Step3TataNilai.this, "id: " + checkedId, Toast.LENGTH_SHORT).show();
                tataNilais = tataNilaiArrayList.get(checkedId);

                isCheck = true;
                lanjut.setEnabled(isCheck);
                lanjut.setClickable(isCheck);

                AlertDialog.Builder builder = new AlertDialog.Builder(group.getContext());
                builder.setMessage(tataNilais.getTitle()+": \n\n"+tataNilais.getContent())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        SharedPreferences sharedPreferences = Step3TataNilai.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        getTataNilai(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF,""));
    }

    public void addRadioButtons(ArrayList<TataNilai> groupCocs) {
        for (int row = 0; row < 1; row++) {
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            radioGroup.setGravity(LinearLayout.SHOW_DIVIDER_MIDDLE);
            for (int i = 1; i <= groupCocs.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId((row * 2) + (i - 1));
                rdbtn.setText(groupCocs.get((i - 1)).getTitle());
                rdbtn.setTextSize(16);
                radioGroup.addView(rdbtn);
            }
        }
    }

    private void getTataNilai(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step3TataNilai.this, "", "Loading. Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Tata_Nilai/get_data/"+idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                //array of cerita TataNilai
                                JSONArray listing = data.getJSONArray("list");
                                for (int i = 0; i < listing.length(); i++) {
                                    JSONObject object = listing.getJSONObject(i);
                                    TataNilai tataNilai = new TataNilai(object.optString("id_tata_nilai", ""), object.optString("title", ""), object.optString("content", ""));
                                    tataNilaiArrayList.add(tataNilai);
                                }
                                addRadioButtons(tataNilaiArrayList);

                                JSONObject history = data.getJSONObject("history");
                                descTanilBef.setText("Pertemuan Sebelumnya: \n"+history.optString("title","(kosong)"));
                                dialog.dismiss();

                            } else {
                                String error = jsonObject.optString("messageError:\n");
                                Toast.makeText(Step3TataNilai.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step3TataNilai.this, "errorResponse: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step3TataNilai.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step3TataNilai.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step3TataNilai.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void pushTataNilai(String idGroup){
        final ProgressDialog dialog = ProgressDialog.show(Step3TataNilai.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_tata_nilai/set_group/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                Intent intent = new Intent(Step3TataNilai.this, Step4DoAndDont.class);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Step3TataNilai.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Step3TataNilai.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Step3TataNilai.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step3TataNilai.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_tata_nilai",tanilSel);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Step3TataNilai.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
