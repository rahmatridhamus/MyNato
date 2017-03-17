package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.PemberitahuanModel;
import my.mynato.rahmatridham.mynato.R;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 3/6/2017.
 */

public class PemberitahuanAdapter extends BaseAdapter {
    Context context;
    ArrayList<PemberitahuanModel> pemberitahuanModelArrayList;

    public PemberitahuanAdapter(Context context, ArrayList<PemberitahuanModel> pemberitahuanModelArrayList) {
        this.context = context;
        this.pemberitahuanModelArrayList = pemberitahuanModelArrayList;
    }

    @Override
    public int getCount() {
        return pemberitahuanModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pemberitahuanModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.listrow_pemberitahuan, parent, false);

        PemberitahuanModel model = pemberitahuanModelArrayList.get(position);

        TextView title = (TextView) v.findViewById(R.id.titlePemberitahuan);
        TextView date = (TextView) v.findViewById(R.id.tanggalPemberitahuan);
        ImageView isRead = (ImageView) v.findViewById(R.id.isReadPemberitahuan);

        title.setText(model.getTitle());
        date.setText(model.getReceived_date());

        if (model.getStatus().equals("SENT")) {
            isRead.setVisibility(View.VISIBLE);
        } else {
            isRead.setVisibility(View.GONE);
        }

        return v;
    }
}
