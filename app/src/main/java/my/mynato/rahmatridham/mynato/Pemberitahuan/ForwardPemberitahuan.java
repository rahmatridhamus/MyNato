package my.mynato.rahmatridham.mynato.Pemberitahuan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Adapter.ListForwarderAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.AbsensiModel;
import my.mynato.rahmatridham.mynato.Model.PemberitahuanModel;
import my.mynato.rahmatridham.mynato.Model.PenerimaForwarder;
import my.mynato.rahmatridham.mynato.R;
import my.mynato.rahmatridham.mynato.StepCoCActivity.Step6Absensi;

public class ForwardPemberitahuan extends AppCompatActivity {
    TextView title, itemDesc, counterForward;
    Button submit, open;
    ListView listForwarder;
    MyCustomAdapter adapter;
    ArrayList<PenerimaForwarder> penerimaForwarderArrayList;
    ArrayList<String> pushDaftarAbsen = new ArrayList<>();

    String idPemb, pdf_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_pemberitahuan);
        Intent intent = getIntent();
        penerimaForwarderArrayList = new ArrayList<>();

        title = (TextView) findViewById(R.id.textViewJudulForward);
        itemDesc = (TextView) findViewById(R.id.textViewDescForward);
        counterForward = (TextView) findViewById(R.id.txtcountForward);
        submit = (Button) findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushDaftarAbsen = new ArrayList<>();
                ArrayList<PenerimaForwarder> absensiModels = adapter.absensiModels;
                for (int i = 0; i < absensiModels.size(); i++) {
                    PenerimaForwarder model = absensiModels.get(i);
                    if (model.isSelected()) {
                        pushDaftarAbsen.add(model.getKode_jabatan());
                    }
                }

                postForward(idPemb);
            }
        });

        open = (Button) findViewById(R.id.buttonOpenDoc);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(ForwardPemberitahuan.this, "gagal mengambil file", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listForwarder = (ListView) findViewById(R.id.listForwarder);

        adapter = new MyCustomAdapter(this, R.layout.listrow_absensi, penerimaForwarderArrayList);
        listForwarder.setAdapter(adapter);
        idPemb = intent.getStringExtra("id_pemberitahuan");
        getForwardPemberitahuan(idPemb);
        adapter.notifyDataSetChanged();
    }


    private void getForwardPemberitahuan(String id_pemberitahuan) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(ForwardPemberitahuan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/forward_message/" + id_pemberitahuan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject dataPemberitahuan = data.getJSONObject("data_pemberitahuan");
                                itemDesc.setText(dataPemberitahuan.optString("content", ""));
                                title.setText(dataPemberitahuan.optString("title", ""));
                                pdf_url = dataPemberitahuan.optString("file", "");

                                JSONArray dataPenerima = data.getJSONArray("daftar_penerima");
                                for (int i = 0; i < dataPenerima.length(); i++) {
                                    JSONObject object = dataPenerima.getJSONObject(i);
                                    PenerimaForwarder forwarder = new PenerimaForwarder(object.optString("kode_jabatan", ""), object.optString("nama_jabatan", ""), false);
                                    penerimaForwarderArrayList.add(forwarder);
                                }
                                counterForward.setText("0 dari " + penerimaForwarderArrayList.size() + " dipilih");
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = ForwardPemberitahuan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(ForwardPemberitahuan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void postForward(final String id_pemberitahuan) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(ForwardPemberitahuan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/forward_message/" + id_pemberitahuan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                startActivity(new Intent(ForwardPemberitahuan.this, Pemberitahuan.class));
                                Toast.makeText(ForwardPemberitahuan.this, "Forward pemberitahuan berhasil dilakukan", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(ForwardPemberitahuan.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ForwardPemberitahuan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(ForwardPemberitahuan.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = ForwardPemberitahuan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    String s = "";
                    for (int i = 0; i < pushDaftarAbsen.size(); i++) {
                        if (s.equals("")) {
                            s = pushDaftarAbsen.get(i);
                        } else {
                            s += "," + pushDaftarAbsen.get(i);
                        }
                    }
//                    Toast.makeText(ForwardPemberitahuan.this, s, Toast.LENGTH_SHORT).show();
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_pemberitahuan", id_pemberitahuan);
                    params.put("forwardnipeg", sharedPreferences.getString(Config.NIPEG_SHARED_PREF, ""));
                    params.put("penerima", s);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(ForwardPemberitahuan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private class MyCustomAdapter extends ArrayAdapter<PenerimaForwarder> {

        private ArrayList<PenerimaForwarder> absensiModels;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<PenerimaForwarder> countryList) {
            super(context, textViewResourceId, countryList);
            this.absensiModels = countryList;
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ForwardPemberitahuan.MyCustomAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(my.mynato.rahmatridham.mynato.R.layout.listrow_absensi, null);

                holder = new ForwardPemberitahuan.MyCustomAdapter.ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(my.mynato.rahmatridham.mynato.R.id.checkBoxAbsensi);
                holder.name.setChecked(absensiModels.get(position).isSelected());
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        PenerimaForwarder model = (PenerimaForwarder) cb.getTag();
                        model.setSelected(cb.isChecked());
                        updateCheck();
                    }
                });
            } else {
                holder = (ForwardPemberitahuan.MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            PenerimaForwarder penerimaForwarder = absensiModels.get(position);
            holder.name.setText(penerimaForwarder.getNama_jabatan());
            holder.name.setChecked(penerimaForwarder.isSelected());
            holder.name.setTag(penerimaForwarder);

            return convertView;

        }

        void updateCheck() {
            ArrayList<PenerimaForwarder> penerimaForwarders = adapter.absensiModels;
            int count = 0;
            for (int i = 0; i < penerimaForwarders.size(); i++) {
                if (penerimaForwarders.get(i).isSelected()) {
                    count++;
                }
            }
            counterForward.setText(count + " dari " + penerimaForwarderArrayList.size() + " dipilih");
        }

    }
}
