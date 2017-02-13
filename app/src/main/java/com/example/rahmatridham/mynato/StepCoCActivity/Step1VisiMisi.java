package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahmatridham.mynato.R;

public class Step1VisiMisi extends AppCompatActivity {

    RelativeLayout screen;
    Button lanjut;
    CheckBox cbVisi,cbMisi;
    TextView txtVisi,txtMisi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1_visi_misi);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 1: Visi & Misi");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        screen = (RelativeLayout) findViewById(R.id.activity_step1_visi_misi);
        cbVisi = (CheckBox) findViewById(R.id.checkBoxVisi);
        cbMisi = (CheckBox) findViewById(R.id.checkBoxMisi);
        txtVisi = (TextView) findViewById(R.id.descVisi);
        txtMisi = (TextView) findViewById(R.id.descMisi);

        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Step1VisiMisi.this,Step2Motivasi.class);
                startActivity(intent);
            }
        });

    }
}
