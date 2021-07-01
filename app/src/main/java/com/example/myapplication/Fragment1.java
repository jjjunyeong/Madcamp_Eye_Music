package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.log4j.chainsaw.Main;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {

    private ArrayList<MainData> arrayList = new ArrayList<>();



    public static List nameinFrag = new ArrayList();
    public static List numberinFrag = new ArrayList();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_layout,container,false);

        initDataset();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(context,arrayList);
        recyclerView.setAdapter(recyclerviewAdapter);

        return view;
    }

    private void initDataset(){
        arrayList.clear();
        for(int i=0;i<nameinFrag.size();i++){
            arrayList.add(new MainData(nameinFrag.get(i).toString(),numberinFrag.get(i).toString()));
        }
    }

}
