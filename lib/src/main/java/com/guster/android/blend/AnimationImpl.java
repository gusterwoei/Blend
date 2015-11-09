package com.guster.android.blend;

/**
 * Created by Gusterwoei on 11/9/15.
 *
 */
interface AnimationImpl {

    /** TRANSLATION **/
    Blend.Animation translationX(float x);

    Blend.Animation translationXBy(float x);

    Blend.Animation translationY(float y);

    Blend.Animation translationYBy(float y);

    /** SCALE **/
    Blend.Animation scaleX(float x);

    Blend.Animation scaleXBy(float x);

    Blend.Animation scaleY(float y);

    Blend.Animation scaleYBy(float y);

    /** ALPHA **/
    Blend.Animation alpha(float alpha);

    Blend.Animation alphaBy(float alpha);

    /** ROTATION **/
    Blend.Animation rotationX(float rotation);

    Blend.Animation rotationXBy(float rotation);

    Blend.Animation rotationY(float rotation);

    Blend.Animation rotationYBy(float rotation);

    Blend.Animation rotation(float rotation);

    Blend.Animation rotationBy(float rotation);
}
