package com.example.myapplication;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {

    public static List nameinFrag = new ArrayList();
    public static List numberinFrag = new ArrayList();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        StringBuffer bf = new StringBuffer();
        bf.append(nameinFrag.get(0));
        bf.append(numberinFrag.get(0));

        Toast.makeText(this.getContext(),bf,Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment1_layout, container, false);
    }

}
