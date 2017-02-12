package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahmatridham.mynato.R;

public class CocVerified extends AppCompatActivity {
    Button next;
    RadioGroup group;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_verified);

        next = (Button) findViewById(R.id.buttonSelanjutnya);
        group = (RadioGroup) findViewById(R.id.radioTipecoc);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CocVerified.this,PrepareAddCoc.class);
                startActivity(intent);
            }
        });
    }
}
