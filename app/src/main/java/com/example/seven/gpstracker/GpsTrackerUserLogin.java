package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class GpsTrackerUserLogin extends AppCompatActivity {

    final Context context = this;
    Button btnusrlogin;
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_user_login);

        final pafap_db db = new pafap_db(this);

        SharedPreferences userDetails = context.getSharedPreferences("userdetails", MODE_PRIVATE);
        String Uname = userDetails.getString("username", "");
        String Upass = userDetails.getString("password", "");
        final EditText mail = (EditText) findViewById(R.id.txtusr);
        final EditText pass = (EditText) findViewById(R.id.txtusrpass);
        mail.setText(Uname);
        pass.setText(Upass);
        btnusrlogin = (Button) findViewById(R.id.btnLogin);
        btnusrlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pafap_strings str = new pafap_strings();
                if(!mail.getText().toString().equals("") && !pass.getText().toString().equals("")){
                    try{
                        if(db.usrExists(mail.getText().toString(), str.md5(pass.getText().toString()))){
                            SharedPreferences userDetails = context.getSharedPreferences("userdetails", MODE_PRIVATE);
                            Editor edit = userDetails.edit();
                            edit.clear();
                            edit.putString("username", mail.getText().toString().trim());
                            edit.putString("password", pass.getText().toString().trim());
                            edit.commit();
                            startActivity(new Intent(GpsTrackerUserLogin.this, GpsTrackerViewList.class).putExtra("uid", db.getUID()));
                            db.close();
                            finish();
                        }
                        else mbview("Register!", "You Must Register!");
                    }
                    catch (Exception ex){
                        mbview("db error!", ex.getMessage());
                    }
                }
                else mbview("Error!", "Enter User And Password!");
            }
        });
    }

    public void mbview(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                startActivity(new Intent(GpsTrackerUserLogin.this, GpsTrackerUserRegister.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void savePrefrences(String key, String value)
    {
        SharedPreferences prefs = context.getSharedPreferences(context.getApplicationContext().getPackageName(), 0);
        prefs.edit().putString(key, value).commit();
    }

    public String getPrefrences(String key)
    {
        SharedPreferences prefs = context.getSharedPreferences(context.getApplicationContext().getPackageName(), 0);
        return prefs.getString(key, "");
    }
}
