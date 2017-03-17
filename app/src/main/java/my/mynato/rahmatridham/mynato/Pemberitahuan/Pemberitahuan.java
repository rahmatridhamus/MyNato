package my.mynato.rahmatridham.mynato.Pemberitahuan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import my.mynato.rahmatridham.mynato.Adapter.PemberitahuanAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.PemberitahuanModel;
import my.mynato.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pemberitahuan extends AppCompatActivity {
    ListView listView;
    PemberitahuanAdapter adapter;
    ArrayList<PemberitahuanModel> pemberitahuanModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan);

        pemberitahuanModelArrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listPemberitahuan);
        adapter = new PemberitahuanAdapter(this, pemberitahuanModelArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PemberitahuanModel model = pemberitahuanModelArrayList.get(position);
                Intent intent = new Intent(Pemberitahuan.this,pemberitahuanDetil.class);
                intent.putExtra("id_pemberitahuan",model.getId_pemberitahuan());
                startActivity(intent);
                finish();
            }
        });

        getPemberitahuan();
        adapter.notifyDataSetChanged();
    }

    private void getPemberitahuan() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Pemberitahuan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    PemberitahuanModel model = new PemberitahuanModel(object.optString("id_pemberitahuan", ""), object.optString("title", ""), object.optString("content", ""), object.optString("sending_date", ""), object.optString("received_date", ""), object.optString("reading_time", ""), object.optString("status", ""));

                                    pemberitahuanModelArrayList.add(model);
                                }
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Pemberitahuan.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Pemberitahuan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Pemberitahuan.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Pemberitahuan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Pemberitahuan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
