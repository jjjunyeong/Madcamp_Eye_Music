package com.example.myapplication;

import android.app.Activity;
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
    private ArrayList<String> artistData = null;
    private Context mContext;

    MusicListAdapter(ArrayList<String> music,ArrayList<String> artist,Context context) {
        musicData = music;
        artistData = artist;
        mContext = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    private OnItemClickListener mListner = null;

    public void setOnItemClickListner(OnItemClickListener listner){
        this.mListner = listner;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView musicName;
        TextView artistName;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            musicName = itemView.findViewById(R.id.music_name);
            artistName = itemView.findViewById(R.id.artist_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListner !=null){
                            mListner.onItemClick(v,pos);
                        }
                    }
                }
            });
        }
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
        String artext = artistData.get(position);
        holder.musicName.setText(text);
        holder.artistName.setText(artext);

    }

    @Override
    public int getItemCount() {
        return musicData.size();
    }


}
