package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.myapplication.ClickedItemActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jxl.Image;
import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DbOpenHelper mDbOpenHelper;
    static List names = new ArrayList();
    static List numbers = new ArrayList();

    String sort = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbOpenHelper = new DbOpenHelper(this);
        readExcel(names, numbers);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        mDbOpenHelper.open();
        mDbOpenHelper.create();
        showDatabase(sort);

        Fragment1.nameinFrag = names;
        Fragment1.numberinFrag = numbers;
    }

    public void readExcel(List names, List numbers){
        try{
            InputStream is = getBaseContext().getResources().getAssets().open("NAVER_Contact.xls", Context.MODE_PRIVATE);

            Workbook wb = Workbook.getWorkbook(is);
            if(wb !=null){
                Sheet sheet = wb.getSheet(0);
                if(sheet !=null){
                    int colTotal = 2;
                    int rowIndexStart=1;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    for(int row = rowIndexStart; row<rowTotal;row++){
                        String contents_name = sheet.getCell(0,row).getContents().toString();
                        String contents_number = sheet.getCell(1,row).getContents().toString();

                        Log.i("Main",contents_name);
                        mDbOpenHelper.open();
                        Log.v("Main","2");
                        mDbOpenHelper.insertColumn(contents_name,contents_number);
                        Log.v("Main","3");
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        names.clear();
        numbers.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            String tempNumber = iCursor.getString(iCursor.getColumnIndex("number"));
            tempNumber = setTextLength(tempNumber,10);

            names.add(tempName);
            numbers.add(tempNumber);
        }
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

}