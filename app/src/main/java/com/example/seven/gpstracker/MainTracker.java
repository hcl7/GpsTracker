package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainTracker extends AppCompatActivity {

    //Button btnusrlogin;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tracker);

        //handler

        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                Intent i = new Intent(MainTracker.this, GpsTrackerUserLogin.class);
                startActivity(i);
                //close this activity
                finish();
            }
        }, 3*1000); // wait for 3 seconds


        //handler

		/*btnusrlogin = (Button) findViewById(R.id.btnUsrLogin);
		btnusrlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AndroidGPSTrackingActivity.this, gpsTrackerUserLogin.class));
				finish();
			}
		});*/
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_up:
                startActivity(new Intent(MainTracker.this, GpsTrackerUserLogin.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
