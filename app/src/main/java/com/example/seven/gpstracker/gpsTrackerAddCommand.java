package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class gpsTrackerAddCommand extends AppCompatActivity {

    final Context context = this;
    Button btnaddcommand;
    String tid = "";
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_add_command);

        tid = getIntent().getStringExtra("tid");
        final pafap_db dbc = new pafap_db(this);
        final String model = dbc.getTableField("trcommands", "model", "tid", Integer.parseInt(tid));
        btnaddcommand = (Button) findViewById(R.id.btnAddCommand);
        btnaddcommand.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText txtcommand = (EditText) findViewById(R.id.txtCommand);
                EditText txtcn = (EditText) findViewById(R.id.txtCommandName);
                if(!txtcn.getText().toString().equals("") && !txtcommand.getText().toString().equals("")){
                    ArrayList<ModelCommand> newc = new ArrayList<ModelCommand>();
                    newc.add(new ModelCommand(model, txtcn.getText().toString(), txtcommand.getText().toString()));
                    dbc.addTrackerCommandsFromArraylist(newc, Integer.parseInt(tid));
                    mbview("Add Command!", "Command Added!");
                    startActivity(new Intent(gpsTrackerAddCommand.this, gpsTrackerSettings.class).putExtra("tid", tid));
                    finish();
                }
                else mbview("Add Error!", "Fill Form!");
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
}
