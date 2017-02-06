package com.example.rahmatridham.mynato.FragmentsLandingPages;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rahmatridham.mynato.R;
import com.example.rahmatridham.mynato.StepCoCActivity.CocVerified;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener{

    CardView cocsenin, cocselasa, cocrabu, cockamis, cocjumat, cocinsidental, absensi, pemberitahuan;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cocsenin = (CardView) view.findViewById(R.id.adminInput);
        cocselasa = (CardView) view.findViewById(R.id.cocSelasa);
        cocrabu = (CardView) view.findViewById(R.id.cocRabu);
        cockamis = (CardView) view.findViewById(R.id.cocKamis);
        cocjumat = (CardView) view.findViewById(R.id.cocJumat);
        cocinsidental = (CardView) view.findViewById(R.id.insidental);
        absensi = (CardView) view.findViewById(R.id.absensi);
        pemberitahuan = (CardView) view.findViewById(R.id.pemberitahuan);

        cocsenin.setOnClickListener(this);
        cocselasa.setOnClickListener(this);
        cocrabu.setOnClickListener(this);
        cockamis.setOnClickListener(this);
        cocjumat.setOnClickListener(this);
        cocinsidental.setOnClickListener(this);
        absensi.setOnClickListener(this);
        pemberitahuan.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i){
            case R.id.adminInput:
                Intent intent = new Intent(v.getContext(),CocVerified.class);
                intent.putExtra("menuClicked","Senin");
                startActivity(intent);
                break;
            case R.id.cocSelasa:
                Intent intent1 = new Intent(v.getContext(),CocVerified.class);
                intent1.putExtra("menuClicked","Selasa");
                startActivity(intent1);
                break;

            case R.id.cocRabu:
                Intent intent3 = new Intent(v.getContext(),CocVerified.class);
                intent3.putExtra("menuClicked","Rabu");
                startActivity(intent3);
                break;

            case R.id.cocKamis:
                Intent intent4 = new Intent(v.getContext(),CocVerified.class);
                intent4.putExtra("menuClicked","Kamis");
                startActivity(intent4);
                break;

            case R.id.cocJumat:
                Intent intent5 = new Intent(v.getContext(),CocVerified.class);
                intent5.putExtra("menuClicked","Jumat");
                startActivity(intent5);
                break;

            case R.id.insidental:
                Intent intent6 = new Intent(v.getContext(),CocVerified.class);
                intent6.putExtra("menuClicked","insidental");
                startActivity(intent6);
                break;

            case R.id.absensi:
                Intent intent7 = new Intent(v.getContext(),CocVerified.class);
                intent7.putExtra("menuClicked","absensi");
                startActivity(intent7);
                break;

            case R.id.pemberitahuan:
                Intent intent8 = new Intent(v.getContext(),CocVerified.class);
                intent8.putExtra("menuClicked","senin");
                startActivity(intent8);
                break;

            case R.id.pakKadir:
                Intent intent9 = new Intent(v.getContext(),CocVerified.class);
                intent9.putExtra("menuClicked","pakKadir");
                startActivity(intent9);
                break;

            case R.id.survey:
                Intent intent10 = new Intent(v.getContext(),CocVerified.class);
                intent10.putExtra("menuClicked","Survey");
                startActivity(intent10);
                break;


        }

    }
}
