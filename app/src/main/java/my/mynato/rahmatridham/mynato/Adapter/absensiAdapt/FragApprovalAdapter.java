package my.mynato.rahmatridham.mynato.Adapter.absensiAdapt;

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
import my.mynato.rahmatridham.mynato.Absensi.RequestKoreksi;
import my.mynato.rahmatridham.mynato.Absensi.absensi;
import my.mynato.rahmatridham.mynato.Absensi.request;
import my.mynato.rahmatridham.mynato.R;

/**
 * Created by rahmatridham on 6/20/2017.
 */

public class FragApprovalAdapter extends BaseAdapter {
    Context context;
    ArrayList<absensi.DataAbsen> dataAbsenArrayList;

    public FragApprovalAdapter(Context context, ArrayList<absensi.DataAbsen> dataAbsenArrayList) {
        this.context = context;
        this.dataAbsenArrayList = dataAbsenArrayList;
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
        View v = inflater.inflate(R.layout.list_row_approval, parent, false);

        final absensi.DataAbsen model = dataAbsenArrayList.get(position);

        TextView tanggal = (TextView) v.findViewById(R.id.textViewTanggalAbsensi);
        TextView status = (TextView) v.findViewById(R.id.textViewStatusAbsensi);
        ImageView arrow = (ImageView) v.findViewById(R.id.imageViewArrowDetail);
        tanggal.setText(model.getTanggal());
        status.setText(model.getStatus());
        if(!model.getStatus().equals("PENDING")){
            status.setBackgroundResource(R.color.greenButton);
            arrow.setVisibility(View.INVISIBLE);
        }else {
            status.setBackgroundResource(R.color.warnamerah);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(v.getContext(), ApprovalKoreksi.class);
                        intent.putExtra("idApproval", model.getId());
                        v.getContext().startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(v.getContext(), "Gagal ke halaman koreksi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return v;
    }
}
