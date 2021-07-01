package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ClickedItemActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class Fragment2 extends Fragment {
    GridView gridView;
    String[] names = {"image1","image2","image3","image4","image5","image6","image7","image8","image9","image10","image11","image12","image13","image14","image15","image16","image17","image18","image19","image20"};
    //int[] dates = {201102, 201102, 201102, 210102, 210104, 210104, 210223, 210224, 210224, 210228, 210303, 210416, 210418, 210418, 210420, 210510, 210512, 210520, 210522, 210601};
    int[] images = {R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6,
            R.drawable.image7,R.drawable.image8,R.drawable.image9,R.drawable.image10,R.drawable.image11,R.drawable.image12,R.drawable.image13,
            R.drawable.image14,R.drawable.image15,R.drawable.image16,R.drawable.image17,R.drawable.image18,R.drawable.image19,R.drawable.image20};

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2_layout, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        CustomAdapter customAdapter = new CustomAdapter(names,images,this);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedName = names[i];
                int selectedImage = images[i];
                //Toast.makeText(getActivity(), names[i], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ClickedItemActivity.class);
                intent.putExtra("name",selectedName);
                intent.putExtra("image",selectedImage);
                //Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                //((StartActivity_Interface)parentActivity).startMyIntent(intent);
            }
        });

        return rootView;
    }

    public class CustomAdapter extends BaseAdapter{
        private String[] imageNames; //String[]>int[]
        private int[] imagesPhoto;
        private Fragment2 context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(String[] imageNames, int[] imagesPhoto, Fragment2 context) {
            this.imageNames = imageNames;
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagesPhoto.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null){
                view = layoutInflater.inflate(R.layout.fragment2_row_items, viewGroup, false);
            }

            //TextView tvName = view.findViewById(R.id.tvName);
            ImageView imageView = view.findViewById(R.id.imageView);

            //tvName.setText(imageNames[i]);
            imageView.setImageResource(imagesPhoto[i]);

            return view;
        }
    }

}
