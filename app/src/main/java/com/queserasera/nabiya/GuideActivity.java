package com.queserasera.nabiya;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends AppCompatActivity{
    // 중복 클릭 방지 시간 설정
    private static final long MIN_CLICK_INTERVAL=600;
    private long mLastDownTime;
    private long mLastUpTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        final ImageButton askButton = (ImageButton)findViewById(R.id.push_button);
        final ImageView infoMsg = (ImageView)findViewById(R.id.guide_message);

        askButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            long currentDownTime= SystemClock.uptimeMillis();
                            long elapsedDownTime=currentDownTime-mLastDownTime;
                            mLastDownTime=currentDownTime;

                            // 중복 클릭인 경우
                            if(elapsedDownTime<=MIN_CLICK_INTERVAL){
                                return false;
                            }
                            imagePushed(askButton, true);
                            changeMessage(infoMsg, true);
                            return true;

                        case MotionEvent.ACTION_UP:
                            long currentUpTime= SystemClock.uptimeMillis();
                            long elapsedUpTime= currentUpTime-mLastUpTime;
                            mLastUpTime=currentUpTime;

                            // 중복 클릭인 경우
                            if(elapsedUpTime<=MIN_CLICK_INTERVAL){
                                return false;
                            }

                            // 너무 짧을 때
                            if(mLastUpTime - mLastDownTime < 300){ // 0.3초 이하
                                AlertDialog.Builder alert = new AlertDialog.Builder(GuideActivity.this);
                                alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                alert.setMessage("'나비나비 큥!'을 외쳐 주세요.");
                                imagePushed(askButton, false);
                                changeMessage(infoMsg, false);
                                alert.show();
                            } else {
                                imagePushed(askButton, false);
                                changeMessage(infoMsg, false);
                                showLoading(v);
                            }
                            return true;
                    }
                return false;
                }
            }
        );
    }
    public void changeMessage(ImageView infoMsg, boolean pushed){
        infoMsg.setImageResource((pushed) ? R.drawable.guide_message_2 : R.drawable.guide_message_1);
    }
    public void imagePushed(ImageButton btn, boolean pushed){
        float scale = (pushed) ? (float)0.9: (float)0.8;
        btn.setImageResource((pushed) ? R.drawable.push_button_2 : R.drawable.push_button_1);
        btn.setScaleX(scale);
        btn.setScaleY(scale);
    }

    public void showLoading(View v){
        // 로딩 열기
        Intent intent = new Intent(this, LoadingActivity.class) ;
        startActivity(intent) ;
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(0,0);
    }
}
