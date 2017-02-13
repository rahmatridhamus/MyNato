package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
                Intent intent = new Intent(CocVerified.this, PrepareAddCoc.class);
                startActivity(intent);
            }
        });

        addRadioButtons(3);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(CocVerified.this, "Changed: "+checkedId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addRadioButtons(int number) {

        for (int row = 0; row < 1; row++) {
            group = new RadioGroup(this);
            group.setOrientation(LinearLayout.VERTICAL);
            group.setGravity(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for (int i = 1; i <= number; i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId((row * 2) + i);
                rdbtn.setText("Radio " + rdbtn.getId());
                rdbtn.setTextSize(16);
                group.addView(rdbtn);
            }
            ((ViewGroup) findViewById(R.id.radioTipecoc)).addView(group);
        }
    }
}
