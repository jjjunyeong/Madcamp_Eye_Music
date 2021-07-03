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
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Fragment3 extends Fragment {
    public final static int RECORD_PERM_CODE = 103;
    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 8000; //frequency
    private int mChannelCount = AudioFormat.CHANNEL_IN_MONO; //channelConfiguration
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT; //audioEncoding
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat); //buffersize

    //RecordAudio recordTask;

    private FFT transformer;
    int blockSize = 256;
    Button recordButton; //startStopButton
    boolean started = false;

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
        View view = inflater.inflate(R.layout.fragment3_layout,container,false);

        recordButton = (Button) view.findViewById(R.id.record_btn);
        recordButton.setOnClickListener(new RecordButtonClickListener());

        transformer = new FFT(blockSize);

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
            if(started){
                started = false;
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
            started = true;
            recordButton.setText("ON_RECORD");
            recordTask();
            //recordTask.execute();
        }
    }

    private void recordTask(){
        AudioRecord audioRecord = new AudioRecord( MediaRecorder.AudioSource.MIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
        short[] buffer = new short[blockSize]; //blockSize = 256
        double[] toTransform = new double[blockSize]; //blockSize = 256

        audioRecord.startRecording();

        while (started) {
            int bufferReadResult = audioRecord.read(buffer, 0, blockSize); //blockSize = 256
            Log.i("bufferReadResult", Integer.toString(bufferReadResult));
            // AudioRecord 객체에서 데이터를 읽은 다음에는 short 타입의 변수들을 double 타입으로
            // 바꾸는 루프를 처리한다.
            // 직접 타입 변환(casting)으로 이 작업을 처리할 수 없다. 값들이 전체 범위가 아니라 -1.0에서
            // 1.0 사이라서 그렇다
            // short를 32,767(Short.MAX_VALUE) 으로 나누면 double로 타입이 바뀌는데,
            // 이 값이 short의 최대값이기 때문이다.
            for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; // 부호 있는 16비트
                Log.i("buffer", Double.toString(buffer[i]));
                Log.i("Short.MAX_VALUE", Short.toString(Short.MAX_VALUE));
                Log.i("toTransform", Double.toString(toTransform[i]));
            }

            // 이제 double값들의 배열을 FFT 객체로 넘겨준다. FFT 객체는 이 배열을 재사용하여 출력 값을
            // 담는다. 포함된 데이터는 시간 도메인이 아니라
            // 주파수 도메인에 존재한다. 이 말은 배열의 첫 번째 요소가 시간상으로 첫 번째 샘플이 아니라는 얘기다.
            // 배열의 첫 번째 요소는 첫 번째 주파수 집합의 레벨을 나타낸다.

            // 256가지 값(범위)을 사용하고 있고 샘플 비율이 8,000 이므로 배열의 각 요소가 대략
            // 15.625Hz를 담당하게 된다. 15.625라는 숫자는 샘플 비율을 반으로 나누고(캡쳐할 수 있는
            // 최대 주파수는 샘플 비율의 반이다. <- 누가 그랬는데...), 다시 256으로 나누어 나온 것이다.
            // 따라서 배열의 첫 번째 요소로 나타난 데이터는 영(0)과 15.625Hz 사이에
            // 해당하는 오디오 레벨을 의미한다.
            //transformer.fft(toTransform);
        }
        audioRecord.stop();


    }

}