package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import static java.lang.Math.abs;

public class ClickedItemActivity extends AppCompatActivity  {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2_activity_clicked_item);

        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        textView = findViewById(R.id.tvName);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            String selectedName = intent.getStringExtra("name");
            Integer selectedImage = intent.getIntExtra("image",0);

            textView.setText(selectedName);
            imageView.setImage(ImageSource.resource(selectedImage));
        }

    }


}