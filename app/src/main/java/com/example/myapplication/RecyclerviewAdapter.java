package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.CustomViewHolder> implements Filterable {

    Context context;
    ArrayList<MainData> unfilteredlist;
    ArrayList<MainData> filteredList;

    public RecyclerviewAdapter(Context context, ArrayList<MainData> list) {
        this.context = context;
        this.unfilteredlist = list;
        this.filteredList = list;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);

        return new RecyclerviewAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerviewAdapter.CustomViewHolder holder, int position) {
        holder.name_tv.setText(filteredList.get(position).getName_tv());
        holder.number_tv.setText(filteredList.get(position).getNumber_tv());

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

        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    filteredList = unfilteredlist;
                }else{
                    ArrayList<MainData> filteringList = new ArrayList<>();
                    for(MainData name : unfilteredlist){
                        if(name.getName_tv().toLowerCase().contains(charString.toLowerCase())){
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<MainData>)results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView number_tv;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.name_tv = (TextView)itemView.findViewById(R.id.name_tv);
            this.number_tv = (TextView)itemView.findViewById(R.id.number_tv);
        }
    }
}
