package com.hacklightly.TableTennisAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sameer on 1/24/2014.
 */
public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView title = (TextView)findViewById(R.id.title), tv1 = (TextView) findViewById(R.id.textView),
                tv2 = (TextView) findViewById(R.id.textView2), tv3 = (TextView) findViewById(R.id.textView3),
                tv4 = (TextView) findViewById(R.id.textView4);

        Button back = (Button)findViewById(R.id.back_button);

        title.setTypeface(LaunchActivity.FONT);
        tv1.setTypeface(LaunchActivity.FONT);
        tv2.setTypeface(LaunchActivity.FONT);
        tv3.setTypeface(LaunchActivity.FONT);
        tv4.setTypeface(LaunchActivity.FONT);
        back.setTypeface(LaunchActivity.FONT);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        finish();
                        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                    }
                }, 300);
            }
        });


    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            }
        }, 300);
}
}