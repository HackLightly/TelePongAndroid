package com.hacklightly.TableTennisAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;

/**
 * Created by sameer on 1/25/2014.
 */
public class SplashActivity extends Activity {
    private final int time = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
        setContentView(R.layout.splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent that will start the Menu-Activity.
                Intent mainIntent = new Intent(SplashActivity.this, LaunchActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, time);*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Create an intent that will start the main activity.
                Intent mainIntent = new Intent(SplashActivity.this, LaunchActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                //Finish splash activity so user cant go back to it.
                SplashActivity.this.finish();

                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            }
        }, time);

    }
}