package my.mynato.rahmatridham.mynato.CHOI;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Adapter.CHOIAdapter.DetChoiAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.ChoiDetailFormsModel;
import my.mynato.rahmatridham.mynato.R;

public class PernyataanBudayaPerilaku extends AppCompatActivity {
    ListView listView;
    DetChoiAdapter adapter;
    ArrayList<ChoiDetailFormsModel> choiDetailFormsModelArrayList;
    String idCuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pernyataan_budaya_perilaku);

        setTitle("Surat Pernyataan (CHOI)");

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            idCuy = (String) b.get("id_aktivasi_choi");
        }

        choiDetailFormsModelArrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listChoiDetail);
        adapter = new DetChoiAdapter(this, choiDetailFormsModelArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChoiDetailFormsModel model = choiDetailFormsModelArrayList.get(position);

                Intent intent = new Intent(PernyataanBudayaPerilaku.this,DetailFormPernyataan.class);
                intent.putExtra("id_formulir",model.getId_formulir());
                intent.putExtra("id_aktivasi_choi",idCuy);
                startActivity(intent);
            }
        });

        getChoiDetail(idCuy);
        adapter.notifyDataSetChanged();
    }

    private void getChoiDetail(String id) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(PernyataanBudayaPerilaku.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Choi/detail_choi/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            ChoiDetailFormsModel model = new ChoiDetailFormsModel(object.optString("id_formulir", ""), object.optString("title", ""), object.optString("nama_formulir", ""), object.optString("status", ""));
                            choiDetailFormsModelArrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
                        Toast.makeText(PernyataanBudayaPerilaku.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(PernyataanBudayaPerilaku.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(PernyataanBudayaPerilaku.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = PernyataanBudayaPerilaku.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(PernyataanBudayaPerilaku.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
