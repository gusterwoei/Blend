# Blend
Android animation chain helper, with this you can create a series of animation with just few lines of code.
Blend is implemented using Android's ```ViewPropertyAnimator```.

## Install
Add the dependency in build.gradle
```xml
dependencies {
    compile 'com.guster.android:blend:1.0.1'
}
```

## Getting Started
### 1. Simple animation chain
This will animate ```myView``` from left to right after 1 second delay, then from right to left.
Total duration is 2 second. Remember to call ```add()``` for each animation at the end to add to the animation queue.
```java
Blend.animate(myView).duration(2000)
    .translationXBy(100f).delay(1000).add()
    .translationXBy(-100f).add()
    .start();
```

### 2. Combined animation chain
You can create a series of combined animations by using ```together()``` method.
The following code will animate myView upwards by 100px while fade to half opacity, then animate downwards by 100px and
gradually go back to full opacity.
```java
Blend.animate(myView).duration(1000)
    .together(
        BlendHelper.get().translationYBy(-100f),
        BlendHelper.get().alpha(0.5f))
    .together(
        BlendHelper.get().translationYBy(100f),
        BlendHelper.get().alpha(1f))
    .start();
```
```BlendHelper``` is an optional helper class to create an ```Blend.Animation``` object.
You can however, create manually like this:
```java
Animation animation = new Blend.Animation()
    .setType(AnimType.TRANSLATION_Y_BY)
    .setValue(100f);
```
You can find the full list of ```AnimType``` at the end of the document.

### 3. Reverse Animation
Calling ```Blend.start()``` will start the animation chain from top to bottom in the way the animations
are defined.

Calling ```Blend.reverseStart()``` however, will start from bottom to top instead, with all animation values
negated (ie. translation 100f will become translation -100f)
```java
Blend.animate(myView).duration(1000)
    .together(
        BlendHelper.get().translationYBy(-100f),
        BlendHelper.get().alpha(0.5f))
    .together(
        BlendHelper.get().translationYBy(100f),
        BlendHelper.get().alpha(1f))
    .reverseStart();
```

### 4. Multiple-View animation chain
A ```Blend``` object is tied to a single view. So far we are only dealing with single-view animation chain.
Now if you want to chain multiple Blends together, you need to use ```BlendGrouper``` class.

The following code will animate the 4 Blends ```myViewBlend, imgViewBlend, textViewBlend, someViewBlend```
in the FIFO order.

```java
BlendGrouper.get()
    .animate(myViewBlend, imgViewBlend, textViewBlend, someViewBlend)
    .callback(new BlendGrouper.Callback() {
        @Override
        public void onAnimationEnd() {
            // animation end, let's do something
            Blend.animate(txtTitle).duration(500)
                .together(
                        BlendHelper.get().alpha(1),
                        BlendHelper.get().scaleX(1),
                        BlendHelper.get().scaleY(1))
                .start();
        }
    })
    .start();
    // .startTogether();
```

If you call ```BlendGrouper.startTogether()``` instead. ```BlendGrouper``` will animate all Blends in parallel.

## Blend Methods
```java
// set total duration for a Blend animation chain
duration();

// set true will repeat the animations upon finished. Default is false
repeat(true);

// check if it is being animated
isAnimating();

// set callback for a Blend
callback(Blend.Callback callback);

// group a list of Animations to be animated together
together();

// start the animations
start();

// reverse the animations
reverseStart();

// stop the animations;
stop();

// reset all animations, this will reset the view's animator properties to its default values
clear();

/** TRANSLATION **/
translationX(float x);
translationXBy(float x);
translationY(float y);
translationYBy(float y);

/** SCALE **/
scaleX(float x);
scaleXBy(float x);
scaleY(float y);
scaleYBy(float y);

/** ALPHA **/
alpha(float alpha);
alphaBy(float alpha);

/** ROTATION **/
rotationX(float rotation);
rotationXBy(float rotation);
rotationY(float rotation);
rotationYBy(float rotation);
rotation(float rotation);
rotationBy(float rotation);
```

## Blend.Animation methods
```java
// set duration for Animation, this will override the duration set via Blend.duration()
duration(long duration);

// set start delay in milliseconds
delay(long duration);

// set interpolator
setInterpolator(TimeInterpolator interpolator);

// set animation callback
setCallback();

// add this Animation to the queue
add();
```

## Full list of Blend.Animation types
### Translation
```
TRANSLATION_X,
TRANSLATION_X_BY
TRANSLATION_Y
TRANSLATION_Y_BY
TRANSLATION_Z
TRANSLATION_Z_BY
```

### Scale
```
SCALE_X
SCALE_X_BY
SCALE_Y
SCALE_Y_BY
```

### Alpha
```
ALPHA
ALPHA_BY
```

### Rotation
```
ROTATION_X
ROTATION_X_BY
ROTATION_Y
ROTATION_Y_BY
ROTATION
ROTATION_BY
```

## Developed by
* Guster Woei - <gusterwoei@gmail.com>

## License
```xml
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
```