package com.shouther.photoeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.google.android.gms.ads.MobileAds;
import com.shouther.photoeditor.splashview.ParticleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {

    private Handler handler;
    private Runnable nextScreenRunnable;
    private long DELAY_NAVIGATION = 1400; // 1.4 sec

//    @BindView(R.id.imageViewImage)
//    public ImageView imageViewImage;

    @BindView(R.id.app_name)
    ParticleView mParticleView;

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext=this;
        ButterKnife.bind(this);
        MobileAds.initialize(this, mContext.getString(R.string.admob_app_id));

        handler = new Handler();
        //imageViewImage.animate().scaleXBy(0.3f).scaleYBy(0.3f).setDuration(1000).setInterpolator(new BounceInterpolator()).start();
        mParticleView.startAnim();
        mParticleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                HomeActivity.open(mContext);
                finish();
            }
        });
       // startTimerForGoToNextScreen();
        }

    private void startTimerForGoToNextScreen() {
        nextScreenRunnable = new Runnable() {
            @Override
            public void run() {
                HomeActivity.open(mContext);
                finish();
            }
        };

        handler.postDelayed(nextScreenRunnable, DELAY_NAVIGATION);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(nextScreenRunnable);
    }
}
