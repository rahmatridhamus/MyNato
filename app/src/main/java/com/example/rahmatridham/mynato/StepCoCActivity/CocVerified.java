package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.LoginActivity;
import com.example.rahmatridham.mynato.Model.GroupCoc;
import com.example.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CocVerified extends AppCompatActivity {
    Button next;
    RadioGroup group;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_verified);

        next = (Button) findViewById(R.id.buttonSelanjutnya);
        group = (RadioGroup) findViewById(R.id.radioTipecoc);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CocVerified.this, PrepareAddCoc.class);
                startActivity(intent);
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(CocVerified.this, "Changed: " + checkedId, Toast.LENGTH_SHORT).show();
            }
        });

        getOption();

    }

    public void addRadioButtons(ArrayList<GroupCoc> groupCocs) {

        for (int row = 0; row < 1; row++) {
            group = new RadioGroup(this);
            group.setOrientation(LinearLayout.VERTICAL);
            group.setGravity(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (int i = 1; i <= groupCocs.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId((row * 2) + (i-1));
                rdbtn.setText(groupCocs.get((i-1)).getNama_group_coc());
                rdbtn.setTextSize(16);
                group.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.radioTipecoc)).addView(group);
        }
    }

    private void getOption() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Group_Coc",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                ArrayList<GroupCoc> groupCocs = new ArrayList<>();
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    GroupCoc groupCoc = new GroupCoc(object.optString("id"), object.optString("id_group_coc"), object.optString("nama_group_coc"));
                                    groupCocs.add(groupCoc);
                                }
                                addRadioButtons(groupCocs);
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(CocVerified.this, error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(CocVerified.this, "error: \n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(CocVerified.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = CocVerified.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF,""));

                    //returning parameter
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(CocVerified.this, "error: \n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
