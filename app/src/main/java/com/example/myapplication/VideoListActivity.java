package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity {

    private static int PICK_VIDEO_REQUEST = 1;
    VideoView videoView;
    MediaController media_Controller;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment3_videos_layout);
        button = findViewById(R.id.button);

        // VideoView : 동영상을 재생하는 뷰
        videoView = (VideoView) findViewById(R.id.videoView2);

        media_Controller = new MediaController(this);
        media_Controller.setAnchorView(videoView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*,video/*");
                startActivityForResult(photoPickerIntent, PICK_VIDEO_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == PICK_VIDEO_REQUEST ) {
            Uri mVideoURI  = data.getData();
            videoView.setMediaController(media_Controller);

            videoView.setVideoURI(mVideoURI);

            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });

        }
    }

}

