package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.viewHolder> {
    private ArrayList<String> musicData = null;

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView musicName;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            musicName = itemView.findViewById(R.id.music_name);
        }
    }

    MusicListAdapter(ArrayList<String> list) {
        musicData = list;
    }

    @NonNull
    @NotNull
    @Override
    public MusicListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_music_info,parent,false);
        MusicListAdapter.viewHolder vh = new MusicListAdapter.viewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MusicListAdapter.viewHolder holder, int position) {
        String text = musicData.get(position);
        holder.musicName.setText(text);

        holder.musicName.setTag(position);
        holder.musicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return musicData.size() ;
    }


}
