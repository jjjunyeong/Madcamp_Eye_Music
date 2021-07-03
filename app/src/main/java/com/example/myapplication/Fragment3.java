package com.example.myapplication;

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

    private FFT transformer;
    int blockSize = 256;
    Button recordButton; //startStopButton
    boolean recording = false;

    // Bitmap 이미지를 표시하기 위해 ImageView를 사용한다. 이 이미지는 현재 오디오 스트림에서 주파수들의 레벨을 나타낸다.
    // 이 레벨들을 그리려면 Bitmap에서 구성한 Canvas 객체와 Paint객체가 필요하다.
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;

    //오디오 재생
    AudioTrack audioTrack;
    Button playButton;
    boolean playing = false;
    public Thread mPlayThread = null;
    public String mFilepath;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout,container,false);

        recordButton = (Button) view.findViewById(R.id.record_btn);
        recordButton.setOnClickListener(new RecordButtonClickListener());

        transformer = new FFT(blockSize);

        //오디오 재생
        playButton = (Button) view.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new PlayButtonClickListener());


        mRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] readData = new byte[mBufferSize];
                mFilepath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/record.pcm";
                //Log.v("mFilepath: ", mFilepath);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mFilepath);
                    while(recording) {
                        int ret = audioRecord.read(readData, 0, mBufferSize);  //  AudioRecord의 read 함수를 통해 pcm data 를 읽어옴
                        //Log.d(TAG, "read bytes is " + ret);
                        try {
                            fos.write(readData, 0, mBufferSize);    //  읽어온 readData 를 파일에 write 함
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if(fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;

            }
        });


        mPlayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] writeData = new byte[mBufferSize];
                FileInputStream fis = null;
                DataInputStream dis = null;
                try {
                    fis = new FileInputStream(mFilepath);
                    dis = new DataInputStream(fis);
                    audioTrack.play();  // write 하기 전에 play 를 먼저 수행해 주어야 함

                    while(playing) {
                        try {
                            int ret = dis.read(writeData, 0, mBufferSize);
                            if (ret <= 0) {
                                (getActivity()).runOnUiThread(new Runnable() { // UI 컨트롤을 위해 //////////
                                    @Override
                                    public void run() {
                                        playing = false;
                                        playButton.setText("PLAY");
                                    }
                                });
                                break;
                            }
                            audioTrack.write(writeData, 0, ret); // AudioTrack 에 write 를 하면 스피커로 송출됨
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally{
                    try {
                        dis.close();
                        fis.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                audioTrack.stop();
                audioTrack.release();
                audioTrack = null;
            }
        });

        // ImageView 및 관련 객체 설정 부분
        imageView = (ImageView) view.findViewById(R.id.colorImage);
        bitmap = Bitmap.createBitmap(256, 100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);

        return view;
    }

    private class RecordButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
//            Toast.makeText(getActivity(), "record button clicked", Toast.LENGTH_SHORT).show();
            if(recording){
                recording = false;
                recordButton.setText("RECORD");
                Toast.makeText(getActivity(), "finish recording", Toast.LENGTH_SHORT).show();
            }else{
                askRecordButtonPermissions();
            }
        }
    }

    private void askRecordButtonPermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            //ask for permission on runtime
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.RECORD_AUDIO}, RECORD_PERM_CODE);
        }else{
            Toast.makeText(getActivity(), "start recording", Toast.LENGTH_SHORT).show();
            recording = true;
            recordButton.setText("STOP");
            if(audioRecord == null){
                audioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
                audioRecord.startRecording();
            }
            mRecordThread.start();
        }
    }


    private class PlayButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), "play button clicked", Toast.LENGTH_SHORT).show();
            if(playing){
                playing = false;
                playButton.setText("PLAY");
                Toast.makeText(getActivity(), "finish playing", Toast.LENGTH_SHORT).show();
            }else{
                playing = true;
                playButton.setText("STOP");
                mPlayThread.start();
            }
        }
    }


}