package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.CoC;
import my.mynato.rahmatridham.mynato.R;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class CocHistAdapter extends BaseAdapter {
    ArrayList<CoC> coCArrayList;
    Context context;

    public CocHistAdapter(ArrayList<CoC> coCArrayList, Context context) {
        this.coCArrayList = coCArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return coCArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coCArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_mycoc, parent, false);

        CoC coC = coCArrayList.get(position);

        TextView judul = (TextView) v.findViewById(R.id.textHeader);
        TextView alamat = (TextView) v.findViewById(R.id.textViewTanggal);
        ImageView view = (ImageView) v.findViewById(R.id.imageList);

        if(coC.getKeterangan_coc().equals("COC INSIDENTAL")){
           view.setImageResource(R.drawable.icon_coc_insidental);
        }else {
            view.setImageResource(R.drawable.icon_coc_jumat);
        }

        judul.setText(coC.getKeterangan_coc());
        alamat.setText(coC.getDate());
        return v;
    }
}
