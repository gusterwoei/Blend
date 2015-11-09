package com.guster.android.blend;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gusterwoei on 11/8/15.
 *
 */
public class Blend implements AnimationImpl {
    private static int nextAnimateKey = 1;

    private long globalDuration = -1;
    private int count = 0;
    private View targetView;
    private HashMap<Integer, Animation> map = new HashMap<>();

    public static Blend prepareFor(View targetView) {
        return new Blend(targetView);
    }

    private Blend(View targetView) {
        this.targetView = targetView;
    }

    protected void addToMap(Animation animation) {
        map.put(++count, animation);
    }

    protected long getDuration() {
        return this.globalDuration;
    }

    protected HashMap<Integer, Animation> getStoreMap() {
        return map;
    }

    protected View getTargetView() {
        return this.targetView;
    }

    public Blend setDuration(long globalDuration) {
        this.globalDuration = globalDuration;
        return this;
    }

    public Blend together(@NonNull Animation ... animations) {
        for(Animation animation : animations) {
            animation.setBlend(this);
            addToMap(animation);
        }

        return this;
    }

    public void start() {
        logd("START ANIMATION");

        // run the first record
        if(map.size() > 0) {
            map.get(nextAnimateKey).run();
        }
    }

    /** TRANSLATION **/
    @Override
    public Animation translationX(float x) {
        return new Animation(this).setType(AnimType.TRANSLATION_X).setValue(x);
    }

    @Override
    public Animation translationXBy(float x) {
        return new Animation(this).setType(AnimType.TRANSLATION_X_BY).setValue(x);
    }

    @Override
    public Animation translationY(float y) {
        return new Animation(this).setType(AnimType.TRANSLATION_Y).setValue(y);
    }

    @Override
    public Animation translationYBy(float y) {
        return new Animation(this).setType(AnimType.TRANSLATION_Y_BY).setValue(y);
    }

    /** SCALE **/
    @Override
    public Animation scaleX(float x) {
        return new Animation(this).setType(AnimType.SCALE_X).setValue(x);
    }

    @Override
    public Animation scaleXBy(float x) {
        return new Animation(this).setType(AnimType.SCALE_X_BY).setValue(x);
    }

    @Override
    public Animation scaleY(float y) {
        return new Animation(this).setType(AnimType.SCALE_Y).setValue(y);
    }

    @Override
    public Animation scaleYBy(float y) {
        return new Animation(this).setType(AnimType.SCALE_Y_BY).setValue(y);
    }

    /** ALPHA **/
    @Override
    public Animation alpha(float alpha) {
        return new Animation(this).setType(AnimType.ALPHA).setValue(alpha);
    }

    @Override
    public Animation alphaBy(float alpha) {
        return new Animation(this).setType(AnimType.ALPHA_BY).setValue(alpha);
    }

    /** ROTATION **/
    @Override
    public Animation rotationX(float rotation) {
        return new Animation(this).setType(AnimType.ROTATION_X).setValue(rotation);
    }

    @Override
    public Animation rotationXBy(float rotation) {
        return new Animation(this).setType(AnimType.ROTATION_X_BY).setValue(rotation);
    }

    @Override
    public Animation rotationY(float rotation) {
        return new Animation(this).setType(AnimType.ROTATION_Y).setValue(rotation);
    }

    @Override
    public Animation rotationYBy(float rotation) {
        return new Animation(this).setType(AnimType.ROTATION_Y_BY).setValue(rotation);
    }

    @Override
    public Animation rotation(float rotation) {
        return new Animation(this).setType(AnimType.ROTATION).setValue(rotation);
    }

    @Override
    public Animation rotationBy(float rotation) {
        return new Animation(this).setType(AnimType.ROTATION_BY).setValue(rotation);
    }


    /** INNER CLASS -- Animation -- **/

    public static class Animation {
        private Blend blend;

        private String id;
        private AnimType type;
        private float value;
        private long duration = -1;
        private long startDelay = -1;
        private TimeInterpolator interpolator;
        private WeakReference<Callback> callback;

        public Animation() {}

        protected Animation(Blend blend) {
            this.blend = blend;
        }

        protected Animation setBlend(Blend blend) {
            this.blend = blend;
        }

        public Animation setId(String id) {
            this.id = id;
            return this;
        }

        public Animation setType(AnimType type) {
            this.type = type;
            return this;
        }

        public Animation setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Animation setStartDelay(long startDelay) {
            this.startDelay = startDelay;
            return this;
        }

        public Animation setValue(float value) {
            this.value = value;
            return this;
        }

        public Animation setInterpolator(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public void setCallback(WeakReference<Callback> callback) {
            this.callback = callback;
        }

        public Blend add() {
            blend.addToMap(this);
            return blend;
        }

        protected void run() {
            ViewPropertyAnimator animator = prepareAnimation();

            logd("RUN ANIM - " + type);
            
            switch (type) {
                case TRANSLATION_X:
                    animator.translationX(value);
                    break;
                case TRANSLATION_X_BY:
                    animator.translationXBy(value);
                    break;
                case TRANSLATION_Y:
                    animator.translationY(value);
                    break;
                case TRANSLATION_Y_BY:
                    animator.translationYBy(value);
                    break;
                case TRANSLATION_Z:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        animator.translationZ(value);
                    }
                    break;
                case TRANSLATION_Z_BY:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        animator.translationZBy(value);
                    }
                    break;
                
                case SCALE_X:
                    animator.scaleX(value);
                    break;
                case SCALE_X_BY:
                    animator.scaleXBy(value);
                    break;
                case SCALE_Y:
                    animator.scaleY(value);
                    break;
                case SCALE_Y_BY:
                    animator.scaleYBy(value);
                    break;
                
                case ALPHA:
                    animator.alpha(value);
                    break;
                case ALPHA_BY:
                    animator.alphaBy(value);
                    break;
                
                case ROTATION:
                    animator.rotation(value);
                    break;
                case ROTATION_BY:
                    animator.rotationBy(value);
                    break;
                case ROTATION_X:
                    animator.rotationX(value);
                    break;
                case ROTATION_X_BY:
                    animator.rotationXBy(value);
                    break;
                case ROTATION_Y:
                    animator.rotationY(value);
                    break;
                case ROTATION_Y_BY:
                    animator.rotationYBy(value);
                    break;
            }

            // increment the Animation object pointer
            nextAnimateKey++;
        }
        
        private ViewPropertyAnimator prepareAnimation() {
            ViewPropertyAnimator animator = blend.getTargetView().animate();
            if(blend.getDuration() >= 0)
                animator.setDuration(blend.getDuration());
            if(duration >= 0)
                animator.setDuration(duration);
            if(startDelay >= 0)
                animator.setStartDelay(startDelay);
            if(interpolator != null)
                animator.setInterpolator(interpolator);

            animator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    // run the next animation
                    if(nextAnimateKey <= blend.getStoreMap().size())
                        blend.getStoreMap().get(nextAnimateKey).run();

                    // return to user callback
                    if(callback != null)
                        callback.get().onAnimationEnd();
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });

            return animator;
        }
    }

    private static void logd(String s) {
        Log.d("BLEND", s);
    }


    /** Animation Callback function **/

    public interface Callback {
        void onAnimationEnd();
    }
}
