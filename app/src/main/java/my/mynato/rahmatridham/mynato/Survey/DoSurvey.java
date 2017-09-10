package my.mynato.rahmatridham.mynato.Survey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import my.mynato.rahmatridham.mynato.Model.JawabanModel;
import my.mynato.rahmatridham.mynato.Model.SoalModel;
import my.mynato.rahmatridham.mynato.R;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoSurvey extends AppCompatActivity {
    TextView namaUjian, soal, itemRemain, timeRemain;
    RadioGroup optJawaban;
    Button next;

    private CountDownTimer countDownTimer; // built in android class
    ArrayList<SoalModel> soalModelArrayList;
    ArrayList<submitAnswerModel> submitAnswerArrayList;

    String id_data_survey = "";
    String id_aktivasi_ujian = "";
    String jsonStringAnswer = "";

    String id_survey = "";
    String id_soal_survey = "";

    int index = 0;
    int timer = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_survey);

        submitAnswerArrayList = new ArrayList<>();

        soal = (TextView) findViewById(R.id.textViewSoal);
        itemRemain = (TextView) findViewById(R.id.textViewSoalRemaining);
        timeRemain = (TextView) findViewById(R.id.strTimeRemain);
        namaUjian = (TextView) findViewById(R.id.textViewNamaUjian);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            String nameExam = (String) b.get("nama_ujian");
            timer = (Integer) b.getInt("durasiSurvey");
            id_survey = (String) b.get("id_survey");
            id_soal_survey = (String) b.get("id_soal_survey");

            namaUjian.setText(nameExam);
        }

        optJawaban = (RadioGroup) findViewById(R.id.listJawaban);
        next = (Button) findViewById(R.id.buttonNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optJawaban.getCheckedRadioButtonId() != -1) {
                    if (index != soalModelArrayList.size()) {
                        //memasukkan id_pertanyaan dan id_jawaban yang dipilih ke array submitAnswerArrayList
                        submitAnswerArrayList.add(new submitAnswerModel(soalModelArrayList.get(index - 1).getId_pertanyaan(), soalModelArrayList.get(index - 1).getJawabanModelArrayList().get(optJawaban.getCheckedRadioButtonId()).getId_jawaban()));
                        doSurvey(soalModelArrayList.get(index));
                        optJawaban.clearCheck();
                        itemRemain.setText((index) + " OF " + soalModelArrayList.size());
                    } else {
                        if (submitAnswerArrayList.size() != soalModelArrayList.size()) {
                            submitAnswerArrayList.add(new submitAnswerModel(soalModelArrayList.get(index - 1).getId_pertanyaan(), soalModelArrayList.get(index - 1).getJawabanModelArrayList().get(optJawaban.getCheckedRadioButtonId()).getId_jawaban()));
                        }
                        next.setText("Submit");
                        next.setBackgroundColor(getResources().getColor(R.color.deepGreenButton));
//                        Toast.makeText(v.getContext(), "FINISH!!!", Toast.LENGTH_SHORT).show();

//                        String s = "";
//                        for (int i = 0; i < submitAnswerArrayList.size(); i++) {
//                            s += submitAnswerArrayList.get(i);
//                        }

                        Gson gson = new GsonBuilder().create();
                        JsonArray arrayAns = gson.toJsonTree(submitAnswerArrayList).getAsJsonArray();
                        JsonObject object = new JsonObject();
                        object.add("data_jawaban", arrayAns);
//                        Toast.makeText(v.getContext(), object.toString(), Toast.LENGTH_SHORT).show();
                        jsonStringAnswer = object.toString();

                        pushJawabanSurvey(id_data_survey, id_aktivasi_ujian, jsonStringAnswer);

                    }
                } else {
                    Toast.makeText(v.getContext(), "Pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSurvey(id_survey, id_soal_survey);
    }

    void startTimer(int minutes) {
        int time = minutes * 60 * 1000;
        CountDownTimer timer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                timeRemain.setText("WAKTU TERSISA " + String.format("%d:%d:%d", hours, minutes, seconds));
            }

            public void onFinish() {
                timeRemain.setText("WAKTU HABIS");
                timeRemain.setTextColor(getResources().getColor(R.color.red));
                pushJawabanSurvey(id_data_survey, id_aktivasi_ujian, jsonStringAnswer);

            }
        };
        timer.start();
    }

    public void getSurvey(final String id_survey, final String id_soal_survey) {
        final ProgressDialog dialog = ProgressDialog.show(DoSurvey.this, "", "Loading. Please wait...", true);
        soalModelArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Survey_Pemahaman/do_survey",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {
//                            if (true) {
                                JSONObject data = jsonObject.getJSONObject("data");

                                id_data_survey = data.optString("id_data_survey", "");
                                id_aktivasi_ujian = data.optString("id_aktivasi_ujian", "");

                                JSONArray dataSoal = data.getJSONArray("data_soal");
                                for (int i = 0; i < dataSoal.length(); i++) {
                                    ArrayList<JawabanModel> jawabanArrayList = new ArrayList<>();
                                    JSONObject object = dataSoal.getJSONObject(i);
                                    JSONArray listJawaban = object.getJSONArray("jawaban");
                                    for (int j = 0; j < listJawaban.length(); j++) {
                                        JSONObject objectJawaban = listJawaban.getJSONObject(j);
                                        JawabanModel jawabanModel = new JawabanModel(objectJawaban.optString("id_jawaban", ""), objectJawaban.optString("nama_jawaban", ""));
                                        jawabanArrayList.add(jawabanModel);
                                    }
                                    SoalModel soalModel = new SoalModel(object.optString("id_pertanyaan", ""), object.optString("pertanyaan", ""), jawabanArrayList);
                                    soalModelArrayList.add(soalModel);
                                }
                                dialog.dismiss();
                                itemRemain.setText("1 OF " + soalModelArrayList.size());
                                startTimer(timer);
                                doSurvey(soalModelArrayList.get(index));
                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(DoSurvey.this, error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                                startActivity(new Intent(DoSurvey.this, Survey.class));
                                finish();
                            }
                        } catch (Exception e) {
//                            Toast.makeText(DoSurvey.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();
                            startActivity(new Intent(DoSurvey.this, Survey.class));
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        error.printStackTrace();
//                        Toast.makeText(DoSurvey.this, "error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal menerima data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                        startActivity(new Intent(DoSurvey.this, Survey.class));
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DoSurvey.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_survey", id_survey);
                    params.put("id_soal_survey", id_soal_survey);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DoSurvey.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(DoSurvey.this);
        requestQueue.add(stringRequest);
    }

    public void pushJawabanSurvey(final String id_data_survey, final String id_aktivasi_ujian, final String arrayJawaban) {
        final ProgressDialog dialog = ProgressDialog.show(DoSurvey.this, "", "Loading. Please wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MAIN_URL + "Survey_Pemahaman/submit_jawaban/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(DoSurvey.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("status").trim();
                            if (status.equals(String.valueOf(1))) {

                                dialog.dismiss();
                                Intent intent = new Intent(DoSurvey.this, Survey.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(DoSurvey.this, "Jawaban berhasil disubmit", Toast.LENGTH_SHORT).show();

                            } else {
                                String error = jsonObject.optString("message");
//                                Toast.makeText(DoSurvey.this, error, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
//                            Toast.makeText(DoSurvey.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
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
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Gagal mengirim data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                try {
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = DoSurvey.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Adding parameters to request
                    params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
                    params.put("id_data_survey", id_data_survey);
                    params.put("id_aktivasi_ujian", id_aktivasi_ujian);
                    params.put("jawaban", arrayJawaban);
                    return params;
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(DoSurvey.this, "error: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void doSurvey(SoalModel soalModel) {
        soal.setText(soalModel.getPertanyaan());
        setQuestionView(soalModel.getJawabanModelArrayList());
        index++;
    }

    public void setQuestionView(ArrayList<JawabanModel> optionAnswer) {
        optJawaban.removeAllViews();

        for (int row = 0; row < 1; row++) {
            optJawaban.setOrientation(LinearLayout.VERTICAL);
            optJawaban.setGravity(LinearLayout.SHOW_DIVIDER_MIDDLE);
            float density = getResources().getDisplayMetrics().density;

            for (int i = 1; i <= optionAnswer.size(); i++) {
                RadioButton rdbtn = new RadioButton(this, null, R.attr.radioButtonStyle);
//                rdbtn.setBackgroundResource(R.drawable.border_radio);
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                int margin = (int) (6 * density);
                params.setMargins(0, margin, 0, margin);
                rdbtn.setLayoutParams(params);
                rdbtn.setId((row * 2) + (i - 1));
                rdbtn.setText(optionAnswer.get((i - 1)).getNama_jawaban());
                rdbtn.setTextColor(getResources().getColor(R.color.darker_grayText));
                rdbtn.setTextSize(14);
                rdbtn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
                optJawaban.addView(rdbtn);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        logout();
    }

    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Anda yakin keluar dari survey ini? semua data akan otomatis dimasukkan ke server");
        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                //Starting login activity
                pushJawabanSurvey(id_data_survey, id_aktivasi_ujian, jsonStringAnswer);

            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public class submitAnswerModel {
        String id_pertanyaan, id_jawaban;

        public submitAnswerModel(String id_pertanyaan, String id_jawaban) {
            this.id_pertanyaan = id_pertanyaan;
            this.id_jawaban = id_jawaban;
        }

        public String getId_pertanyaan() {
            return id_pertanyaan;
        }

        public void setId_pertanyaan(String id_pertanyaan) {
            this.id_pertanyaan = id_pertanyaan;
        }

        public String getId_jawaban() {
            return id_jawaban;
        }

        public void setId_jawaban(String id_jawaban) {
            this.id_jawaban = id_jawaban;
        }

        @Override
        public String toString() {
            return "id_pertanyaan='" + id_pertanyaan +
                    ", id_jawaban='" + id_jawaban + '\n';
        }
    }


}
