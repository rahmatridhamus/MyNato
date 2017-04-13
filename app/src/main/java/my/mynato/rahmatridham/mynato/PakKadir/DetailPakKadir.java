package my.mynato.rahmatridham.mynato.PakKadir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;
import my.mynato.rahmatridham.mynato.StepCoCActivity.Step2Motivasi;
import my.mynato.rahmatridham.mynato.StepCoCActivity.Step3TataNilai;

public class DetailPakKadir extends AppCompatActivity {
    CheckBox gotIt;
    TextView title,content;
    Button lanjutkan;
    String id_pak_kadir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pak_kadir);

        gotIt = (CheckBox) findViewById(R.id.checkBoxPakKadir);
        title = (TextView) findViewById(R.id.titleDetail);
        content = (TextView) findViewById(R.id.descPakKadir);
        lanjutkan = (Button) findViewById(R.id.buttonLanjutkan);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            title.setText((String) b.get("getTitle"));
            content.setText((String) b.get("getContent"));
            id_pak_kadir = (String) b.get("getId_pak_kadir");
        }

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gotIt.isChecked()){
                    pushPakKadir(id_pak_kadir);
                }else {
                    Toast.makeText(DetailPakKadir.this, "Mohon dichecklist terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void pushPakKadir(String id_pak_kadir) {
        final ProgressDialog dialog = ProgressDialog.show(DetailPakKadir.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "PakKadir/senddata/" + id_pak_kadir,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Step2Motivasi.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {

                                Intent intent = new Intent(DetailPakKadir.this, PakKadir.class);
                                startActivity(intent);
                                finish();

                                dialog.dismiss();
                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(DetailPakKadir.this, error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DetailPakKadir.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(DetailPakKadir.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DetailPakKadir.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("status","READ");
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DetailPakKadir.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
