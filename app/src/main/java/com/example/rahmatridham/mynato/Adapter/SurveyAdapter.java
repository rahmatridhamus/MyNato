package com.example.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rahmatridham.mynato.Model.SurveyModel;
import com.example.rahmatridham.mynato.R;
import com.example.rahmatridham.mynato.Survey.DetailSurvey;

import java.util.ArrayList;

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
