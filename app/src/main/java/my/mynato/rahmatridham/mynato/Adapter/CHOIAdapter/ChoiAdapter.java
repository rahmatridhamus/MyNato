package my.mynato.rahmatridham.mynato.Adapter.CHOIAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import my.mynato.rahmatridham.mynato.Absensi.ApprovalKoreksi;
import my.mynato.rahmatridham.mynato.Absensi.absensi;
import my.mynato.rahmatridham.mynato.CHOI.PernyataanBudayaPerilaku;
import my.mynato.rahmatridham.mynato.Model.ChoiModel;
import my.mynato.rahmatridham.mynato.R;

/**
 * Created by rahmatridham on 7/7/2017.
 */

public class ChoiAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChoiModel> choiModelArrayList;

    public ChoiAdapter(Context context, ArrayList<ChoiModel> choiModelArrayList) {
        this.context = context;
        this.choiModelArrayList = choiModelArrayList;
    }

    @Override
    public int getCount() {
        return choiModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return choiModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_approval, parent, false);

        final ChoiModel model = choiModelArrayList.get(position);

        TextView tanggal = (TextView) v.findViewById(R.id.textViewTanggalAbsensi);
        TextView status = (TextView) v.findViewById(R.id.textViewStatusAbsensi);
        ImageView arrow = (ImageView) v.findViewById(R.id.imageViewArrowDetail);
        tanggal.setText(model.getNama_choi());
        status.setText(model.getKeterangan());
        if(!model.getKeterangan().equals("PENDING APPROVAL") || model.getKeterangan().equals("CLOSED")){
            status.setBackgroundResource(R.color.greenButton);
        }else {
            status.setBackgroundResource(R.color.warnamerah);
        }

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(v.getContext(), PernyataanBudayaPerilaku.class);
                    intent.putExtra("id_aktivasi_choi", model.getId_aktivasi_choi());
                    v.getContext().startActivity(intent);
                    ((Activity)context).finish();
                }catch (Exception e){
                    Toast.makeText(v.getContext(), "Gagal ke halaman koreksi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
