package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.rahmatridham.mynato.R;

public class Step5Thematik extends AppCompatActivity {
    Button lanjut;
    RelativeLayout screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step5_thematik);

        screen = (RelativeLayout) findViewById(R.id.activity_step5_thematik);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        lanjut = (Button) findViewById(R.id.buttonLanjutkan);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Step5Thematik.this,Step6Absensi.class);
                startActivity(intent);
            }
        });

        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle("Step 5: Thematik");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
