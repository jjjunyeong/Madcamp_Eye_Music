package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        TextView textView = findViewById(R.id.musiclist_name);
        textView.setText("Music List");

        Context mContext = getApplicationContext();

        ArrayList<String> musiclist = new ArrayList<>();

        musiclist.add("Alcohol Free") ;
        musiclist.add("A Song Written Easily") ;
        musiclist.add("Banana") ;
        musiclist.add("Beautiful Beautiful") ;
        musiclist.add("Butter") ;
        musiclist.add("Dun Dun Dance") ;
        musiclist.add("Love Sick Girls") ;
        musiclist.add("Rollin") ;
        musiclist.add("Lazenca Save Us") ;
        musiclist.add("Piano Left to Right") ;

        ArrayList<String> artistlist = new ArrayList<>();
        artistlist.add("Twice");
        artistlist.add("OneUs");
        artistlist.add("Minions");
        artistlist.add("OnAndOff");
        artistlist.add("BTS");
        artistlist.add("OhMyGirl");
        artistlist.add("BlackPink");
        artistlist.add("BraveGirls");
        artistlist.add("하현우");
        artistlist.add("None");


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.musiclist_list) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        MusicListAdapter adapter = new MusicListAdapter(musiclist,artistlist,mContext);
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