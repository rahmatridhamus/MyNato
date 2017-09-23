package my.mynato.rahmatridham.mynato.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Model.Games;

import java.util.ArrayList;

/**
 * Created by rahmatridham on 2/22/2017.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {
    private ArrayList<Games> gamesArrayList;
    OnClickListenerGames onClickListenerGames;


    public GamesAdapter(ArrayList<Games> gamesArrayList, OnClickListenerGames onClickListenerGames) {
        this.gamesArrayList = gamesArrayList;
        this.onClickListenerGames = onClickListenerGames;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(my.mynato.rahmatridham.mynato.R.layout.list_row_motivasi, parent, false);
        return new GamesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Games games = gamesArrayList.get(position);
        holder.judul.setText(games.getTitle());
        holder.buka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(games.getFile().replaceAll("\\s+","%20")); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });
        holder.bind(gamesArrayList.get(position),onClickListenerGames);
    }

    @Override
    public int getItemCount() {
        return gamesArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul;
        public Button buka;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(my.mynato.rahmatridham.mynato.R.id.textDescCard);
            buka = (Button) view.findViewById(my.mynato.rahmatridham.mynato.R.id.bukaPdfButCard);
        }
        public void bind(final Games item, final OnClickListenerGames listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }
}