package com.guster.android.blend;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Gusterwoei on 11/8/15.
 *
 */
public class Blend implements AnimationImpl {
    private int nextAnimateKey = 1;
    private float tvOriScaleX, tvOriScaleY, tvOriAlpha;

    private long globalDuration = -1;
    private int count = 0;
    private View targetView;
    private boolean isReverse;
    private boolean isAnimating;
    private boolean repeat;
    private HashMap<Integer, Animation[]> map = new HashMap<>();
    private WeakReference<Callback> blendCallback;
    private WeakReference<PrivateCallback> privateBlendCallback;

    public static Blend animate(View targetView) {
        return new Blend(targetView);
    }

    private Blend(View targetView) {
        this.targetView = targetView;

        // store the original scale x,y and alpha for reverse() use
        tvOriScaleX = targetView.getScaleX();
        tvOriScaleY = targetView.getScaleY();
        tvOriAlpha = targetView.getAlpha();
    }

    private void reset() {
        nextAnimateKey = 1;
        isReverse = false;
        isAnimating = false;
    }

    protected void addToMap(Animation[] animations) {
        map.put(++count, animations);
    }

    protected long getDuration() {
        return this.globalDuration;
    }

    protected HashMap<Integer, Animation[]> getStoreMap() {
        return map;
    }

    protected View getTargetView() {
        return this.targetView;
    }

    protected int getNextAnimateKey() {
        return nextAnimateKey;
    }

    protected void runNextAnimations(Animation[] animations) {
        // first we determine which animation has the longest duration,
        // then we set the callback for that one to ensure all group animations are completed
        /*Animation longestAnim = animations[0];
        for(Animation anim : animations) {
            if(anim.duration > longestAnim.duration || (longestAnim.duration == -1 && anim.duration > globalDuration)) {
                longestAnim = anim;
            }
        }*/

        for(Animation anim : animations) {
            //anim.prepareAnimation(anim == longestAnim);
            anim.prepareAnimation(true);
            anim.run();
        }

        // increment the map key for next animation
        if(isReverse)
            nextAnimateKey--;
        else
            nextAnimateKey++;
    }

    protected void setPrivateCallback(PrivateCallback privateBlendCallback) {
        this.privateBlendCallback = new WeakReference<PrivateCallback>(privateBlendCallback);
    }

    public Blend duration(long globalDuration) {
        this.globalDuration = globalDuration;
        return this;
    }

    public Blend repeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public Blend callback(Callback callback) {
        this.blendCallback = new WeakReference<Callback>(callback);
        return this;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public Blend together(Animation ... animations) {
        for(Animation animation : animations) {
            animation.setBlend(this);
        }

        addToMap(animations);

        return this;
    }

    public void start() {
        if(isAnimating) {
            logd("Blend is currently running, please try later...");
            return;
        }

        isAnimating = true;

        // run the first record
        if(map.size() > 0) {
            Animation[] arr = map.get(nextAnimateKey);
            runNextAnimations(arr);
        }
    }

    public void reverseStart() {
        if(isAnimating) {
            logd("Blend is currently running, please try later...");
            return;
        }

        nextAnimateKey = map.size();
        isReverse = true;
        start();
    }

    public void stop() {
        reset();
        targetView.animate().cancel();
    }

    public void clear() {
        reset();
        if(targetView != null) {
            targetView.animate().cancel();
            targetView.setTranslationX(0);
            targetView.setTranslationY(0);
            targetView.setAlpha(tvOriAlpha);
            targetView.setRotation(0);
            targetView.setScaleX(tvOriScaleX);
            targetView.setScaleY(tvOriScaleY);
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
            return this;
        }

        public Animation setId(String id) {
            this.id = id;
            return this;
        }

        protected Animation setType(AnimType type) {
            this.type = type;
            return this;
        }

        protected Animation setValue(float value) {
            this.value = value;
            return this;
        }

        public Animation duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Animation delay(long startDelay) {
            this.startDelay = startDelay;
            return this;
        }

        public Animation setInterpolator(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public Animation setCallback(Callback callback) {
            this.callback = new WeakReference<Callback>(callback);
            return this;
        }

        public Blend add() {
            blend.addToMap(new Animation[]{this});
            return blend;
        }

        protected void run() {
            ViewPropertyAnimator animator = blend.getTargetView().animate();

            //logd("RUN ANIM - " + type + " " + value + " :: " + blend.getNextAnimateKey());

            // for reverse mode, negate the animation value
            float value = blend.isReverse? -this.value : this.value;
            if(blend.isReverse) {
                if(type == AnimType.SCALE_X)
                    value = blend.tvOriScaleX;
                else if(type == AnimType.SCALE_Y)
                    value = blend.tvOriScaleY;
                else if(type == AnimType.ALPHA)
                    value = blend.tvOriAlpha;
            }
            
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
        }
        
        private ViewPropertyAnimator prepareAnimation(boolean setCallback) {
            ViewPropertyAnimator animator = blend.getTargetView().animate();

            // evenly distribute the global duration among animators
            if(blend.getDuration() >= 0)
                animator.setDuration(blend.getDuration() / blend.getStoreMap().size());
            if(duration >= 0)
                animator.setDuration(duration);
            if(interpolator != null)
                animator.setInterpolator(interpolator);
            animator.setStartDelay(startDelay >= 0? startDelay : 0);

            if(setCallback) {
                animator.setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        // run the next animation
                        if (blend.getNextAnimateKey() <= blend.getStoreMap().size() && blend.getNextAnimateKey() > 0) {
                            Animation[] arr = blend.getStoreMap().get(blend.getNextAnimateKey());
                            blend.runNextAnimations(arr);
                        } else {
                            logd("ANIMATION COMPLETE!");

                            final boolean isReverse = blend.isReverse;

                            // reset flags to initial state upon complete
                            blend.reset();

                            // return to the blend-one callback
                            if(blend.blendCallback != null)
                                blend.blendCallback.get().onAnimationEnd();

                            if(blend.privateBlendCallback != null)
                                blend.privateBlendCallback.get().onAnimationEnd();

                            if(blend.repeat) {
                                // restart again
                                if(isReverse)
                                    blend.reverseStart();
                                else
                                    blend.start();
                            }
                        }

                        // return to user callback
                        if (callback != null)
                            callback.get().onAnimationEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }

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

    /** Private Use Callback **/
    protected interface PrivateCallback {
        void onAnimationEnd();
    }
}
