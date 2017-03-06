package com.example.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    ArrayList<GroupCoc> groupCocs = new ArrayList<>();
    GroupCoc groupCoc;
    boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_verified);

        next = (Button) findViewById(R.id.buttonSelanjutnya);
        group = (RadioGroup) findViewById(R.id.radioTipecoc);
        next.setEnabled(isCheck);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CocVerified.this, PrepareAddCoc.class);

                //Creating a shared preference
                SharedPreferences sharedPreferences = CocVerified.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.IDGROUPCOC_SHARED_PREF, groupCoc.getId_group_coc());
                editor.commit();

                startActivity(intent);
                next.setEnabled(isCheck);
                next.setClickable(isCheck);
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(CocVerified.this, "id: " + groupCocs.get(checkedId).getId_group_coc(), Toast.LENGTH_SHORT).show();
                groupCoc = groupCocs.get(checkedId);

                isCheck = true;
                next.setEnabled(isCheck);
                next.setClickable(isCheck);

            }
        });

        getGroupCoc();

    }

    public void addRadioButtons(ArrayList<GroupCoc> groupCocs) {

        for (int row = 0; row < 1; row++) {
            group.setOrientation(LinearLayout.VERTICAL);
            group.setGravity(LinearLayout.SHOW_DIVIDER_MIDDLE);
            for (int i = 1; i <= groupCocs.size(); i++) {
                RadioButton rdbtn = new RadioButton(this,null,R.attr.radioButtonStyle);
                rdbtn.setId((row * 2) + (i - 1));
                rdbtn.setText(groupCocs.get((i - 1)).getNama_group_coc());
                rdbtn.setTextSize(20);
                group.addView(rdbtn);
            }
        }
    }

    private void getGroupCoc() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(CocVerified.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Group_Coc", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            GroupCoc groupCoc = new GroupCoc(object.optString("id"), object.optString("id_group_coc"), object.optString("nama_group_coc"));
                            groupCocs.add(groupCoc);
                        }
                        addRadioButtons(groupCocs);
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
                        Toast.makeText(CocVerified.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(CocVerified.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(CocVerified.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = CocVerified.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(CocVerified.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
