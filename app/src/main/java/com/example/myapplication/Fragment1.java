package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class Fragment1 extends Fragment implements View.OnClickListener, TextWatcher {

    private ArrayList<MainData> arrayList = new ArrayList<>();
    RecyclerviewAdapter recyclerviewAdapter;

    public static List nameinFrag = new ArrayList();
    public static List numberinFrag = new ArrayList();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_layout,container,false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        EditText editText = (EditText)view.findViewById(R.id.search_et);
        editText.addTextChangedListener(this);


        initDataset();

        Context context = view.getContext();

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerviewAdapter = new RecyclerviewAdapter(context,arrayList);
        recyclerView.setAdapter(recyclerviewAdapter);

        Button button = (Button)view.findViewById(R.id.search_btn);
        button.setOnClickListener(this);

        return view;
    }


    private void initDataset(){
        arrayList.clear();
        for(int i=0;i<nameinFrag.size();i++){
            arrayList.add(new MainData(nameinFrag.get(i).toString(),numberinFrag.get(i).toString()));
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"이제 좀 됐으면...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        recyclerviewAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
