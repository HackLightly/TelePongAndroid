package com.hacklightly.TableTennisAndroid;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.Window;
import com.koushikdutta.async.http.socketio.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sameer on 1/24/2014.
 */
public class SessionActivity extends Activity implements SensorEventListener, ConnectCallback, EventCallback, DisconnectCallback, ErrorCallback {
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    int n, nLast;

    public static SocketIOClient S_CLIENT;
    public static String MY_ID;



    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        n = 0;
        nLast = 0;

        MY_ID = getIntent().getStringExtra("id");
        Log.d ("myapp", MY_ID);

        SocketIOClient.connect("http://telepong.herokuapp.com", new ConnectCallback() {

            @Override
            public void onConnectCompleted(Exception ex, SocketIOClient client) {
                S_CLIENT = client;

                if (ex != null) {
                    return;
                }

                JSONObject connect = new JSONObject();

                try {
                    connect.put("data", MY_ID);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(connect);

                S_CLIENT.emit("joinConnectionMobile", jsonArray);
                Log.d("myapp", "emitted: " + jsonArray.toString());

                client.setDisconnectCallback(SessionActivity.this);
                client.setErrorCallback(SessionActivity.this);
                //client.setJSONCallback(SessionActivity.this);
                //client.setStringCallback(SessionActivity.this);

                //You need to explicitly specify which events you are interested in receiving
                client.addListener("statusChange", SessionActivity.this);
                client.addListener("gameData", SessionActivity.this);


            }
        }, new Handler());


    }

    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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
                    Log.d("test", "HIT: " + n);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(100);
                    nLast = n;
                    Log.d ("test", "x: " + x + " y: " + y + " z: " + z);
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

        //Log.d("MainActivity", "Event:" + event + "ArgumentsHI:"
        //        + argument.toString(2));

        if (event.equals("statusChange")) {

        }
        else if (event.equals ("gameData")) {

        }
     }

    @Override
    public void onConnectCompleted(Exception ex, SocketIOClient client) {

    }

    @Override
    public void onDisconnect(Exception e) {
        Log.d("myapp", "SOCKET DISCONNECTED");
    }

    @Override
    public void onError(String error) {
        Log.d("myapp", error);
    }
}