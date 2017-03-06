package com.example.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.rahmatridham.mynato.Model.AbsensiModel;
import com.example.rahmatridham.mynato.Model.PakKadirModel;
import com.example.rahmatridham.mynato.R;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 3/3/2017.
 */

public class AbsensiAdapter extends BaseAdapter {
    Context context;
    public ArrayList<AbsensiModel> absensiModels;

    public AbsensiAdapter(Context context, ArrayList<AbsensiModel> absensiModels) {
        this.context = context;
        this.absensiModels = absensiModels;
    }

    @Override
    public int getCount() {
        return absensiModels.size();
    }

    @Override
    public Object getItem(int position) {
        return absensiModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.listrow_absensi, parent, false);

        AbsensiModel model = absensiModels.get(position);

        CheckBox box = (CheckBox) v.findViewById(R.id.checkBoxAbsensi);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                AbsensiModel absensiModel = (AbsensiModel) checkBox.getTag();
                absensiModel.setSelected(checkBox.isChecked());
            }
        });

        box.setText(model.getNama());
        return v;
    }
}
