package my.mynato.rahmatridham.mynato;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button signIn;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextpassword);
        signIn = (Button) findViewById(R.id.buttonSignIn);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().equals(null) && !password.getText().equals(null)) {

                    authenticate();
                }

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if (loggedIn) {
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, LandingPage.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }

    void authenticate() {
        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "Loading. Please wait...", true);

        final String email = username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();


        //Creating a string request
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Login/create", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

//                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status").trim();
                    if (status.equals(String.valueOf(1))) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        //Creating a shared preference
                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //Adding values to editor
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                        editor.putString(Config.EMAIL_SHARED_PREF, email);
                        editor.putString(Config.USERID_SHARED_PREF, data.optString("user_id"));
                        editor.putString(Config.NIPEG_SHARED_PREF, data.optString("nipeg"));
                        editor.putString(Config.NAMA_SHARED_PREF, data.optString("nama"));
                        editor.putString(Config.KODEPOSISI_SHARED_PREF, data.optString("kode_posisi"));
                        editor.putString(Config.ROLE_SHARED_PREF, data.optString("role"));
                        editor.putString(Config.JABATAN_SHARED_PREF, data.optString("jabatan"));
                        editor.putString(Config.TOKEN_SHARED_PREF, data.optString("token"));

                        //Saving values to editor
                        editor.commit();

                        dialog.dismiss();
                        //Starting profile activity
                        Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                        startActivity(intent);
                        finish();

                    } else {
                        final String error = jsonObject.optString("message");
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Login gagal, mohon diulangi\n" + error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    final String error = e.getMessage();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Login gagal, mohon diulangi", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();

                        }
                    });

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Login gagal, mohon diulangi", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();

                            }
                        });
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    //Adding parameters to request
                    params.put(Config.KEY_EMAIL, email);
                    params.put(Config.KEY_PASSWORD, password);

                    //returning parameter
                    return params;
                } catch (final Exception e) {

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "errorParam: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
