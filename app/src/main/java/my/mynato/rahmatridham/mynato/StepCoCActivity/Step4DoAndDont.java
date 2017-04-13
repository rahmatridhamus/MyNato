package my.mynato.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import my.mynato.rahmatridham.mynato.Adapter.DoAndDontAdapter;
import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.DoAndDont;
import my.mynato.rahmatridham.mynato.Model.SubDoDont;
import my.mynato.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step4DoAndDont extends AppCompatActivity {
    Button lanjut;
    ArrayList<DoAndDont> doandontArrayList = new ArrayList<>();
    ExpandableListView listView;
    DoAndDontAdapter listAdapter;
    TextView descDodontBef, dipilihDodont;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String doDontSel="",subDoDontSel="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4_do_and_dont);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        sharedPreferences = Step4DoAndDont.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        listView = (ExpandableListView) findViewById(R.id.listDoandDont);
        descDodontBef = (TextView) findViewById(R.id.strDodontSebelum);
        dipilihDodont = (TextView) findViewById(R.id.textViewDipilihDodont);

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subDoDontSel.equals("")) {
                    SharedPreferences sharedPreferences = Step4DoAndDont.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    pushDoandDont(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));

                } else {
                    Toast.makeText(Step4DoAndDont.this, "Checklist untuk melanjutkan", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "test", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 4: Do and Don't");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        listAdapter = new DoAndDontAdapter(this, listDataHeader, listDataChild);
        listView.setAdapter(listAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);

                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setMessage("Do" + ": \n" + doandontArrayList.get(groupPosition).getSubDoDonts().get(childPosition).getDost() + "\n\nDont: \n" + doandontArrayList.get(groupPosition).getSubDoDonts().get(childPosition).getDont())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dipilihDodont.setText("dipilih: "+doandontArrayList.get(groupPosition).getSubtit().get(childPosition)+", "+listDataHeader.get(groupPosition));
                                dipilihDodont.setTextColor(getResources().getColor(R.color.holo_green));
                                doDontSel = doandontArrayList.get(groupPosition).getId_do_and_dont();
                                subDoDontSel = doandontArrayList.get(groupPosition).getSubDoDonts().get(childPosition).getId_sub_do_and_dont();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        getDoAndDont(sharedPreferences.getString(Config.IDGROUPCOC_SHARED_PREF, ""));

    }

    private void getDoAndDont(String idGroup) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(Step4DoAndDont.this, "", "Loading. Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_and_Dont/get_data/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");

                                //array of Do and Dont
                                JSONArray list = data.getJSONArray("list");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    JSONArray subtit = object.getJSONArray("subtitle");
                                    ArrayList<SubDoDont> subtitleArrayList = new ArrayList<>();
                                    if (!object.isNull("subtitle")) {
                                        for (int j = 0; j < subtit.length(); j++) {
                                            JSONObject objSubtit = subtit.getJSONObject(j);
                                            subtitleArrayList.add(new SubDoDont(objSubtit.optString("id_sub_do_and_dont", ""), objSubtit.optString("sub_title", ""), objSubtit.optString("do", ""), objSubtit.optString("dont", "")));
                                        }
                                    }
                                    DoAndDont dont = new DoAndDont(object.optString("id_do_and_dont", ""), object.optString("title", ""), subtitleArrayList);
                                    doandontArrayList.add(dont);
                                }
                                prepare(doandontArrayList);

                                JSONObject history = data.getJSONObject("history");
                                descDodontBef.setText("Pertemuan Sebelumnya: \n" + history.optString("title","(kosong)"));
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(Step4DoAndDont.this, error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();

                            }
                        } catch (Exception e) {
//                            Toast.makeText(Step4DoAndDont.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error: \n" + e.getMessage(), Snackbar.LENGTH_LONG);
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
//                        Toast.makeText(Step4DoAndDont.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error: \n" + error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step4DoAndDont.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
//                    Toast.makeText(Step4DoAndDont.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error: \n" + e.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void prepare(ArrayList<DoAndDont> doAndDonts) {

        for (int i = 0; i < doAndDonts.size(); i++) {
            listDataHeader.add(doAndDonts.get(i).getTitle());
        }
        for (int i = 0; i < listDataHeader.size(); i++) {
            listDataChild.put(listDataHeader.get(i), doAndDonts.get(i).getSubtit());
        }
        listAdapter.notifyDataSetChanged();
    }

    public void pushDoandDont(String idGroup) {
        final ProgressDialog dialog = ProgressDialog.show(Step4DoAndDont.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Do_CoC/set_do_and_dont/" + idGroup,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Step4DoAndDont.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                if(sharedPreferences.getString(Config.KETERANGAN_SHARED_PREF,"").equals("INSIDENTAL")){
                                    Intent intent = new Intent(Step4DoAndDont.this, Step5Insidental.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(Step4DoAndDont.this, Step5Thematik.class);
                                    startActivity(intent);
                                }
                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(Step4DoAndDont.this, "responseError\n"+error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "responseError\n"+error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
//                            Toast.makeText(Step4DoAndDont.this, "errorJSON: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "errorJSON: \n" + e.getMessage(), Snackbar.LENGTH_LONG);
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
//                        Toast.makeText(Step4DoAndDont.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error: \n" + error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Step4DoAndDont.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_do_and_dont",doDontSel);
                    params.put("id_sub_do_and_dont",subDoDontSel);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
//                    Toast.makeText(Step4DoAndDont.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "error: \n" + e.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
