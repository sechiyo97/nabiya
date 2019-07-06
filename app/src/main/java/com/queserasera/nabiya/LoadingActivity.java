package com.queserasera.nabiya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class LoadingActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView loadingNavi = (ImageView) findViewById(R.id.loading_nabi);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(loadingNavi);
        Glide.with(this).load(R.raw.loading_nabi_white).into(gifImage);

        startLoading();
    }

    private void startLoading(){
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(getBaseContext(), AnswerActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }, 2800);
    }

    @Override
    public void finish(){
        super.finish();
        handler.removeCallbacksAndMessages(null);
        overridePendingTransition(0,0);
    }
}
