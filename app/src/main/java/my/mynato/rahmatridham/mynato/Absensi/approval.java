package my.mynato.rahmatridham.mynato.Absensi;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import my.mynato.rahmatridham.mynato.Adapter.absensiAdapt.FragAbsenAdapter;
import my.mynato.rahmatridham.mynato.Adapter.absensiAdapt.FragApprovalAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class approval extends Fragment {

    ListView listAbsensi;
    ArrayList<absensi.DataAbsen> dataAbsenArrayList;
    FragApprovalAdapter adapter;

    public approval() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approval, container, false);
        dataAbsenArrayList = new ArrayList<>();

        listAbsensi = (ListView) view.findViewById(R.id.listApproval);
        adapter = new FragApprovalAdapter(approval.this.getContext(), dataAbsenArrayList);
        listAbsensi.setAdapter(adapter);
        getApproval();
        adapter.notifyDataSetChanged();
        return view;
    }

    private void getApproval() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/get_approval",
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
                                    dataAbsenArrayList.add(new absensi.DataAbsen(object.getString("nipeg"), object.getString("tanggal"), object.getString("keterangan"), object.getString("status"),object.getString("id_koreksi_absensi")));

                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(approval.this.getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(approval.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(approval.this.getContext(), "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = approval.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(approval.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(approval.this.getContext());
        requestQueue.add(stringRequest);
    }

}
