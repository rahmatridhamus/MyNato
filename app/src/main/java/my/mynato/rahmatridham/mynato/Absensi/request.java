package my.mynato.rahmatridham.mynato.Absensi;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class request extends Fragment {
    ListView listAbsensi;
    ArrayList<absensi.DataAbsen> dataAbsenArrayList;
    FragAbsenAdapter adapter;
    RelativeLayout korAbs;


    public request() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        dataAbsenArrayList = new ArrayList<>();

        korAbs = (RelativeLayout) view.findViewById(R.id.KoreksiAbsen);
        korAbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(request.this.getContext(),RequestKoreksi.class));
            }
        });

        listAbsensi = (ListView) view.findViewById(R.id.listRequest);
        listAbsensi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                absensi.DataAbsen model = dataAbsenArrayList.get(position);
                Toast.makeText(view.getContext(), model.getStatus(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new FragAbsenAdapter(dataAbsenArrayList, request.this.getContext());
        listAbsensi.setAdapter(adapter);
        getRequest();
        adapter.notifyDataSetChanged();
        return view;
    }

    private void getRequest() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/get_request",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                //array of Do and Dont
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONArray absensis = data.getJSONArray("data_absensi");

                                for (int i = 0; i < absensis.length(); i++) {
                                    JSONObject object = absensis.getJSONObject(i);
                                    dataAbsenArrayList.add(new absensi.DataAbsen(object.getString("nipeg"), object.getString("tanggal"), object.getString("keterangan"), object.getString("status")));

                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(request.this.getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(request.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(request.this.getContext(), "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = request.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(request.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(request.this.getContext());
        requestQueue.add(stringRequest);
    }

}
