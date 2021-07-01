package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.CustomViewHolder> {

    private ArrayList<MainData> arrayList;

    public RecyclerviewAdapter(Context context, ArrayList<MainData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerviewAdapter.CustomViewHolder holder, int position) {
        holder.name_tv.setText(arrayList.get(position).getName_tv());
        holder.number_tv.setText(arrayList.get(position).getNumber_tv());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer bf = new StringBuffer();
                bf.append(holder.name_tv.getText().toString());
                bf.append(holder.number_tv.getText().toString());

                Toast.makeText(v.getContext(),bf,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return (null!=arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView name_tv;
        protected TextView number_tv;
        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.name_tv = (TextView)itemView.findViewById(R.id.name_tv);
            this.number_tv = (TextView)itemView.findViewById(R.id.number_tv);
        }
    }
}
