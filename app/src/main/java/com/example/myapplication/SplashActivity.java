package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    ImageView mLoadingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        TextView l= (TextView) findViewById(R.id.tv_jj);
        l.clearAnimation();
        l.startAnimation(anim);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    int waited= 0;
                    while (waited<3000){
                        sleep(100);
                        waited +=100;
                    }

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();

                }catch (InterruptedException e){

                }finally {
                    SplashActivity.this.finish();
                }
            }
        };
        thread.start();

    }
}