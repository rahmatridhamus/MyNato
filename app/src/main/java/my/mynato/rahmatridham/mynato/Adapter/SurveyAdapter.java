package my.mynato.rahmatridham.mynato.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.SurveyModel;
import my.mynato.rahmatridham.mynato.R;
import my.mynato.rahmatridham.mynato.Survey.DetailSurvey;

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
        View v = inflater.inflate(my.mynato.rahmatridham.mynato.R.layout.listrow_survey_pemahaman, parent, false);

        final SurveyModel model = surveyModels.get(position);

        TextView judul = (TextView) v.findViewById(my.mynato.rahmatridham.mynato.R.id.JudulSurvey);
        TextView tanggal = (TextView) v.findViewById(my.mynato.rahmatridham.mynato.R.id.tanggalSurvey);
        TextView status = (TextView) v.findViewById(my.mynato.rahmatridham.mynato.R.id.kerjakanSurvey);
        RelativeLayout relayClickSurvey = (RelativeLayout) v.findViewById(my.mynato.rahmatridham.mynato.R.id.relayClickSurvey);
        Button button = (Button) v.findViewById(my.mynato.rahmatridham.mynato.R.id.butOpenSurvey);
        judul.setText("Survey Pemahaman " + position);
        tanggal.setText(model.getTanggal_mulai());
        if (model.getKeterangan().equals("Open")) {
            status.setText("Kerjakan Survey");
            button.setBackgroundResource(R.drawable.border_green);

        } else {
            button.setBackgroundResource(R.drawable.border_red);
            button.setText("Closed");
            status.setText("Lihat Hasil Pengerjaan");
        }

        relayClickSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailSurvey.class);
                intent.putExtra("id_survey", model.getId_survey());
                intent.putExtra("keterangan_survey", model.getKeterangan());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        return v;
    }

}
