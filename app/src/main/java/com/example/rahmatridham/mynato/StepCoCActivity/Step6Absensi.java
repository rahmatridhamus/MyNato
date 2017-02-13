package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rahmatridham.mynato.LandingPage;
import com.example.rahmatridham.mynato.R;

import java.util.ArrayList;

public class Step6Absensi extends AppCompatActivity {
    Button selesai;
    RelativeLayout screen;
    LinearLayout linearLayout;
    ArrayList<String> list;
    ArrayList<CheckBox> listCheckBox;
    CheckBox checkBox;
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
                getCheckedName();
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

        linearLayout = (LinearLayout) findViewById(R.id.checkAbsensi);
        generateCheckList();
    }

    public void generateCheckList(){
        list = new ArrayList<>();
        listCheckBox = new ArrayList<>();
        list.add("satu");
        list.add("dua");
        list.add("tiga");
        list.add("empat");

        for (int i = 0; i < list.size(); i++) {
            checkBox = new CheckBox(this);
            checkBox.setTextSize(20);
            checkBox.setId(i);
            checkBox.setText(list.get(i));
            checkBox.setOnClickListener(clickingListener(checkBox));
            linearLayout.addView(checkBox);
            listCheckBox.add(checkBox);
        }
    }

    View.OnClickListener clickingListener(final Button button){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Step6Absensi.this, "Mantap", Toast.LENGTH_SHORT).show();
            }
        };
    }

    void getCheckedName(){
        String s = "";
        for (int i = 0; i < listCheckBox.size(); i++) {
            if(listCheckBox.get(i).isChecked()){
                s+=listCheckBox.get(i).getText().toString();
            }
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
