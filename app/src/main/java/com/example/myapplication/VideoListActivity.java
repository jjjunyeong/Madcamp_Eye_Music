package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity {

    private static int PICK_VIDEO_REQUEST = 1;
    VideoView videoView;
    MediaController media_Controller;
    Button button;
    MediaPlayer mediaPlayer;

    String type = "video/*";

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
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, PICK_VIDEO_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == PICK_VIDEO_REQUEST ) {
            Uri mVideoURI  = data.getData();
            String name = getVideoNametoUri(data.getData());

            String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + name;
       //     Toast.makeText(this, filepath, Toast.LENGTH_SHORT).show();
            TextView textView = findViewById(R.id.videoText);
            textView.setText(filepath);

            videoView.setMediaController(media_Controller);
            videoView.setVideoURI(mVideoURI);
            videoView.requestFocus();


            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //mediaPlayer.start();
                    videoView.start();
                }
            });
        }

    }

    public String getVideoNametoUri(Uri data){
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        String name = path.substring(path.lastIndexOf("/")+1);
        return name;
    }

}

