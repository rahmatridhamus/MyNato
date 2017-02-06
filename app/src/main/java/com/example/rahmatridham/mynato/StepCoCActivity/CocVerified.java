package com.example.rahmatridham.mynato.StepCoCActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahmatridham.mynato.R;

public class CocVerified extends AppCompatActivity {

    TextView kontenCOChari,itemRapat;
    CardView adminInput,AnggotaCoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_senin);

        kontenCOChari = (TextView) findViewById(R.id.kontenCocHari);
//        itemRapat = (TextView) findViewById(R.id.kontenCocHari);
        adminInput = (CardView) findViewById(R.id.adminInput);
        AnggotaCoc = (CardView) findViewById(R.id.anggotaCoc);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            String day =(String) b.get("menuClicked");
            kontenCOChari.setText("Konten CoC "+day);
        }

        adminInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CocVerified.this, Step1VisiMisi.class));
            }
        });

        AnggotaCoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CocVerified.this, "testing", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
