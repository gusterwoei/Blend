package com.guster.android.example;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guster.android.blend.Blend;
import com.guster.android.blend.BlendHelper;
import com.guster.android.blend.BlendGrouper;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.btn_moving)
    protected View movingButton;
    @Bind(R.id.btn_reset)
    protected Button btnReset;
    @Bind(R.id.btn_start_animation)
    protected Button btnStart;
    @Bind(R.id.lyt_canvas)
    protected View lytCanvas;
    @Bind(R.id.img_girl1)
    protected ImageView imgGirl1;
    @Bind(R.id.img_girl2)
    protected ImageView imgGirl2;
    @Bind(R.id.img_boy)
    protected ImageView imgBoy;
    @Bind(R.id.img_rock)
    protected ImageView imgRock;
    @Bind(R.id.txt_title)
    protected TextView txtTitle;

    // properties
    private int animationMode = 0;
    private Blend blendButton, blendGirl1, blendGirl2, blendBoy, blendTitle, blendRock;
    private BlendGrouper blendGrouper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        resetAnimation();
    }

    @OnClick(R.id.btn_reset)
    protected void resetAnimation() {

        if(blendButton != null)
            blendButton.clear();
        if(blendGirl1 != null)
            blendGirl1.clear();
        if(blendGirl2 != null)
            blendGirl2.clear();
        if(blendBoy != null)
            blendBoy.clear();
        if(blendTitle != null)
            blendTitle.clear();
        if(blendRock != null)
            blendRock.clear();

        txtTitle.setAlpha(0f);
        txtTitle.setScaleX(3f);
        txtTitle.setScaleY(3f);
        imgRock.setTranslationX(-300);
        imgRock.setAlpha(1f);

        Toast.makeText(this, "Animation Reset", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_start_animation)
    protected void startAnimation() {
        if(blendBoy != null && blendBoy.isAnimating()) return;

        final int BUTTON_DURATION = 300;
        final int IMG_DURATION = 300;
        int IMG_VAL = new Random().nextInt(130);
        IMG_VAL = IMG_VAL < 50? 50 : IMG_VAL;

        blendButton = Blend.animate(movingButton).duration(BUTTON_DURATION)
                .translationXBy(100f).add()
                .translationXBy(-100f).add();

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
                        BlendHelper.get().alpha(1f),
                        BlendHelper.get().translationYBy(IMG_VAL));

        blendBoy = Blend.animate(imgBoy).duration(IMG_DURATION)
                .together(
                        BlendHelper.get().alpha(0.5f),
                        BlendHelper.get().translationYBy(-IMG_VAL).delay(200))
                .together(
                        BlendHelper.get().alpha(1f),
                        BlendHelper.get().translationYBy(IMG_VAL));

        blendGrouper = BlendGrouper.get()
                .animate(blendButton, blendGirl1, blendGirl2, blendBoy).callback(new BlendGrouper.Callback() {
                    @Override
                    public void onAnimationEnd() {
                        // show the title
                        blendTitle = Blend.animate(txtTitle).duration(250)
                                .together(
                                        BlendHelper.get().alpha(1),
                                        BlendHelper.get().scaleX(1),
                                        BlendHelper.get().scaleY(1)
                                );
                        blendTitle.start();
                    }
                });

        blendRock = Blend.animate(imgRock).duration(1000)
                .together(
                        BlendHelper.get().rotation(360),
                        BlendHelper.get().translationX(0).setCallback(new Blend.Callback() {
                            @Override
                            public void onAnimationEnd() {
                                blendGrouper.startTogether();
                            }
                        }))
                .alpha(0).duration(500).add();
        blendRock.start();
    }

    /*protected void startAnimationOld() {
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
    }*/

    private void logd(String s) {
        Log.d("BLEND", s);
    }
}
