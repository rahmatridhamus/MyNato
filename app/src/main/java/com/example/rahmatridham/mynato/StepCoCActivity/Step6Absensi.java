package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.rahmatridham.mynato.LandingPage;
import com.example.rahmatridham.mynato.R;

public class Step6Absensi extends AppCompatActivity {
    Button selesai;
    RelativeLayout screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step6_absensi);

        screen = (RelativeLayout) findViewById(R.id.activity_step6_absensi);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        selesai = (Button) findViewById(R.id.buttonSelesai);
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Step6Absensi.this,LandingPage.class);
                startActivity(intent);
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 6: Absensi");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
