package my.mynato.rahmatridham.mynato.Absensi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.Model.PenerimaForwarder;
import my.mynato.rahmatridham.mynato.Pemberitahuan.ForwardPemberitahuan;
import my.mynato.rahmatridham.mynato.R;

public class RequestKoreksi extends AppCompatActivity {
    RadioGroup alkor;
    RadioButton luarUnit, absenProblem;
    EditText masuk, keluar, tanggal;
    Button submit;
    TextView nama, nipeg, kantor, atasan;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_koreksi);
        this.setTitle("Request Koreksi Absensi");

        alkor = (RadioGroup) findViewById(R.id.groupAlkor);
        luarUnit = (RadioButton) findViewById(R.id.radioButtonUnitLuar);
        absenProblem = (RadioButton) findViewById(R.id.radioButtonMAB);
        nama = (TextView) findViewById(R.id.textViewNamaPemohon);
        nipeg = (TextView) findViewById(R.id.textViewNipegPemohon);
        kantor = (TextView) findViewById(R.id.textViewKantorPemohon);
        atasan = (TextView) findViewById(R.id.textViewAtasanPemohon);
        masuk = (EditText) findViewById(R.id.editTextMasuk);
        keluar = (EditText) findViewById(R.id.editTextKeluar);
        tanggal = (EditText) findViewById(R.id.editTextTanggalKoreksi);
        submit = (Button) findViewById(R.id.buttonSubmit);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RequestKoreksi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String strSelHour;
                        String strSelMin;
                        if (selectedHour < 10) {
                            strSelHour = "0" + selectedHour;
                        } else {
                            strSelHour = "" + selectedHour;
                        }

                        if (selectedMinute < 10) {
                            strSelMin = "0" + selectedMinute;
                        } else {
                            strSelMin = "" + selectedMinute;
                        }
                        masuk.setText(strSelHour + ":" + strSelMin);
//                        masuk.setEnabled(false);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RequestKoreksi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String strSelHour;
                        String strSelMin;
                        if (selectedHour < 10) {
                            strSelHour = "0" + selectedHour;
                        } else {
                            strSelHour = "" + selectedHour;
                        }

                        if (selectedMinute < 10) {
                            strSelMin = "0" + selectedMinute;
                        } else {
                            strSelMin = "" + selectedMinute;
                        }
                        keluar.setText(strSelHour + ":" + strSelMin);
//                        keluar.setEnabled(false);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        tanggal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RequestKoreksi.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masuk.getText().toString().equals("") || keluar.getText().toString().equals("") || tanggal.getText().toString().equals("") || alkor.getCheckedRadioButtonId() == (-1)) {
                    Toast.makeText(RequestKoreksi.this, "Mohon dilengkapi kolom yang disediakan", Toast.LENGTH_SHORT).show();

                } else {
                    if (luarUnit.isChecked()) {
                        submitRequest(masuk.getText().toString(), keluar.getText().toString(), tanggal.getText().toString(), "ACARA DI UNIT LUAR KANTOR KEDUDUKAN");
                    } else if (absenProblem.isChecked()) {
                        submitRequest(masuk.getText().toString(), keluar.getText().toString(), tanggal.getText().toString(), "MESIN ABSEN BERMASALAH");
                    } else {
                        Toast.makeText(RequestKoreksi.this, "Mohon dilengkapi kolom yang disediakan coy ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getData();
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tanggal.setText(sdf.format(myCalendar.getTime()));
    }

    private void getData() {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(RequestKoreksi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/form_request",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                nama.setText(": " + data.optString("nama", "null"));
                                nipeg.setText(": " + data.optString("nipeg", "null"));
                                kantor.setText(": " + data.optString("nama_kantor", "null"));
                                atasan.setText(": " + data.optString("nama_atasan", "null"));
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
                    SharedPreferences sharedPreferences = RequestKoreksi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(RequestKoreksi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void submitRequest(final String masuk, final String keluar, final String tanggal, final String ket) {
        //Creating a string request
        final ProgressDialog dialog = ProgressDialog.show(RequestKoreksi.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Absensi/send_request",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
                                Toast.makeText(RequestKoreksi.this, "Submit data berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RequestKoreksi.this, AbsensiPage.class));
                                finish();
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
                    SharedPreferences sharedPreferences = RequestKoreksi.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("masuk_seharusnya", masuk);
                    params.put("pulang_seharusnya", keluar);
                    params.put("tanggal_dikoreksi", tanggal);
                    params.put("keterangan", ket);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(RequestKoreksi.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
