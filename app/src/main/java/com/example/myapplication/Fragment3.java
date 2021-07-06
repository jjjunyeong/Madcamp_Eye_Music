package com.example.myapplication;

import android.content.Intent;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Fragment3 extends Fragment {
    MediaPlayer player;
    int musicPosition;
    int music;
    boolean musicLoad;
    public final static int RECORD_PERM_CODE = 103;

    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 8000; //frequency
    private int mChannelCount = AudioFormat.CHANNEL_IN_STEREO; //channelConfiguration
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT; //audioEncoding
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat); //buffersize

    //RecordAudio recordTask;
    AudioRecord audioRecord = null;
    MediaRecorder recorder;

    public Thread mRecordThread = null;

    private RealDoubleFFT transformer;

//    private BitmapToVideoEncoder bitmapToVideoEncoder;

    int blockSize = 256;
    ImageButton recordButton; //startStopButton
    Button musiclistButton;
    ImageButton playButton;
    ImageButton pauseButton;
    boolean recording = false;
    boolean playing = false;

    // Bitmap 이미지를 표시하기 위해 ImageView를 사용한다. 이 이미지는 현재 오디오 스트림에서 주파수들의 레벨을 나타낸다.
    // 이 레벨들을 그리려면 Bitmap에서 구성한 Canvas 객체와 Paint객체가 필요하다.
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    File file;
    FileOutputStream fos;
    Bitmap copy;

    File music_file;

    ImageButton videosButton;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout, container, false);

        recordButton = (ImageButton) view.findViewById(R.id.record_btn);
        recordButton.setOnClickListener(new RecordButtonClickListener());

        musiclistButton = (Button) view.findViewById(R.id.btn_title);
        musiclistButton.setOnClickListener(new MusicListButtonClickListener());

        playButton = (ImageButton) view.findViewById(R.id.btn_play);
        playButton.setOnClickListener(new PlayButtonClickListener());

        videosButton = (ImageButton) view.findViewById(R.id.videos_btn);
        videosButton.setOnClickListener(new VideosButtonClickListener());

        transformer = new RealDoubleFFT(blockSize);

        // ImageView 및 관련 객체 설정 부분
        imageView = (ImageView) view.findViewById(R.id.colorImage);
        bitmap = Bitmap.createBitmap(1070, 1600, Bitmap.Config.ARGB_8888); //1024, 800
        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==0){
            musiclistButton.setText("Alcohol Free");
            musicLoad=true;
            music=0;
        }else if(resultCode==1){
            musiclistButton.setText("A Song Written Easily");
            musicLoad=true;
            music=1;
        }else if(resultCode==2) {
            musiclistButton.setText("BANANA");
            musicLoad = true;
            music = 2;
        }else if(resultCode==3){
            musiclistButton.setText("Beautiful Beautiful");
            musicLoad=true;
            music=3;
        }else if(resultCode==4){
            musiclistButton.setText("butter_BTS");
            musicLoad=true;
            music=4;
        }else if(resultCode==5){
            musiclistButton.setText("Dun Dun Dance");
            musicLoad=true;
            music=5;
        }else if(resultCode==6){
            musiclistButton.setText("Love Sick Girls");
            musicLoad=true;
            music=6;
        }else if(resultCode==7){
            musiclistButton.setText("Rollin_BraveGirls");
            musicLoad=true;
            music=7;
        }else if(resultCode==8){
            musiclistButton.setText("Lazenca Save Us");
            musicLoad=true;
            music=8;
        }
        else if(resultCode==9){
            musiclistButton.setText("Piano Left to Right");
            musicLoad=true;
            music=9;
        }
        if(player!=null){
            player.release();
            player=null;
        }
    }

    private void playAudio() {
        if(musicLoad){
            if (player != null && !player.isPlaying()) {
                player.seekTo(musicPosition);
                player.start();

                Toast.makeText(getContext(), "재시작됨.", Toast.LENGTH_SHORT).show();
            }
            else{
                closePlayer();
                switch (music){
                    case 0: player = MediaPlayer.create(getContext(),R.raw.alcoholfree_twice);
                        break;
                    case 1: player = MediaPlayer.create(getContext(),R.raw.asongwritteneasily_oneus);
                        break;
                    case 2: player = MediaPlayer.create(getContext(),R.raw.banana_minions);
                        break;
                    case 3: player = MediaPlayer.create(getContext(),R.raw.beautifulbeautiful_onandoff);
                        break;
                    case 4: player = MediaPlayer.create(getContext(),R.raw.bts_butter);
                        break;
                    case 5: player = MediaPlayer.create(getContext(),R.raw.dundundance_ohmygirl);
                        break;
                    case 6: player = MediaPlayer.create(getContext(),R.raw.lovesickgirls_blackpink);
                        break;
                    case 7: player = MediaPlayer.create(getContext(),R.raw.rollin_bravegirls);
                        break;
                    case 8: player = MediaPlayer.create(getContext(),R.raw.lazencasaveus_hahyunwoo);
                        break;
                    case 9: player = MediaPlayer.create(getContext(),R.raw.piano_x2);
                        break;
                }
                player.start();
                Toast.makeText(getContext(), "재생 시작됨.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    private void pauseAudio() {
        if (player != null) {
            musicPosition = player.getCurrentPosition();
            player.pause();

            Toast.makeText(getContext(), "일시정지됨.", Toast.LENGTH_SHORT).show();
        }
    }


    private class MusicListButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MusicListActivity.class);
            startActivityForResult(intent,111);
        }
    }
    private class PlayButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(playing){
                playing = false;
                pauseAudio();
                playButton.setImageResource(R.drawable.ic_playing);
                Toast.makeText(getActivity(), "finish recording", Toast.LENGTH_SHORT).show();
            } else {
                playing = true;
                playAudio();
                playButton.setImageResource(R.drawable.ic_pause);
                //askRecordButtonPermissions();
            }
        }
    }

    private class VideosButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), VideoListActivity.class);
            startActivity(intent);
        }
    }

    private class RecordButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            Toast.makeText(getActivity(), "record button clicked", Toast.LENGTH_SHORT).show();
            if (recording) {
                recording = false;
                recordButton.setImageResource(R.drawable.ic_not_recording);
                //Toast.makeText(getActivity(), "finish recording", Toast.LENGTH_SHORT).show();
            } else {
                askRecordButtonPermissions();
            }
        }
    }
    boolean isRecording = false;
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(music_file);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
            isRecording = true; }
        catch (IOException e) {
            Log.e("Main", "prepare() failed");
            e.printStackTrace(); }
    }
    private void stopRecording() {
        if(isRecording){ recorder.stop(); }
        recorder.reset();
        recorder.release();
        isRecording = false; }
    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {



                    Toast.makeText(getContext(), "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수

            } else {
                //Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void askRecordButtonPermissions() {
        checkPermission();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for record permission on runtime
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_PERM_CODE);
        } else {
            recordButton.setImageResource(R.drawable.ic_recording);
            recording = true;
            //Toast.makeText(getActivity(), "start recording", Toast.LENGTH_SHORT).show();

            if (audioRecord == null) {
                mRecordThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AudioRecord audioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
                        short[] buffer = new short[blockSize]; //blockSize = 256
                        double[] toTransform = new double[blockSize]; //blockSize = 256

//                        recorder = new MediaRecorder();
//                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                        startRecording();
                        audioRecord.startRecording();

                        BitmapToVideoEncoder bitmapToVideoEncoder = new BitmapToVideoEncoder(new BitmapToVideoEncoder.IBitmapToVideoEncoderCallback() {
                            @Override
                            public void onEncodingComplete(File outputFile) {
                                //Toast.makeText(getActivity(), "Encoding Complete", Toast.LENGTH_SHORT).show();
                            }
                        });

                        try{
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String getTime = sdf.format(date);

                            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + getTime + ".mp4");
                            music_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/" + getTime + ".mp3");

//                            recorder.setOutputFile(music_file);
//                            recorder.prepare();
//                            recorder.start();

                            bitmapToVideoEncoder.startEncoding(1070, 1600, file);


//                            fos = new FileOutputStream(file);
//                            if(fos!=null) {
//                                bitmapToVideoEncoder.startEncoding(1070, 1600, file);
//                            }
//                            fos.close();
                            }catch(Exception e){
                                Log.e("testSaveView", "Exception: " + e.toString());
                            }

                        int count = 0;

                        //자연스러운 변화 만들기
                        int[][] subPaintPosition;
                        int[] tempPaintposition;
                        subPaintPosition = new int[4][blockSize*3];
                        tempPaintposition = new int[blockSize*3];
                        while (recording) {
                            int bufferReadResult = audioRecord.read(buffer, 0, blockSize); //blockSize = 256

                            for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                                toTransform[i] = (double) buffer[i] / Short.MAX_VALUE;
                            }
                            transformer.ft(toTransform);
                            canvas.drawColor(Color.BLACK);
                            paint = new Paint();

                            for (int i = 0; i < blockSize; i++) {
                                if(toTransform[i]*20<40){
                                    tempPaintposition[i*3]=0;
                                    tempPaintposition[i*3+1]=0;
                                    tempPaintposition[i*3+2]=0;
                                    continue;
                                }
                                int r = (int) toTransform[i]*20;

                                Random ran = new Random();
                                int xmin = 0;
                                int xmax = 1070;
                                int ymin = 0;
                                int ymax = 1600;
                                int xran = ran.nextInt(xmax-xmin+1) + xmin;
                                int yran = ran.nextInt(ymax-ymin+1) + ymin;

//                                if(i<=8) paint.setColor(navy);
//                                else if(i<=15) paint.setColor(blue);
//                                else if(i<=30) paint.setColor(green);
//                                else if(i<=60) paint.setColor(yellow);
//                                else if(i<=100) paint.setColor(orange);
//                                else paint.setColor(red);
//                                canvas.drawCircle(xran, yran, r, paint);

                                tempPaintposition[i*3]=r;
                                tempPaintposition[i*3+1]=xran;
                                tempPaintposition[i*3+2]=yran;
                            }
                            subPaintPosition[0]=subPaintPosition[1];
                            subPaintPosition[1]=subPaintPosition[2];
                            subPaintPosition[2]=subPaintPosition[3];
                            subPaintPosition[3]=tempPaintposition;//subPaintPosition[4];
//                            subPaintPosition[4]=subPaintPosition[5];
//                            subPaintPosition[5]=subPaintPosition[6];
//                            subPaintPosition[6]=subPaintPosition[7];
//                            subPaintPosition[7]=subPaintPosition[8];
//                            subPaintPosition[8]=tempPaintposition;
                            for(int i=0; i < 4; i++){
                                int x=0;
                                int y=0;
                                int r=0;
                                for(int j=0;j<blockSize;j++){
                                    r=subPaintPosition[i][j*3];
                                    x=subPaintPosition[i][j*3+1];
                                    y=subPaintPosition[i][j*3+2];
                                    painting(x,y,r,j,i,canvas);
                                }
                            }

                            imageView.buildDrawingCache();
                            Bitmap bit = imageView.getDrawingCache();
                            if(bit != null){
                                copy = bit.copy(bit.getConfig(), true);
                            }
                            bitmapToVideoEncoder.queueFrame(copy);
                            imageView.invalidate();
//                            //이전 내역들을 queue 형식으로 저장
//                            subPaintPosition[0]=subPaintPosition[1];
//                            subPaintPosition[1]=subPaintPosition[2];
//                            subPaintPosition[2]=subPaintPosition[3];
//                            subPaintPosition[3]=subPaintPosition[4];
//                            subPaintPosition[4]=subPaintPosition[5];
//                            subPaintPosition[5]=subPaintPosition[6];
//                            subPaintPosition[6]=subPaintPosition[7];
//                            subPaintPosition[7]=subPaintPosition[8];
//                            subPaintPosition[8]=tempPaintposition;




                            //saving as png files
//                            try{
//                                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android studio" + count + ".png");
//                                fos = new FileOutputStream(file);
//                                if(fos!=null) {
//                                    bit.compress(Bitmap.CompressFormat.PNG, 50, fos);
//                                }
//                                fos.close();
//                            }catch(Exception e){
//                                Log.e("testSaveView", "Exception: " + e.toString());
//                            }
//                            count++;

                        }
                        bitmapToVideoEncoder.stopEncoding();
                        audioRecord.stop();
                        stopRecording();

//                        if(recorder != null){
//                            recorder.stop();
//                            recorder.reset();
//                            recorder.release();
//                            recorder = null;
//                        }


                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(file);
                        mediaScanIntent.setData(contentUri);
                        getActivity().sendBroadcast(mediaScanIntent);

                    }
                });
            }
            mRecordThread.start();
        }
    }
    private void painting(int x, int y, int r, int hz, int num, Canvas canvas){
        if(r!=0){
            int red = getResources().getColor(R.color.red);
            int orange = getResources().getColor(R.color.orange);
            int yellow = getResources().getColor(R.color.yellow);
            int green = getResources().getColor(R.color.green);
            int blue = getResources().getColor(R.color.blue);
            int navy = getResources().getColor(R.color.navy);
            if(hz<=8) paint.setColor(navy);
            else if(hz<=15) paint.setColor(blue);
            else if(hz<=30) paint.setColor(green);
            else if(hz<=60) paint.setColor(yellow);
            else if(hz<=100) paint.setColor(orange);
            else paint.setColor(red);
            //paint.setAlpha((8-num*2)*10);
            canvas.drawCircle(x, y, r, paint);
        }
    }
}

