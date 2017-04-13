package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import my.mynato.rahmatridham.mynato.Model.AbsensiModel;
import my.mynato.rahmatridham.mynato.Model.PenerimaForwarder;
import my.mynato.rahmatridham.mynato.R;

/**
 * Created by rahmatridham on 3/20/2017.
 */

public class ListForwarderAdapter extends BaseAdapter {
    Context context;
    public ArrayList<PenerimaForwarder> penerimaForwarders;

    public ListForwarderAdapter(Context context, ArrayList<PenerimaForwarder> absensiModels) {
        this.context = context;
        this.penerimaForwarders = absensiModels;
    }

    @Override
    public int getCount() {
        return penerimaForwarders.size();
    }

    @Override
    public Object getItem(int position) {
        return penerimaForwarders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.listrow_absensi, parent, false);

        PenerimaForwarder model = penerimaForwarders.get(position);

        CheckBox box = (CheckBox) v.findViewById(R.id.checkBoxAbsensi);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                PenerimaForwarder penerimaForwarder = (PenerimaForwarder) checkBox.getTag();
                penerimaForwarder.setSelected(checkBox.isChecked());
            }
        });

        box.setText(model.getNama_jabatan());
        return v;
    }
}
