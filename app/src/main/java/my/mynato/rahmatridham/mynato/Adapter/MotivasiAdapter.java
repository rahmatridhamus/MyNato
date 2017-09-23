package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.CeritaMotivasi;
import my.mynato.rahmatridham.mynato.R;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 2/22/2017.
 */

public class MotivasiAdapter extends RecyclerView.Adapter<MotivasiAdapter.MyViewHolder> {
    private ArrayList<CeritaMotivasi> ceritaMotivasiArrayList;
    OnItemClickListenerMotivasi clickListenerMotivasi;

    public MotivasiAdapter(ArrayList<CeritaMotivasi> ceritaMotivasiArrayList, OnItemClickListenerMotivasi clickListenerMotivasi1) {
        this.ceritaMotivasiArrayList = ceritaMotivasiArrayList;
        this.clickListenerMotivasi = clickListenerMotivasi1;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_motivasi, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CeritaMotivasi motivasi = ceritaMotivasiArrayList.get(position);
        holder.judul.setText(motivasi.getTitle());
        holder.buka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(motivasi.getFile().replaceAll("\\s+","%20")); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });
        holder.bind(ceritaMotivasiArrayList.get(position),clickListenerMotivasi);
    }

    @Override
    public int getItemCount() {
        return ceritaMotivasiArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul;
        public Button buka;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.textDescCard);
            buka = (Button) view.findViewById(R.id.bukaPdfButCard);
        }

        public void bind(final CeritaMotivasi item, final OnItemClickListenerMotivasi listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
