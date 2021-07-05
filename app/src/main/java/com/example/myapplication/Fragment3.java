package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Fragment3 extends Fragment {
    public final static int RECORD_PERM_CODE = 103;

    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 8000; //frequency
    private int mChannelCount = AudioFormat.CHANNEL_IN_STEREO; //channelConfiguration
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT; //audioEncoding
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat); //buffersize

    //RecordAudio recordTask;
    AudioRecord audioRecord = null;

    public Thread mRecordThread = null;

    private RealDoubleFFT transformer;
    int blockSize = 256;
    Button recordButton; //startStopButton
    boolean recording = false;

    // Bitmap 이미지를 표시하기 위해 ImageView를 사용한다. 이 이미지는 현재 오디오 스트림에서 주파수들의 레벨을 나타낸다.
    // 이 레벨들을 그리려면 Bitmap에서 구성한 Canvas 객체와 Paint객체가 필요하다.
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout, container, false);

        recordButton = (Button) view.findViewById(R.id.record_btn);
        recordButton.setOnClickListener(new RecordButtonClickListener());

        transformer = new RealDoubleFFT(blockSize);

        // ImageView 및 관련 객체 설정 부분
        imageView = (ImageView) view.findViewById(R.id.colorImage);
        bitmap = Bitmap.createBitmap(1024, 800, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
//        paint = new Paint();
//        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);

        return view;
    }

    private class RecordButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            Toast.makeText(getActivity(), "record button clicked", Toast.LENGTH_SHORT).show();
            if (recording) {
                recording = false;
                recordButton.setText("RECORD");
                Toast.makeText(getActivity(), "finish recording", Toast.LENGTH_SHORT).show();
            } else {
                askRecordButtonPermissions();
            }
        }
    }

    private void askRecordButtonPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for record permission on runtime
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_PERM_CODE);
        } else {
            Toast.makeText(getActivity(), "start recording", Toast.LENGTH_SHORT).show();
            recording = true;
            recordButton.setText("STOP");
            if (audioRecord == null) {
                mRecordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AudioRecord audioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
                        short[] buffer = new short[blockSize]; //blockSize = 256
                        double[] toTransform = new double[blockSize]; //blockSize = 256

                        audioRecord.startRecording();
                        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        int count = 0;

                        while (recording) {
//                            if(count<100){
//                                count++;
//                                continue;
//                            }
//                            count = 0;

                            int bufferReadResult = audioRecord.read(buffer, 0, blockSize); //blockSize = 256
                            Log.i("bufferReadResult", Integer.toString(bufferReadResult));

                            for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                                toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; // 부호 있는 16비트
                                Log.i("buffer", Double.toString(buffer[i]));
                                Log.i("Short.MAX_VALUE", Short.toString(Short.MAX_VALUE));
                                Log.i("toTransform", Double.toString(toTransform[i]));
                            }
                            transformer.ft(toTransform);
                            canvas.drawColor(Color.BLACK);

                            for (int i = 0; i < toTransform.length; i++) {
//                                int x = i;
//                                int downy = (int) (100 - (toTransform[i] * 10));
//                                int upy = 100;

                                int r = (int) toTransform[i]*20;
                                int temp = toTransform.length / 7;

                                Random ran = new Random();
                                int xmin = 0;
                                int xmax = 1024;
                                int ymin = 0;
                                int ymax = 800;
                                int xran = ran.nextInt(xmax-xmin+1) + xmin;
                                int yran = ran.nextInt(ymax-ymin+1) + ymin;

                                paint = new Paint();
                                if(0<=i && i<=temp) paint.setColor(Color.RED);
                                else if(temp<i && i<=temp*2) paint.setColor(Color.MAGENTA);
                                else if(temp*2<i && i<=temp*3) paint.setColor(Color.YELLOW);
                                else if(temp*3<i && i<=temp*4) paint.setColor(Color.GREEN);
                                else if(temp*4<i && i<=temp*5) paint.setColor(Color.BLUE);
                                else if(temp*5<i && i<=temp*6) paint.setColor(Color.CYAN);
                                else paint.setColor(Color.LTGRAY);
                                //if()
                                //paint.setColor(Color.GREEN);

                                canvas.drawCircle(xran, yran, r, paint);
                                //canvas.drawLine(x*4, downy*8, x*4, upy*8, paint);
                            }
                            imageView.invalidate();
                        }
                        audioRecord.stop();
                    }
                });
            }
            mRecordThread.start();
        }
    }

    public void CreateandSaveVideoFiles(ArrayList<Bitmap> MyBitmapArray){
        return;
    }

}

