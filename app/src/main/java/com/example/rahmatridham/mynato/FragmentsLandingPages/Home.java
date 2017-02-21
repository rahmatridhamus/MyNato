package com.example.rahmatridham.mynato.FragmentsLandingPages;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.R;
import com.example.rahmatridham.mynato.StepCoCActivity.CocVerified;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    CardView cocThematik, cocinsidental, absensi, pemberitahuan, survey;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cocThematik = (CardView) view.findViewById(R.id.cocThematik);
        cocinsidental = (CardView) view.findViewById(R.id.insidental);
        absensi = (CardView) view.findViewById(R.id.absensi);
        pemberitahuan = (CardView) view.findViewById(R.id.pemberitahuan);
        survey = (CardView) view.findViewById(R.id.survey);

        cocThematik.setOnClickListener(this);
        cocinsidental.setOnClickListener(this);
        absensi.setOnClickListener(this);
        pemberitahuan.setOnClickListener(this);
        survey.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.cocThematik:
                Intent intent5 = new Intent(v.getContext(), CocVerified.class);
                intent5.putExtra(Config.KETERANGAN_SHARED_PREF, "THEMATIK");
                startActivity(intent5);
                break;

            case R.id.insidental:
                Intent intent6 = new Intent(v.getContext(), CocVerified.class);
                intent6.putExtra(Config.IDGROUPCOC_SHARED_PREF, "INSIDENTAL");
                startActivity(intent6);
                break;

            case R.id.absensi:
                Toast.makeText(v.getContext(), "menu absensi clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.pemberitahuan:
                Toast.makeText(v.getContext(), "menu pemberitahuan clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.pakKadir:
                Toast.makeText(v.getContext(), "menu pakKadir clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.survey:
                Toast.makeText(v.getContext(), "menu survey clicked", Toast.LENGTH_SHORT).show();
                break;


        }

    }
}
