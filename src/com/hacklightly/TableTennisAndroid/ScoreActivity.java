package com.hacklightly.TableTennisAndroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sameer on 1/25/2014.
 */
public class ScoreActivity extends Activity {

    private int p1Score, p2Score, playerValue;

    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        p1Score = Integer.parseInt(getIntent().getExtras().get("p1Score").toString());
        p2Score = Integer.parseInt(getIntent().getExtras().get("p2Score").toString());
        playerValue = Integer.parseInt(getIntent().getExtras().get("playerValue").toString());

        TextView title = (TextView)findViewById(R.id.title), result = (TextView) findViewById(R.id.result),
                score = (TextView) findViewById(R.id.score);

        Button back = (Button) findViewById(R.id.back_button);
        if (playerValue == 1) {
            result.setText("You " + (p1Score>=p2Score?"WON!":"LOST!"));
            score.setText("Score: "+ p1Score  + " - " + p2Score);
        }
        else {
            result.setText("You " + (p2Score>=p1Score?"WON!":"LOST!"));
            score.setText("Score: "+ p2Score  + " - " + p1Score);
        }

        title.setTypeface(LaunchActivity.FONT);
        result.setTypeface(LaunchActivity.FONT);
        score.setTypeface(LaunchActivity.FONT);
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