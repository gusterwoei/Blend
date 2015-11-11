package com.guster.android.example;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Gusterwoei on 11/11/15.
 */
@Deprecated
public class TestActivity extends AppCompatActivity {
    //Variable to store brightness value
    private int brightness;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("BLEND", "Turning Brightness");

        //Get the content resolver
        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();

        try {
            // To handle the auto
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);

            Log.d("BLEND", "Brightness: " + brightness);
            setBrightness(brightness);

        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("BLEND", "Cannot access system brightness");
            e.printStackTrace();
        }
    }

    private void setBrightness(int brightness) {
        //Set the system brightness using the brightness variable value
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        //Get the current window attributes
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        //Set the brightness of this window
        //layoutpars.screenBrightness = brightness / (float)255;
        layoutpars.screenBrightness = 1f;
        //Apply attribute changes to this window
        window.setAttributes(layoutpars);

        Log.d("BLEND", "New Brightness: " + layoutpars.screenBrightness);
    }
}
