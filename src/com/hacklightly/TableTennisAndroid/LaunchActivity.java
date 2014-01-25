package com.hacklightly.TableTennisAndroid;


import android.content.Intent;
import android.graphics.Typeface;
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

        TextView title = (TextView)findViewById(R.id.title), finePrint = (TextView)findViewById(R.id.finePrint);
        Button scan = (Button)findViewById(R.id.qr_button),
                about = (Button)findViewById(R.id.about_button);

        FONT = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Light.ttf");

        title.setTypeface(FONT);
        about.setTypeface(FONT);
        scan.setTypeface(FONT);
        finePrint.setTypeface(FONT);

        scan.setOnClickListener(this);
        about.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.qr_button) {
        Intent qrDroid = new Intent("la.droid.qr.scan");
        startActivityForResult(qrDroid, 0);
        }
        else {
            //about
            Intent i = new Intent(LaunchActivity.this, AboutActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
        String result = data.getExtras().getString("la.droid.qr.result");

        Log.d("myapp", result);

        Intent i = new Intent (LaunchActivity.this, SessionActivity.class);
         i.putExtra("id", result);
        startActivity(i);
        }
        catch (NullPointerException e) {
            Log.d ("myapp", "QR Droid farted");
        }

    }


}
