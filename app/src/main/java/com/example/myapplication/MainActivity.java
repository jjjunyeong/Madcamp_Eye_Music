package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    List names = new ArrayList();
    List numbers = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readExcel(names,numbers);

//        Intent intent = new Intent(MainActivity.this, Fragment1.class);
//        PhoneNumber phoneNumber = new PhoneNumber(names,numbers);
//        intent.putExtra("DATA",phoneNumber);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("DATA",phoneNumber);


//        for(int i=0; i<names.size();i++){
//            StringBuffer sb1 = new StringBuffer();
//            sb1.append(names.get(i));
//            sb1.append(numbers.get(i));
//            Toast.makeText(this,sb1,Toast.LENGTH_SHORT).show();
//        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

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

                    StringBuilder sb;
                    for(int row = rowIndexStart; row<rowTotal;row++){
                        sb = new StringBuilder();

                        for(int col=0; col<colTotal;col++){
                            String contents = sheet.getCell(col,row).getContents();
                            Log.v("Main",col + "번째: "+contents);
                            if(col==0){
                                names.add(contents);
                            }
                            if(col==1){
                                numbers.add(contents);
                            }
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