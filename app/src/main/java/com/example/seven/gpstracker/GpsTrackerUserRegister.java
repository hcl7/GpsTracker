package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GpsTrackerUserRegister extends AppCompatActivity {

    Button btnreg;
    final Context context = this;

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_user_register);

        final pafap_db db = new pafap_db(this);
        btnreg = (Button) findViewById(R.id.btnregister);
        btnreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    EditText usrmail = (EditText) findViewById(R.id.txtemail);
                    if(!isEmail(usrmail.getText().toString())) usrmail.setText("");
                    EditText usrpass = (EditText) findViewById(R.id.txtuserpass);
                    EditText usrpassconf = (EditText) findViewById(R.id.txtuserpassconfirm);
                    pafap_strings str = new pafap_strings();
                    if(!usrmail.getText().toString().equals("") && !usrpass.getText().toString().equals("") && !usrpassconf.getText().toString().equals("")){
                        if(usrpass.getText().toString().equals(usrpassconf.getText().toString())){
                            ArrayList<usrTracker> usreg = new ArrayList<usrTracker>();
                            usreg.add(new usrTracker(usrmail.getText().toString(), str.md5(usrpass.getText().toString())));
                            db.addUsrContents(usreg);
                            mbview("Register!", "User Registered!");
                            startActivity(new Intent(GpsTrackerUserRegister.this, GpsTrackerUserLogin.class));
                            GpsTrackerUserRegister.this.finish();
                        }
                        else mbview("Error Confirm", "Confirm password not the same!");
                    }
                    else mbview("Form", "Fill form!");
                }catch (Exception ex){
                    mbview("register", ex.getMessage());
                }
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

    public boolean isEmail(String email){
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        boolean matchFound = matcher.matches();
        return matchFound;
    }
}
