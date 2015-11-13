/**
 Copyright 2015 Gusterwoei

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **/

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
