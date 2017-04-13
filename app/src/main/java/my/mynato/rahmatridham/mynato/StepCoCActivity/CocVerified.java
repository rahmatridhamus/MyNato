package my.mynato.rahmatridham.mynato.StepCoCActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.GroupCoc;
import my.mynato.rahmatridham.mynato.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CocVerified extends AppCompatActivity {
    Button next;
    RadioGroup group;
    ArrayList<GroupCoc> groupCocs = new ArrayList<>();
    GroupCoc groupCoc;
    boolean isCheck = false;
    TextView judul;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_verified);
        sharedPreferences = CocVerified.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        next = (Button) findViewById(R.id.buttonSelanjutnya);
        group = (RadioGroup) findViewById(R.id.radioTipecoc);
        judul = (TextView) findViewById(R.id.kontenCocHari);
        if (sharedPreferences.getString(Config.KETERANGAN_SHARED_PREF, "").equals("INSIDENTAL")) {
            judul.setText("CoC Insidental");
        } else {
            judul.setText("CoC Thematik");
        }


        next.setEnabled(isCheck);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CocVerified.this, PrepareAddCoc.class);

                //Creating a shared preference
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.IDGROUPCOC_SHARED_PREF, groupCoc.getId_group_coc());
                editor.commit();

                startActivity(intent);
                next.setEnabled(isCheck);
                next.setClickable(isCheck);
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Toast.makeText(CocVerified.this, "id: " + groupCocs.get(checkedId).getId_group_coc(), Toast.LENGTH_SHORT).show();
                groupCoc = groupCocs.get(checkedId);
                isCheck = true;
                next.setEnabled(isCheck);
                next.setClickable(isCheck);

            }
        });

        getGroupCoc();

    }

    public void addRadioButtons(ArrayList<GroupCoc> groupCocs) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_checked }, // unchecked
                        new int[] {  android.R.attr.state_checked }  // checked
                },
                new int[] {
                        getResources().getColor(R.color.greyBorder),
                        getResources().getColor(R.color.warnaijo)
                }
        );

        ColorStateList colorStateListbg = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {getResources().getColor(R.color.colorPrimary) }
        );

        for (int row = 0; row < 1; row++) {
            group.setOrientation(LinearLayout.VERTICAL);
            group.setGravity(LinearLayout.SHOW_DIVIDER_MIDDLE);
            float density = getResources().getDisplayMetrics().density;

            for (int i = 1; i <= groupCocs.size(); i++) {
                AppCompatRadioButton rdbtn = new AppCompatRadioButton(this);
                rdbtn.setBackgroundResource(R.drawable.border_radio);
//                rdbtn.setHighlightColor(getResources().getColor(R.color.warnaijo));
//                rdbtn.setBackgroundTintList(colorStateListbg);
//                rdbtn.setSupportButtonTintList(colorStateList);
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                int margin = (int) (6 * density);
                params.setMargins(0, margin, 0, margin);
                rdbtn.setLayoutParams(params);
                rdbtn.setId((row * 2) + (i - 1));
                rdbtn.setText(groupCocs.get((i - 1)).getNama_group_coc());
                rdbtn.setTextSize(18);
                group.addView(rdbtn);
            }
        }
    }

    private void getGroupCoc() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(CocVerified.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Group_Coc", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();

                    if (status.equals(String.valueOf(1))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            GroupCoc groupCoc = new GroupCoc(object.optString("id"), object.optString("id_group_coc"), object.optString("nama_group_coc"));
                            groupCocs.add(groupCoc);
                        }
                        addRadioButtons(groupCocs);
                        dialog.dismiss();
                    } else {
                        String error = jsonObject.optString("message");
                        Toast.makeText(CocVerified.this, "errorMessage: \n" + error, Toast.LENGTH_SHORT).show();
                        Snackbar snackbars = Snackbar.make(findViewById(android.R.id.content), "Gagal mendapatkan data", Snackbar.LENGTH_LONG);
                        snackbars.show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(CocVerified.this, "error Response: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mendapatkan data. Periksa kembali internet." + e.getMessage(), Snackbar.LENGTH_LONG);
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
//                        Toast.makeText(CocVerified.this, "error getting response: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error getting response: \n" + error.getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = CocVerified.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
//                    params.put(Config.KODEPOSISI_SHARED_PREF, sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF, ""));
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));

                    return params;
                } catch (Exception e) {
                    e.getMessage();
//                    Toast.makeText(CocVerified.this, "error param: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "error param: \n" + e.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
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
