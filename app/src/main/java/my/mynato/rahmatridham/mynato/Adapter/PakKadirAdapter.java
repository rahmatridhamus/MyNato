package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.PakKadirModel;
import my.mynato.rahmatridham.mynato.R;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 2/24/2017.
 */

public class PakKadirAdapter extends BaseAdapter {
    ArrayList<PakKadirModel> pakKadirArrayList;
    Context context;

    public PakKadirAdapter(ArrayList<PakKadirModel> pakKadirArrayList, Context context) {
        this.pakKadirArrayList = pakKadirArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pakKadirArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pakKadirArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_pakkadir, parent, false);

        PakKadirModel pakKadirModel = pakKadirArrayList.get(position);

        TextView title = (TextView) v.findViewById(R.id.txtTitlePakkadir);
        TextView tgl = (TextView) v.findViewById(R.id.txtTanggalPakkadir);

        title.setText(pakKadirModel.getTitle());
        tgl.setText(pakKadirModel.getReceived_date());
        return v;
    }
}
