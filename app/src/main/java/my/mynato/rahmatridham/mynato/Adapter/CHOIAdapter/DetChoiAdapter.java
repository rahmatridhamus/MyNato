package my.mynato.rahmatridham.mynato.Adapter.CHOIAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import my.mynato.rahmatridham.mynato.Model.ChoiDetailFormsModel;
import my.mynato.rahmatridham.mynato.R;

/**
 * Created by rahmatridham on 7/7/2017.
 */

public class DetChoiAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChoiDetailFormsModel> choiDetailFormsModelArrayList;

    public DetChoiAdapter(Context context, ArrayList<ChoiDetailFormsModel> choiDetailFormsModelArrayList) {
        this.context = context;
        this.choiDetailFormsModelArrayList = choiDetailFormsModelArrayList;
    }

    @Override
    public int getCount() {
        return choiDetailFormsModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return choiDetailFormsModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_pakkadir, parent, false);

        ChoiDetailFormsModel model = choiDetailFormsModelArrayList.get(position);

        TextView title = (TextView) v.findViewById(R.id.txtTitlePakkadir);
        TextView tgl = (TextView) v.findViewById(R.id.txtTanggalPakkadir);
        ImageView isRead = (ImageView) v.findViewById(R.id.isReadPemberitahuan);

        if(model.getStatus().equals("PENDING")){
            isRead.setVisibility(View.VISIBLE);
        }else {
            isRead.setVisibility(View.INVISIBLE);
        }

        title.setText(model.getNama_formulir());
        tgl.setVisibility(View.GONE);
        return v;
    }
}
