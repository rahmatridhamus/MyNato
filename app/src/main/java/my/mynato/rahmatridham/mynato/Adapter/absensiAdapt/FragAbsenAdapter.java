package my.mynato.rahmatridham.mynato.Adapter.absensiAdapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import my.mynato.rahmatridham.mynato.Absensi.absensi;
import my.mynato.rahmatridham.mynato.R;

/**
 * Created by rahmatridham on 6/20/2017.
 */

public class FragAbsenAdapter extends BaseAdapter {

    ArrayList<absensi.DataAbsen> dataAbsenArrayList;
    Context context;

    public FragAbsenAdapter(ArrayList<absensi.DataAbsen> dataAbsenArrayList, Context context) {
        this.dataAbsenArrayList = dataAbsenArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataAbsenArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataAbsenArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_absensi_request, parent, false);

        absensi.DataAbsen model = dataAbsenArrayList.get(position);

        TextView tanggal = (TextView) v.findViewById(R.id.textViewTanggalAbsensi);
        TextView status = (TextView) v.findViewById(R.id.textViewStatusAbsensi);

        tanggal.setText(model.getTanggal());
        status.setText(model.getStatus());

        if(model.getKeterangan().isEmpty()){ //masuk di fragment absensi
            if(!model.getStatus().equals("TERLAMBAT")){
                status.setBackgroundResource(R.drawable.border_green);

            }else {
                status.setBackgroundResource(R.drawable.border_red);
            }
        }else{
            if(!model.getStatus().equals("PENDING")){
                status.setBackgroundResource(R.drawable.border_green);
            }else {
                status.setBackgroundResource(R.drawable.border_red);
            }
        }


        return v;
    }
}
