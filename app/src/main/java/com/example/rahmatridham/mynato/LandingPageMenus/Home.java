package com.example.rahmatridham.mynato.LandingPageMenus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rahmatridham.mynato.Config;
import com.example.rahmatridham.mynato.Pemberitahuan.Pemberitahuan;
import com.example.rahmatridham.mynato.R;
import com.example.rahmatridham.mynato.StepCoCActivity.CocVerified;
import com.example.rahmatridham.mynato.StepCoCActivity.PakKadir;
import com.example.rahmatridham.mynato.Survey.Survey;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    CardView cocThematik, cocinsidental, absensi, pemberitahuan, survey, pakKadirun;

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
        pakKadirun = (CardView) view.findViewById(R.id.pakKadir);

        cocThematik.setOnClickListener(this);
        cocinsidental.setOnClickListener(this);
        absensi.setOnClickListener(this);
        pemberitahuan.setOnClickListener(this);
        survey.setOnClickListener(this);
        pakKadirun.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = Home.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int i = v.getId();
        switch (i) {
            case R.id.cocThematik:
                //Creating a shared preference
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.KETERANGAN_SHARED_PREF, "THEMATIK");
                editor.commit();

                Intent intent5 = new Intent(v.getContext(), CocVerified.class);
                startActivity(intent5);
                break;

            case R.id.insidental:
                //Creating a shared preference
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString(Config.KETERANGAN_SHARED_PREF, "INSIDENTAL");
                edit.commit();

                Intent intent6 = new Intent(v.getContext(), CocVerified.class);
                startActivity(intent6);
                break;

            case R.id.absensi:
                Toast.makeText(v.getContext(), "menu absensi clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.pemberitahuan:
                startActivity(new Intent(Home.this.getContext(), Pemberitahuan.class));
                break;

            case R.id.pakKadir:
                startActivity(new Intent(Home.this.getContext(), PakKadir.class));
                break;

            case R.id.survey:
                startActivity(new Intent(Home.this.getContext(), Survey.class));
                break;

        }

    }
}
