package com.hacklightly.TableTennisAndroid;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class LaunchActivity extends Activity implements View.OnClickListener{

// In onCreate method



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        Intent qrDroid = new Intent("la.droid.qr.scan");
        startActivityForResult(qrDroid, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("la.droid.qr.result");
        Log.d("myapp", result);
    }


}
