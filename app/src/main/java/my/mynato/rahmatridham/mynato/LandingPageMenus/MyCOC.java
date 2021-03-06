package my.mynato.rahmatridham.mynato.LandingPageMenus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import my.mynato.rahmatridham.mynato.Adapter.CocHistAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.CoC;
import my.mynato.rahmatridham.mynato.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_coc, container, false);
        cArrayList = new ArrayList<>();
        namaCoc = (TextView) view.findViewById(R.id.textHeader);
        tanggal = (TextView) view.findViewById(R.id.textViewTanggal);

        listViewHistory = (ListView) view.findViewById(R.id.listCocHistory);
        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CoC dataCoc = cArrayList.get(position);
                if (dataCoc.getKeterangan_coc().equals("COC THEMATIK")) {
                    Intent intent = new Intent(MyCOC.this.getContext(), DetailHistoryCoc.class);
                    intent.putExtra("id_coc_activity", dataCoc.getId_coc_activity());
                    intent.putExtra("id_group_coc", dataCoc.getId_group_coc());
                    intent.putExtra("keterangan_coc", "THEMATIK");
                    startActivity(intent);
//                    Toast.makeText(MyCOC.this.getContext(),dataCoc.getId_coc_activity() , Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MyCOC.this.getContext(), DetailHistoryCocInsidental.class);
                    intent.putExtra("id_coc_activity", dataCoc.getId_coc_activity());
                    intent.putExtra("id_group_coc", dataCoc.getId_group_coc());
                    intent.putExtra("keterangan_coc", "INSIDENTAL");
                    startActivity(intent);
//                    Toast.makeText(MyCOC.this.getContext(),dataCoc.getId_coc_activity() , Toast.LENGTH_SHORT).show();

                }


            }
        });


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cocRefresh);
        refreshLayout.setColorSchemeResources(
                R.color.greenButton,
                R.color.mynatoBlue,
                R.color.colorPrimaryDarkBlue
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
//                                Toast.makeText(MyCOC.this.getContext(), error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data CoC \n"+error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (Exception e) {
//                            Toast.makeText(MyCOC.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data CoC ", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
//                        Toast.makeText(MyCOC.this.getContext(), "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(getView(), "Gagal menerima data CoC ", Snackbar.LENGTH_LONG);
                        snackbar.show();
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
