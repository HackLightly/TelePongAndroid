package com.hacklightly.TableTennisAndroid;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LaunchActivity extends Activity implements View.OnClickListener{

    public static Typeface FONT;

// In onCreate method



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView title = (TextView)findViewById(R.id.title), finePrint = (TextView)findViewById(R.id.finePrint),
        info = (TextView)findViewById(R.id.info);
        Button scan = (Button)findViewById(R.id.qr_button),
                about = (Button)findViewById(R.id.about_button);

        FONT = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Light.ttf");

        title.setTypeface(FONT);
        about.setTypeface(FONT);
        scan.setTypeface(FONT);
        finePrint.setTypeface(FONT);
        info.setTypeface(FONT);

        scan.setOnClickListener(this);
        about.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.qr_button) {



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent i = new Intent ("la.droid.qr.scan");
                    LaunchActivity.this.startActivityForResult(i, 0);
                    //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                    overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                }
            }, 300);
        }
        else {
            //about


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //Create an intent that will start the main activity.
                    Intent i = new Intent(LaunchActivity.this, AboutActivity.class);
                    LaunchActivity.this.startActivity(i);
                    //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                    overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                }
            }, 300);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            String result = data.getExtras().getString("la.droid.qr.result");
            Log.d("myapp", "pass to session: " + result);

            Intent i = new Intent (LaunchActivity.this, SessionActivity.class);
            i.putExtra("id", result);
            LaunchActivity.this.startActivity(i);

        }
        catch (NullPointerException e) {
            Log.d ("myapp", "QR Droid farted");
        }

    }


}
