package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.myapplication.Fragment1.arrayList;
import static com.example.myapplication.Fragment1.nameinFrag;
import static com.example.myapplication.Fragment1.numberinFrag;
import static com.example.myapplication.Fragment1.recyclerView;
import static com.example.myapplication.MainActivity.arrayIndex;
import static com.example.myapplication.MainActivity.mDbOpenHelper;
import static com.example.myapplication.MainActivity.setTextLength;

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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Long nowIndex = Long.parseLong(arrayIndex.get(position));
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("데이터 삭제")
                        .setMessage("해당 데이터를 삭제 하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                mDbOpenHelper.deleteColumn(nowIndex);
                                arrayList.remove(position);
                                showDatabase("name");
                                Context context = v.getContext();
                                RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(context,arrayList);
                                recyclerView.setAdapter(recyclerviewAdapter);

                                //showDatabase(sort);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        });
    }

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        arrayIndex.clear();
        arrayList.clear();
        nameinFrag.clear();
        numberinFrag.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            String tempNumber = iCursor.getString(iCursor.getColumnIndex("number"));
            tempNumber = setTextLength(tempNumber,10);
            arrayIndex.add(tempIndex);
            nameinFrag.add(tempName);
            numberinFrag.add(tempNumber);
            arrayList.add(new MainData(tempName,tempNumber));
        }
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
