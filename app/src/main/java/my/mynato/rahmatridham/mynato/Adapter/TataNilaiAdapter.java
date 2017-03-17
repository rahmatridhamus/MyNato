package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.TataNilai;
import my.mynato.rahmatridham.mynato.R;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 2/23/2017.
 */

public class TataNilaiAdapter extends BaseAdapter {
    ArrayList<TataNilai> tataNilaiArrayList;
    Context context;

    public TataNilaiAdapter(ArrayList<TataNilai> tataNilaiArrayList, Context context) {
        this.tataNilaiArrayList = tataNilaiArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tataNilaiArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return tataNilaiArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_mycoc, parent, false);

        TataNilai tataNilai = tataNilaiArrayList.get(position);

        TextView judul = (TextView) v.findViewById(R.id.textView4judulTanil);

        judul.setText(tataNilai.getTitle());
        return v;
    }
}
