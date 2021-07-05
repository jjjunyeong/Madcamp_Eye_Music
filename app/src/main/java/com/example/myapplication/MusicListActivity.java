package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        Context mContext = getApplicationContext();

        ArrayList<String> list = new ArrayList<>();

        list.add("Alcohol Free_Twice") ;
        list.add("A Song Written Easily_OneUs") ;
        list.add("Beautiful Beautiful_OnAndOff") ;
        list.add("Butter_BTS") ;
        list.add("Dun Dun Dance_OhMyGirl") ;
        list.add("Love Sick Girls_BlackPink") ;
        list.add("Rollin_BraveGirls") ;


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.musiclist_list) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        MusicListAdapter adapter = new MusicListAdapter(list,mContext);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListner(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                setResult(position);
                finish();
            }
        });
    }


}