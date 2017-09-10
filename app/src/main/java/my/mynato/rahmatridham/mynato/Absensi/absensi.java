package my.mynato.rahmatridham.mynato.Absensi;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import my.mynato.rahmatridham.mynato.Adapter.AbsensiAdapter;
import my.mynato.rahmatridham.mynato.Adapter.absensiAdapt.FragAbsenAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.LandingPageMenus.MyCOC;
import my.mynato.rahmatridham.mynato.Model.CoC;
import my.mynato.rahmatridham.mynato.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class absensi extends Fragment {
    ListView listAbsensi;
    ArrayList<absensi.DataAbsen> dataAbsenArrayList;
    FragAbsenAdapter adapter;

    public absensi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_absensi, container, false);
        dataAbsenArrayList = new ArrayList<>();

        listAbsensi = (ListView) view.findViewById(R.id.absenlist);
        adapter = new FragAbsenAdapter(dataAbsenArrayList, absensi.this.getContext());
        listAbsensi.setAdapter(adapter);
        getAbsensi();
        adapter.notifyDataSetChanged();
        return view;
    }

    private void getAbsensi() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/get_absensi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(absensi.this.getContext(), response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                //array of Do and Dont
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONArray absensis = data.getJSONArray("data_absensi");

                                for (int i = 0; i < absensis.length(); i++) {
                                    JSONObject object = absensis.getJSONObject(i);
                                    dataAbsenArrayList.add(new DataAbsen(object.getString("nipeg"), object.getString("tanggal"), "", object.getString("status")));

                                }
                                adapter.notifyDataSetChanged();

                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(absensi.this.getContext(), error, Toast.LENGTH_SHORT).show();
                                if (error.equals("empty data")) {
                                    Snackbar snackbar = Snackbar.make(getView(), "Belum ada list absensi ", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    Snackbar snackbar = Snackbar.make(getView(),error, Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        } catch (Exception e) {
//                            Toast.makeText(absensi.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data absensi", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
//                        Toast.makeText(absensi.this.getContext(), "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data absensi", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = absensi.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(absensi.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(absensi.this.getContext());
        requestQueue.add(stringRequest);
    }

    public static class DataAbsen {
        String nipeg, tanggal, keterangan, status, id;

        public DataAbsen(String nipeg, String tanggal, String keterangan, String status) {
            this.nipeg = nipeg;
            this.tanggal = tanggal;
            this.keterangan = keterangan;
            this.status = status;
        }

        public DataAbsen(String nipeg, String tanggal, String keterangan, String status, String id) {
            this.nipeg = nipeg;
            this.tanggal = tanggal;
            this.keterangan = keterangan;
            this.status = status;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }

        public String getNipeg() {
            return nipeg;
        }

        public void setNipeg(String nipeg) {
            this.nipeg = nipeg;
        }

        public String getTanggal() {
            return tanggal;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}


