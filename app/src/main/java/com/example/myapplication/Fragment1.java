package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.log4j.chainsaw.Main;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.MainActivity.arrayIndex;
import static com.example.myapplication.MainActivity.mDbOpenHelper;
import static com.example.myapplication.MainActivity.setTextLength;

public class Fragment1 extends Fragment implements TextWatcher {

    public static ArrayList<MainData> arrayList = new ArrayList<>();
    RecyclerviewAdapter recyclerviewAdapter;
    static RecyclerView recyclerView;

    public static List nameinFrag = new ArrayList();
    public static List numberinFrag = new ArrayList();

    FloatingActionButton add_btn;
    FloatingActionButton add_number_btn;
    Animation fromBottom, toBottom, rotateOpen, rotateClose;
    Boolean clicked = false;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_layout,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
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

        fromBottom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_close_anim);

        add_btn = (FloatingActionButton) view.findViewById(R.id.add_btn_1);
        add_number_btn = (FloatingActionButton) view.findViewById(R.id.add_number_btn);

        add_btn.setOnClickListener(new Fragment1.AddBtnClickListener());
        add_number_btn.setOnClickListener(new Fragment1.AddNumberBtnClickListener());

        return view;
    }


    private void initDataset(){
        arrayList.clear();
        for(int i=0;i<nameinFrag.size();i++){
            arrayList.add(new MainData(nameinFrag.get(i).toString(),numberinFrag.get(i).toString()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==1){
            if(resultCode==-1){
                String newName = data.getStringExtra("name");
                String newNumber = data.getStringExtra("number");

                if(newName.length() !=0 && newNumber.length() !=0){
                    mDbOpenHelper.open();
                    mDbOpenHelper.insertColumn(newName,newNumber);
                    arrayList.add(new MainData(newName,newNumber));
                    showDatabase("name");
                    Context context = getContext();
                    recyclerviewAdapter = new RecyclerviewAdapter(context,arrayList);
                    recyclerView.setAdapter(recyclerviewAdapter);
//                    Context context = getContext();
//                    Context context = (LayoutInflater)getLayoutInflater().inflate(R.layout.fragment1_layout,,false);//container가 뭘까...?
//                    recyclerviewAdapter = new RecyclerviewAdapter(context,arrayList);
//                    recyclerView.setAdapter(recyclerviewAdapter);
                }
            }
        }
    }

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        arrayIndex.clear();
        arrayList.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            String tempNumber = iCursor.getString(iCursor.getColumnIndex("number"));
            tempNumber = setTextLength(tempNumber,10);
            arrayIndex.add(tempIndex);
            arrayList.add(new MainData(tempName,tempNumber));
        }
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

    class AddBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            onAddButtonClicked(view);
            Toast.makeText(getActivity(), "add clicked", Toast.LENGTH_SHORT).show();
        }
    }

    class AddNumberBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(), PopupActivity.class);
            startActivityForResult(intent, 1);
            Toast.makeText(getActivity(), "add number clicked", Toast.LENGTH_SHORT).show();
        }
    }

    void onAddButtonClicked(View view){
        setVisibility(clicked, view);
        setAnimation(clicked);
        clicked = !clicked;
    }

    void setVisibility(Boolean clicked, View view){
        if(!clicked){
            add_number_btn.setVisibility(view.VISIBLE);
        }
        else{
            add_number_btn.setVisibility(view.INVISIBLE);
        }
    }

    void setAnimation(Boolean clicked) {
        if(!clicked){
            add_number_btn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        }
        else{
            add_number_btn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }
}
