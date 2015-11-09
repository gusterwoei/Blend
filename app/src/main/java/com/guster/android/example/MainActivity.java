package com.guster.android.example;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;

import com.guster.android.blend.Blend;

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

    // properties
    private int animationMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    //@OnClick(R.id.btn_start_animation)
    protected void startAnimation() {
        final int CANVAS_WIDTH = lytCanvas.getWidth();
        final int CANVAS_HEIGHT = lytCanvas.getHeight();

        logd("W,H: " + CANVAS_WIDTH + ", " + CANVAS_HEIGHT);

        ViewPropertyAnimator animator = movingButton.animate()
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        logd("Animation Start");
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        logd("Animation End");

                        switch (animationMode) {
                            case 0:
                                movingButton.animate().translationXBy(CANVAS_WIDTH - movingButton.getWidth());
                                break;
                            case 1:
                                movingButton.animate().translationYBy(movingButton.getHeight() - CANVAS_HEIGHT);
                                break;
                            case 2:
                                movingButton.animate().translationXBy(movingButton.getWidth() - CANVAS_WIDTH);
                                break;
                            case 3:
                                movingButton.animate()
                                        .translationXBy( (CANVAS_WIDTH - movingButton.getWidth())/2)
                                        .translationYBy( (CANVAS_HEIGHT - movingButton.getHeight())/2);
                                break;
                            case 4:
                                movingButton.animate()
                                        .scaleXBy(-0.5f)
                                        .scaleYBy(-0.5f)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                logd("Animation Action End");
                                                movingButton.animate().scaleXBy(0.5f).scaleYBy(0.5f);
                                            }
                                        })
                                        .start();
                                break;
                            case 5:
                                movingButton.animate().cancel();
                                break;
                        }

                        animationMode++;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

        animator.translationYBy(CANVAS_HEIGHT - movingButton.getHeight());
        animator.start();
    }

    @OnClick(R.id.btn_start_animation)
    protected void startAnimation2() {
        final int CANVAS_WIDTH = lytCanvas.getWidth();
        final int CANVAS_HEIGHT = lytCanvas.getHeight();

        Blend.prepareFor(movingButton).setDuration(1000)
                .translationYBy(CANVAS_HEIGHT - movingButton.getHeight()).add()
                .translationXBy(CANVAS_WIDTH - movingButton.getWidth()).add()
                .translationYBy(movingButton.getHeight() - CANVAS_HEIGHT).add()
                .translationXBy(movingButton.getWidth() - CANVAS_WIDTH).add()
                .translationXBy((CANVAS_WIDTH - movingButton.getWidth()) / 2).add()
                .translationYBy((CANVAS_HEIGHT - movingButton.getHeight()) / 2).add()
                .scaleXBy(-0.5f).add().scaleYBy(-0.5f).add()
                .scaleXBy(0.5f).add().scaleYBy(0.5f).add();

    }

    @OnClick(R.id.btn_reset)
    protected void resetAnimation() {
        movingButton.animate().cancel();
        movingButton.setTranslationX(0);
        movingButton.setTranslationY(0);
        movingButton.animate().setListener(null);
        animationMode = 0;

        /*movingButton.animate().translationX(0).translationY(0).setDuration(0)
        .withEndAction(new Runnable() {
            @Override
            public void run() {
                animationMode = 0;
            }
        });*/
    }

    private void logd(String s) {
        Log.d("BLEND", s);
    }
}
