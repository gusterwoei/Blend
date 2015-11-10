package com.guster.android.example;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.guster.android.blend.Blend;
import com.guster.android.blend.BlendHelper;
import com.guster.android.blend.BlendGrouper;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.btn_moving)
    protected Button movingButton;
    @Bind(R.id.btn_reset)
    protected Button btnReset;
    @Bind(R.id.btn_start_animation)
    protected FloatingActionButton btnStart;
    @Bind(R.id.lyt_canvas)
    protected View lytCanvas;
    @Bind(R.id.img_girl1)
    protected ImageView imgGirl1;
    @Bind(R.id.img_girl2)
    protected ImageView imgGirl2;
    @Bind(R.id.img_boy)
    protected ImageView imgBoy;

    // properties
    private int animationMode = 0;
    private Blend blendButton, blendGirl1, blendGirl2, blendBoy;
    private BlendGrouper blendGrouper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start_animation)
    protected void startAnimation2() {
        if(blendBoy != null && blendBoy.isAnimating()) return;

        final int BUTTON_DURATION = 300;
        final int IMG_DURATION = 300;
        int IMG_VAL = new Random().nextInt(130);
        IMG_VAL = IMG_VAL < 50? 50 : IMG_VAL;

        blendButton = Blend.animate(movingButton).duration(BUTTON_DURATION)
                .translationYBy(100f).add()
                .translationYBy(-100f).add();
        blendGirl1 = Blend.animate(imgGirl1).duration(IMG_DURATION)
                .together(
                        BlendHelper.get().translationYBy(-IMG_VAL),
                        BlendHelper.get().alpha(0.5f))
                .together(
                        BlendHelper.get().translationYBy(IMG_VAL),
                        BlendHelper.get().alpha(1f));
        blendGirl2 = Blend.animate(imgGirl2).duration(IMG_DURATION)
                .together(
                        BlendHelper.get().alpha(0.5f),
                        BlendHelper.get().translationYBy(-IMG_VAL).delay(100))
                .together(
                        BlendHelper.get().translationYBy(IMG_VAL),
                        BlendHelper.get().alpha(1f));
        blendBoy = Blend.animate(imgBoy).duration(IMG_DURATION)
                .together(
                        BlendHelper.get().alpha(0.5f),
                        BlendHelper.get().translationYBy(-IMG_VAL).delay(200))
                .together(
                        BlendHelper.get().translationYBy(IMG_VAL),
                        BlendHelper.get().alpha(1f));

        blendGrouper = BlendGrouper.get()
                .animate(blendButton, blendGirl1, blendGirl2, blendBoy).callback(new BlendGrouper.Callback() {
                    @Override
                    public void onAnimationEnd() {
                        logd("START AGAIN");
                    }
                });
        blendGrouper.startTogether();
    }

    //@OnClick(R.id.btn_start_animation)
    protected void startAnimation() {
        final int CANVAS_WIDTH = lytCanvas.getWidth();
        final int CANVAS_HEIGHT = lytCanvas.getHeight();

        blendButton = Blend.animate(movingButton).duration(500)
                .translationYBy(CANVAS_HEIGHT - movingButton.getHeight()).add()
                .translationXBy(CANVAS_WIDTH - movingButton.getWidth()).add()
                .translationYBy(movingButton.getHeight() - CANVAS_HEIGHT).add()
                .translationXBy(movingButton.getWidth() - CANVAS_WIDTH).setCallback(new Blend.Callback() {
                    @Override
                    public void onAnimationEnd() {
                        logd("Callback Now");
                    }
                }).add()
                .together(
                        BlendHelper.get().translationXBy((CANVAS_WIDTH - movingButton.getWidth()) / 2),
                        BlendHelper.get().translationYBy((CANVAS_HEIGHT - movingButton.getHeight()) / 2))
                .together(
                        BlendHelper.get().scaleXBy(-0.5f),
                        BlendHelper.get().scaleYBy(-0.5f))
                .together(
                        BlendHelper.get().scaleXBy(0.5f),
                        BlendHelper.get().scaleYBy(0.5f));

        Blend blend2 = Blend.animate(movingButton)
                .duration(500).repeat(true)
                .rotationBy(360f).add()
                .translationYBy(-50f).duration(300).add()
                .translationYBy(50f).duration(300).add();

        BlendGrouper.get()
                .animate(blendButton, blend2)
                .callback(new BlendGrouper.Callback() {
                    @Override
                    public void onAnimationEnd() {
                        logd("Happy Birthday Friend");
                    }
                })
                .start();
    }

    @OnClick(R.id.btn_reset)
    protected void resetAnimation() {
        blendButton.clear();
        blendGirl1.clear();
        blendGirl2.clear();
        blendBoy.clear();
        animationMode = 0;
        //blendButton = null;
    }

    private void logd(String s) {
        Log.d("BLEND", s);
    }
}
