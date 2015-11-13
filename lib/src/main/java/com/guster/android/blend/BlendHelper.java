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

/**
 * Created by Gusterwoei on 11/9/15.
 *
 */
public class BlendHelper implements AnimationImpl {
    private static BlendHelper blendHelper;

    public static BlendHelper get() {
        if(blendHelper == null)
            blendHelper = new BlendHelper();
        return blendHelper;
    }

    /** TRANSLATION **/
    @Override
    public Blend.Animation translationX(float x) {
        return new Blend.Animation().setType(AnimType.TRANSLATION_X).setValue(x);
    }

    @Override
    public Blend.Animation translationXBy(float x) {
        return new Blend.Animation().setType(AnimType.TRANSLATION_X_BY).setValue(x);
    }

    @Override
    public Blend.Animation translationY(float y) {
        return new Blend.Animation().setType(AnimType.TRANSLATION_Y).setValue(y);
    }

    @Override
    public Blend.Animation translationYBy(float y) {
        return new Blend.Animation().setType(AnimType.TRANSLATION_Y_BY).setValue(y);
    }

    /** SCALE **/
    @Override
    public Blend.Animation scaleX(float x) {
        return new Blend.Animation().setType(AnimType.SCALE_X).setValue(x);
    }

    @Override
    public Blend.Animation scaleXBy(float x) {
        return new Blend.Animation().setType(AnimType.SCALE_X_BY).setValue(x);
    }

    @Override
    public Blend.Animation scaleY(float y) {
        return new Blend.Animation().setType(AnimType.SCALE_Y).setValue(y);
    }

    @Override
    public Blend.Animation scaleYBy(float y) {
        return new Blend.Animation().setType(AnimType.SCALE_Y_BY).setValue(y);
    }

    /** ALPHA **/
    @Override
    public Blend.Animation alpha(float alpha) {
        return new Blend.Animation().setType(AnimType.ALPHA).setValue(alpha);
    }

    @Override
    public Blend.Animation alphaBy(float alpha) {
        return new Blend.Animation().setType(AnimType.ALPHA_BY).setValue(alpha);
    }

    /** ROTATION **/
    @Override
    public Blend.Animation rotationX(float rotation) {
        return new Blend.Animation().setType(AnimType.ROTATION_X).setValue(rotation);
    }

    @Override
    public Blend.Animation rotationXBy(float rotation) {
        return new Blend.Animation().setType(AnimType.ROTATION_X_BY).setValue(rotation);
    }

    @Override
    public Blend.Animation rotationY(float rotation) {
        return new Blend.Animation().setType(AnimType.ROTATION_Y).setValue(rotation);
    }

    @Override
    public Blend.Animation rotationYBy(float rotation) {
        return new Blend.Animation().setType(AnimType.ROTATION_Y_BY).setValue(rotation);
    }

    @Override
    public Blend.Animation rotation(float rotation) {
        return new Blend.Animation().setType(AnimType.ROTATION).setValue(rotation);
    }

    @Override
    public Blend.Animation rotationBy(float rotation) {
        return new Blend.Animation().setType(AnimType.ROTATION_BY).setValue(rotation);
    }
}
