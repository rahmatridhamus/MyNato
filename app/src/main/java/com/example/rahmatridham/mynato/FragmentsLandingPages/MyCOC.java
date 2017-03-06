package com.example.rahmatridham.mynato.FragmentsLandingPages;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahmatridham.mynato.Adapter.CocHistAdapter;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Model.CoC;
import com.example.rahmatridham.mynato.Model.DoAndDont;
import com.example.rahmatridham.mynato.R;
import com.example.rahmatridham.mynato.StepCoCActivity.Step4DoAndDont;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCOC extends Fragment {
    TextView namaCoc, tanggal;
    ListView listViewHistory;
    CocHistAdapter adapter;
    ArrayList<CoC> cArrayList;
    SwipeRefreshLayout refreshLayout;

    public MyCOC() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_coc, container, false);
        cArrayList = new ArrayList<>();
        namaCoc = (TextView) view.findViewById(R.id.textHeader);
        tanggal = (TextView) view.findViewById(R.id.textViewTanggal);
        listViewHistory = (ListView) view.findViewById(R.id.listCocHistory);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cocRefresh);
        refreshLayout.setColorSchemeResources(
                R.color.cardview_shadow_end_color,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cArrayList.clear();
                getHistory();
            }
        });

        adapter = new CocHistAdapter(cArrayList, MyCOC.this.getContext());
        listViewHistory.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        refreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshLayout.setRefreshing(true);
                                        cArrayList.clear();
                                        getHistory();
                                    }
                                }
        );
        return view;
    }

    private void getHistory() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "History",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                //array of Do and Dont
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    CoC coC = new CoC(object.optString("id_coc_activity", ""), object.optString("id_group_coc", ""), object.optString("keterangan_coc", ""), object.optString("date", ""));
                                    cArrayList.add(coC);
                                }
                                adapter.notifyDataSetChanged();
                                refreshLayout.setRefreshing(false);
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(MyCOC.this.getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MyCOC.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(MyCOC.this.getContext(), "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = MyCOC.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(MyCOC.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(MyCOC.this.getContext());
        requestQueue.add(stringRequest);
    }

}
