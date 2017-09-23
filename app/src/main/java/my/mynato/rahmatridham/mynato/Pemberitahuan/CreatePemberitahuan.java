package my.mynato.rahmatridham.mynato.Pemberitahuan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import in.gauriinfotech.commons.Commons;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.PemberitahuanModel;
import my.mynato.rahmatridham.mynato.Model.PenerimaForwarder;
import my.mynato.rahmatridham.mynato.R;
import my.mynato.rahmatridham.mynato.StepCoCActivity.Step6Absensi;
import my.mynato.rahmatridham.mynato.VolleyAssets.AppHelper;
import my.mynato.rahmatridham.mynato.VolleyAssets.VolleyMultipartRequest;
import my.mynato.rahmatridham.mynato.VolleyAssets.VolleySingleton;

public class CreatePemberitahuan extends AppCompatActivity {
    TextView counterForward, namaFile;
    EditText title, itemDesc;
    Button submit, open;
    ListView listForwarder;
    CreatePemberitahuan.MyCustomAdapter adapter;
    ArrayList<PenerimaForwarder> penerimaForwarderArrayList;
    ArrayList<String> pushDaftarAbsen = new ArrayList<>();
    final int SELECT_PDF = 1;
    String pdf_url;
    File myFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pemberitahuan);
        this.setTitle("Buat Pemberitahuan");

        penerimaForwarderArrayList = new ArrayList<>();

        title = (EditText) findViewById(R.id.textViewJudulForward);
        itemDesc = (EditText) findViewById(R.id.textViewDescForward);
        counterForward = (TextView) findViewById(R.id.txtcountForward);
        namaFile = (TextView) findViewById(R.id.textViewNamaFile);
        namaFile.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        namaFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(v.getContext(), v);
                Menu m = popup.getMenu();
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_pdf, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Open")) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(CreatePemberitahuan.this, "Tidak dapat membuka file", Toast.LENGTH_SHORT).show();
                            }
                        } else if (item.getTitle().equals("Delete")) {
                            myFile = null;
                            namaFile.setText("");
                        }

                        return false;
                    }
                });
                popup.show();


            }
        });
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


                postForward(title.getText().toString(), itemDesc.getText().toString());
            }
        });

        open = (Button) findViewById(R.id.buttonOpenDoc);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "Select a PDF "), SELECT_PDF);

                } catch (Exception e) {
                    Toast.makeText(CreatePemberitahuan.this, "gagal mengambil file", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listForwarder = (ListView) findViewById(R.id.listForwarder);

        adapter = new CreatePemberitahuan.MyCustomAdapter(this, R.layout.listrow_absensi, penerimaForwarderArrayList);
        listForwarder.setAdapter(adapter);
        getDataCreate();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PDF:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                namaFile.setText(displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        namaFile.setText(displayName);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getDataCreate() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(CreatePemberitahuan.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/create_message",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");

                                JSONArray dataPenerima = data.getJSONArray("data_penerima");
                                for (int i = 0; i < dataPenerima.length(); i++) {
                                    JSONObject object = dataPenerima.getJSONObject(i);
                                    PenerimaForwarder forwarder = new PenerimaForwarder(object.optString("nipeg", ""), object.optString("nama", ""), true);
                                    penerimaForwarderArrayList.add(forwarder);
                                }
                                counterForward.setText(penerimaForwarderArrayList.size()+" dari " + penerimaForwarderArrayList.size() + " dipilih");
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
                    SharedPreferences sharedPreferences = CreatePemberitahuan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void postForward(final String title, final String content) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(CreatePemberitahuan.this, "", "Loading. Please wait...", true);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/send_message", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                // parse success output
//                Toast.makeText(CreatePemberitahuan.this, resultResponse, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    String status = jsonObject.optString("status").trim();
                    if (status.equals(String.valueOf(1))) {

                        startActivity(new Intent(CreatePemberitahuan.this, Pemberitahuan.class));
                        finish();
                        Toast.makeText(CreatePemberitahuan.this, "Pemberitahuan berhasil dikirimkan", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
//                        Toast.makeText(Step6Absensi.this, "responseError\n" + error, Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
//                    Toast.makeText(Step6Absensi.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal parsing data\n", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                Toast.makeText(CreatePemberitahuan.this, "errorResponse" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data "+error.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                snackbar.show();
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = CreatePemberitahuan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Adding parameters to request
                String s = "";
                for (int i = 0; i < pushDaftarAbsen.size(); i++) {
                    if (s.equals("")) {
                        s = pushDaftarAbsen.get(i);
                    } else {
                        s += "," + pushDaftarAbsen.get(i);
                    }
                }

                Log.d("digoda",s);

                params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                params.put("title", title);
                params.put("content", content);
                params.put("penerima", s);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if ((myFile != null)) {
                    params.put("file", new DataPart(myFile.getPath(), Config.readFile(myFile)));
                }//masukan file pdf kesini
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

//    private void postForwardNoFile(final String title, final String content) {
//        //Creating a string request
//        final ProgressDialog dialog = ProgressDialog.show(CreatePemberitahuan.this, "", "Loading. Please wait...", true);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Pemberitahuan/send_message",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        String resultResponse = new String(response);
//                        // parse success output
//                        try {
//                            JSONObject jsonObject = new JSONObject(resultResponse);
//                            String status = jsonObject.optString("status").trim();
//                            if (status.equals(String.valueOf(1))) {
//
//                                startActivity(new Intent(CreatePemberitahuan.this, Pemberitahuan.class));
//                                finish();
//                                Toast.makeText(CreatePemberitahuan.this, "Pemberitahuan berhasil dikirimkan", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            } else {
//                                String error = jsonObject.optString("message");
//                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
//                                snackbar.show();
//                                dialog.dismiss();
//                            }
//                        } catch (Exception e) {
//                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal parsing data\n", Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                            dialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //You can handle error here if you want
//                        error.printStackTrace();
//                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data\n"+error.getMessage(), Snackbar.LENGTH_LONG);
//                        snackbar.show();
//                        dialog.dismiss();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                try {
//                    //Creating a shared preference
//                    SharedPreferences sharedPreferences = CreatePemberitahuan.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                    //Adding parameters to request
//                    String s = "";
//                    for (int i = 0; i < pushDaftarAbsen.size(); i++) {
//                        if (s.equals("")) {
//                            s = pushDaftarAbsen.get(i);
//                        } else {
//                            s += "," + pushDaftarAbsen.get(i);
//                        }
//                    }
//
//                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
//                    params.put("title", title);
//                    params.put("content", content);
//                    params.put("penerima", s);
//                    return params;
//                } catch (Exception e) {
//                    e.getMessage();
//                    Toast.makeText(ForwardPemberitahuan.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                return params;
//            }
//        };
//
//        //Adding the string request to the queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }


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

            CreatePemberitahuan.MyCustomAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(my.mynato.rahmatridham.mynato.R.layout.listrow_absensi, null);

                holder = new CreatePemberitahuan.MyCustomAdapter.ViewHolder();
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
                holder = (CreatePemberitahuan.MyCustomAdapter.ViewHolder) convertView.getTag();
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
