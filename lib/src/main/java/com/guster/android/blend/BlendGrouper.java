package com.guster.android.blend;


import java.lang.ref.WeakReference;

/**
 * Created by Gusterwoei on 11/10/15.
 */
public class BlendGrouper {
    private Blend[] blends;
    private int currentIndex = 0;
    private WeakReference<Callback> callback;
    private boolean startTogether;

    public static BlendGrouper get() {
        return new BlendGrouper();
    }

    public BlendGrouper callback(Callback callback) {
        this.callback = new WeakReference<Callback>(callback);
        return this;
    }

    public BlendGrouper animate(final Blend... arr) {
        this.blends = arr;

        for(Blend blend : blends) {
            // set a private callback here to notify the animation end
            blend.setPrivateCallback(new Blend.PrivateCallback() {
                @Override
                public void onAnimationEnd() {
                    if(currentIndex < arr.length) {
                        if(!startTogether) {
                            blends[currentIndex++].start();
                        } else {
                            currentIndex++;
                        }
                    } else {
                        // reset values
                        startTogether = false;

                        // the animation chain has completely ended
                        if(callback != null)
                            callback.get().onAnimationEnd();
                    }
                }
            });
        }

        return this;
    }

    public void start() {
        if(blends.length > 0) {
            blends[currentIndex++].start();
        }
    }

    public void startTogether() {
        startTogether = true;
        for(int i=0; i<blends.length; i++) {
            blends[i].start();

            if(i == 0)
                currentIndex++;
        }
    }


    /** Callback function **/

    public interface Callback {
        void onAnimationEnd();
    }
}
