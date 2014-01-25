package com.hacklightly.TableTennisAndroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.koushikdutta.async.http.socketio.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sameer on 1/24/2014.
 */
public class SessionActivity extends Activity implements SensorEventListener, ConnectCallback,
        EventCallback,  ErrorCallback, JSONCallback, StringCallback {

 //   private ProgressDialog pd;
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private int n, nLast;
    private   MediaPlayer mPlayer;
    public static SocketIOClient S_CLIENT;
    private String myID;

    private boolean started = false;
    private boolean playerIsSet = false;

    public static int playerValue = -1;

    public static int P1_SCORE = 0, P2_SCORE = 0;

    private Vibrator v;



    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);
        myID = getIntent().getStringExtra("id");
        Log.d ("myapp", "read in session: " + myID);
/*
        pd = new ProgressDialog(this);
        pd.setTitle("Waiting...");
        pd.setMessage("Waiting for Players...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);*/
        //pd.setIndeterminate(true);

        mPlayer = MediaPlayer.create(SessionActivity.this, R.raw.swoosh);

        started = false;
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        n = 0;
        nLast = 0;


        SocketIOClient.connect("http://telepong.herokuapp.com", new ConnectCallback() {

            @Override
            public void onConnectCompleted(Exception ex, SocketIOClient client) {

                S_CLIENT = client;
                Log.d ("myapp", "callback received");
                if (ex != null) {
                    return;
                }

                JSONObject connect = new JSONObject();

                try {
                    connect.put("id", myID);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(connect);

                client.emit("joinConnectionMobile", jsonArray);


                Log.d("myapp", "emitted: " + jsonArray.toString());

               //client.setDisconnectCallback(SessionActivity.this);
                client.setErrorCallback(SessionActivity.this);
                client.setJSONCallback(SessionActivity.this);
                client.setStringCallback(SessionActivity.this);

                client.addListener("statusChange", SessionActivity.this);
                client.addListener("gameData", SessionActivity.this);
                client.addListener("onHit", SessionActivity.this);


               // pd.show();


            }
        }, new Handler());

        /*ImageView iv = (ImageView)findViewById(R.id.imageView);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });*/



    }
    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause () {
        super.onPause();

        try {
            v.cancel();
        }
        catch (NullPointerException e) {}
    }
    @Override
    public void onStop () {
        super.onStop();
        try {
            v.cancel();
        }
        catch (NullPointerException e) {}
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = FloatMath.sqrt(x * x + y * y);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            n++;
            if(mAccel > 4.5){
                if (Math.abs (n - nLast) >= 5) {
                    //Log.d("test", "HIT: " + n);
                    nLast = n;
                    //Log.d ("test", "x: " + x + " y: " + y + " z: " + z);

                    JSONObject swing = new JSONObject();

                    try {
                        swing.put("id", myID);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(swing);
                    S_CLIENT.emit("swing", jsonArray);

                    Log.d ("myapp", "emitted swing");

                }
            }
        }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    @Override
    public void onEvent(String event, JSONArray argument, Acknowledge acknowledge) {
        String s = argument.toString();
        Log.d("myapp", "event: " + event.toString() + " arg: " + s.toString());

        if (event.toString().equals("statusChange")) {


            s = s.substring(1, s.length()-1);
            int x = Integer.parseInt(s);
            Log.d("myapp", "GOT A STATUS CHANGE! val: " +  x);

            if (x == 4) {

                started = true;

                Log.d("myapp", "got a 4");

                if (!playerIsSet) {
                    playerValue =2 ;
                    playerIsSet = true;
                }
            }



            if (x == 2) {

                if (!playerIsSet) {
                    playerIsSet = true;
                    playerValue = 1;
                }

            }

            if (x == 8) {
                //player one scored
                P1_SCORE ++;
                Log.d ("myapp", "I " + (playerValue==1?"WON":"LOST") + " the point");
            }

            if ( x == 10) {
                //player one won
                Log.d ("myapp", "I " + (playerValue==1?"WON":"LOST") + " the game");


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Create an intent that will start the main activity.
                        Intent i = new Intent (SessionActivity.this, ScoreActivity.class);
                        i.putExtra("playerValue", playerValue);
                        i.putExtra("p1Score", P1_SCORE);
                        i.putExtra("p2Score", P2_SCORE);
                        SessionActivity.this.startActivity(i);
                        SessionActivity.this.finish();

                        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                        return;
                    }
                }, 300);

            }

            if (x==9) {
                //player two scored
                P2_SCORE ++;
                Log.d ("myapp", "I " + (playerValue==2?"WON":"LOST") + " the point");

            }

            if (x == 11) {
                //player two won
                Log.d ("myapp", "I " + (playerValue==2?"WON":"LOST") + " the game");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Create an intent that will start the main activity.
                        Intent i = new Intent(SessionActivity.this, ScoreActivity.class);
                        i.putExtra("playerValue", playerValue);
                        i.putExtra("p1Score", P1_SCORE);
                        i.putExtra("p2Score", P2_SCORE);
                        SessionActivity.this.startActivity(i);
                        SessionActivity.this.finish();

                        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                        return;
                    }
                }, 300);
            }
            if (x== -1) {
                Log.d ("myapp", "ERROR: " + x);
                finish();
                return;
            }



       }


        else if (event.equals ("gameData")) {

        }
        else if (event.equals ("onHit")) {

            s = s.substring(1, s.length()-1);
            int x = Integer.parseInt(s);
            Log.d("myapp", "got a hit! player: " +  x + " my player #: " + playerValue);


            if ( x == playerValue) {



                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mPlayer.start();


                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(100);
            }
        }
     }

    @Override
    public void onConnectCompleted(Exception ex, SocketIOClient client) {

    }

   /* @Override
    public void onDisconnect(Exception e) {
        Log.d("myapp", "SOCKET DISCONNECTED");
    }*/

    public void onDestroy () {
        super.onDestroy();
        try {
        S_CLIENT.disconnect();
    }
    catch (NullPointerException e) {Log.d("myapp", "SOCKET DISCONNECTED2");}

    }

    @Override
    public void onError(String error) {
        Log.d("myapp", error);
    }

    @Override
    public void onJSON(JSONObject json, Acknowledge acknowledge) {

    }

    @Override
    public void onString(String string, Acknowledge acknowledge) {

    }
}