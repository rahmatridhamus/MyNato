package com.example.rahmatridham.mynato.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Model.SurveyModel;
import com.example.rahmatridham.mynato.R;
import com.example.rahmatridham.mynato.StepCoCActivity.DetailSurvey;
import com.example.rahmatridham.mynato.StepCoCActivity.Step1VisiMisi;
import com.example.rahmatridham.mynato.StepCoCActivity.Step2Motivasi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahmatridham on 3/5/2017.
 */

public class SurveyAdapter extends BaseAdapter {
    Context context;
    ArrayList<SurveyModel> surveyModels;

    public SurveyAdapter(Context context, ArrayList<SurveyModel> surveyModels) {
        this.context = context;
        this.surveyModels = surveyModels;
    }

    @Override
    public int getCount() {
        return surveyModels.size();
    }

    @Override
    public Object getItem(int position) {
        return surveyModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.listrow_survey_pemahaman, parent, false);

        final SurveyModel model = surveyModels.get(position);

        TextView judul = (TextView) v.findViewById(R.id.JudulSurvey);
        TextView tanggal = (TextView) v.findViewById(R.id.tanggalSurvey);
        TextView status = (TextView) v.findViewById(R.id.kerjakanSurvey);
        Button button = (Button) v.findViewById(R.id.butOpenSurvey);

        judul.setText("Survey Pemahaman " + position);
        tanggal.setText(model.getTanggal_mulai());
        if (model.getKeterangan().equals("Open")) {
            status.setText("Kerjakan Survey");
        } else {
            status.setText("Lihat Hasil Pengerjaan");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailSurvey.class);
                intent.putExtra("id_survey",model.getId_survey());
                context.startActivity(intent);
            }
        });
        return v;
    }

}
