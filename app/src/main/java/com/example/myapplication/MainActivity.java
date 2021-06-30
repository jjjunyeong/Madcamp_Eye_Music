package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readExcel();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void readExcel(){
        Log.v("Main","시작");
        try{
            InputStream is = getBaseContext().getResources().getAssets().open("NAVER_Contact.xls", Context.MODE_PRIVATE);

            Workbook wb = Workbook.getWorkbook(is);
            Log.v("Main","시작");
            if(wb !=null){
                Sheet sheet = wb.getSheet(0);
                if(sheet !=null){
                    int colTotal = 2;
                    int rowIndexStart=1;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    Log.v("Main","for문 시작");
                    for(int row = rowIndexStart; row<rowTotal;row++){
                        sb = new StringBuilder();

                        for(int col=0; col<colTotal;col++){
                            String contents = sheet.getCell(col,row).getContents();

                            Log.v("Main",col + "번째: "+contents);
                        }
                    }
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}