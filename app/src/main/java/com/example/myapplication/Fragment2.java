package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ClickedItemActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Fragment2 extends Fragment {
    GridView gridView;
    FloatingActionButton add_btn;
    FloatingActionButton camera_btn;
    FloatingActionButton gallery_btn;
    Animation fromBottom, toBottom, rotateOpen, rotateClose;
    Boolean clicked = false;
    Boolean heart_visible = false;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    public ArrayList<Integer> img_numbers;
    public ArrayList<String> img_names;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){

        String[] names = {"JPEG_20201102_image1.jpg","JPEG_20201102_image2.jpg","JPEG_20201102_image3.jpg","JPEG_20210102_image4.jpg","JPEG_20210104_image5.jpg",
                "JPEG_20210104_image6.jpg","JPEG_20210223_image7.jpg","JPEG_20210224_image8.jpg","JPEG_20210224_image9.jpg","JPEG_20210228_image10.jpg",
                "JPEG_20210303_image11.jpg","JPEG_20210416_image12.jpg","JPEG_20210418_image13.jpg","JPEG_20210418_image14.jpg","JPEG_20210420_image15.jpg",
                "JPEG_20210510_image16.jpg","JPEG_20210512_image17.jpg","JPEG_20210520_image18.jpg","JPEG_20210522_image19.jpg","JPEG_20210601_image20.jpg"};

        img_names = new ArrayList<String>();
        img_names.addAll(Arrays.asList(names));

        Integer[] images = {R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6,
                R.drawable.image7,R.drawable.image8,R.drawable.image9,R.drawable.image10,R.drawable.image11,R.drawable.image12,R.drawable.image13,
                R.drawable.image14,R.drawable.image15,R.drawable.image16,R.drawable.image17,R.drawable.image18,R.drawable.image19,R.drawable.image20};
        img_numbers = new ArrayList<Integer>();
        img_numbers.addAll(Arrays.asList(images));


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2_layout, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        CustomAdapter customAdapter = new CustomAdapter(img_names,img_numbers,this);
        gridView.setAdapter(customAdapter);


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                ImageView heart = (ImageView)view.findViewById(R.id.heart);

                if(heart.isShown()){
                    heart.setVisibility(View.INVISIBLE);
                }else{
                    heart.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedName = img_names.get(i);
                Integer selectedImage = img_numbers.get(i);
                //Toast.makeText(getActivity(), names[i], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ClickedItemActivity.class);
                intent.putExtra("name",selectedName);
                intent.putExtra("image",selectedImage);
                //Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                //((StartActivity_Interface)parentActivity).startMyIntent(intent);
            }
        });

        fromBottom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_close_anim);

        add_btn = (FloatingActionButton) rootView.findViewById(R.id.add_btn);
        camera_btn = (FloatingActionButton) rootView.findViewById(R.id.camera_btn);
        gallery_btn = (FloatingActionButton) rootView.findViewById(R.id.gallery_btn);

        add_btn.setOnClickListener(new AddBtnClickListener());
        camera_btn.setOnClickListener(new CameraBtnClickListener());
        gallery_btn.setOnClickListener(new GalleryBtnClickListener());

        return rootView;
    }


    private class AddBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            onAddButtonClicked(view);
//            Toast.makeText(getActivity(), "add clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private void onAddButtonClicked(View view){
        setVisibility(clicked, view);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked, View view){
        if(!clicked){
            camera_btn.setVisibility(view.VISIBLE);
            gallery_btn.setVisibility(view.VISIBLE);
        }
        else{
            camera_btn.setVisibility(view.INVISIBLE);
            gallery_btn.setVisibility(view.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if(!clicked){
            camera_btn.startAnimation(fromBottom);
            gallery_btn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        }
        else{
            camera_btn.startAnimation(toBottom);
            gallery_btn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }

    private class CameraBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
//            Toast.makeText(getActivity(), "camera clicked", Toast.LENGTH_SHORT).show();
            askCameraPermissions();
        }
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //ask for permission on runtime
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }else{
                Toast.makeText(getActivity(), "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
                //왜 안떠..
            }
        }
    }

    private void openCamera() {
//        Toast.makeText(getActivity(), "Camera Open Request", Toast.LENGTH_SHORT).show();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE){
            Toast.makeText(getActivity(), "Will implement something..", Toast.LENGTH_SHORT).show();
            //찍은 사진 가져오기
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            Drawable drawable = new BitmapDrawable(image);
//
//            Resources r = getResources();
//            Drawable d = new BitmapDrawable(r, image);
//            img_numbers.add(drawable);
//            img_names.add("example");
        }
    }

    private class GalleryBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), "gallery clicked", Toast.LENGTH_SHORT).show();
        }
    }



    public class CustomAdapter extends BaseAdapter{
        private ArrayList<String> imageNames;
        private ArrayList<Integer> imagesPhoto;
        private Fragment2 context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(ArrayList<String> imageNames, ArrayList<Integer> imagesPhoto, Fragment2 context) {
            this.imageNames = imageNames;
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagesPhoto.size();
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
            imageView.setImageResource(imagesPhoto.get(i));

            return view;
        }
    }

}
