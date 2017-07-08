package my.mynato.rahmatridham.mynato.LandingPageMenus;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.PemberitahuanModel;
import my.mynato.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;
import my.mynato.rahmatridham.mynato.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    TextView nama, jabatan, lokasi;
    CircularImageView profile;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nama = (TextView) view.findViewById(R.id.NamaProfil);
        jabatan = (TextView) view.findViewById(R.id.JabatanProfil);
        lokasi = (TextView) view.findViewById(R.id.kantorProfil);
        profile = (CircularImageView) view.findViewById(R.id.circularImageView);

        getPemberitahuan();
        return view;
    }

    private void getPemberitahuan() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                JSONObject object = data.getJSONObject(0);

                                nama.setText(object.optString("nama",""));
                                jabatan.setText(object.optString("nama_jabatan",""));
                                lokasi.setText(object.optString("nama_kantor",""));
                                Picasso.with(Profile.this.getContext()).load(object.optString("url_foto","")).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        profile.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        profile.setImageResource(R.drawable.icon_profile_active);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });

                            } else {
                                String error = jsonObject.optString("message");
                                Toast.makeText(Profile.this.getContext(), "Gagal menerima data profil \n"+error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Profile.this.getContext(), "Gagal menerima data profil \n"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Profile.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        Toast.makeText(Profile.this.getContext(), "Gagal menerima data profil", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Profile.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
//                    Toast.makeText(Profile.this.getContext(), "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(Profile.this.getContext(), "Gagal menerima data profil", Toast.LENGTH_SHORT).show();

                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this.getContext());
        requestQueue.add(stringRequest);
    }

}
