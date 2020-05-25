package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

public class gpsTrackerNewBrand extends AppCompatActivity {

    final Context context = this;
    String uid = "";

    @Override
    public void onBackPressed() {
        startActivity(new Intent(gpsTrackerNewBrand.this, gpsTrackerAdd.class).putExtra("uid", uid));
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_new_brand);

        uid = getIntent().getStringExtra("uid");
        final pafap_db db = new pafap_db(this);
        final ArrayList<TypeModel> newBrandArray = new ArrayList<TypeModel>();
        final ArrayList<ModelCommand> newModelArray = new ArrayList<ModelCommand>();
        final EditText brand = (EditText) findViewById(R.id.txtNewBrand);
        final EditText model = (EditText) findViewById(R.id.txtNewModel);
        final EditText command = (EditText) findViewById(R.id.txtAddNewCommand);
        final EditText commandname = (EditText) findViewById(R.id.txtNewCommandName);
        Button btnadd2list = (Button) findViewById(R.id.btnAdd2List);
        btnadd2list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!db.checkField("brands", "model", model.getText().toString()))
                    newBrandArray.add(new TypeModel(brand.getText().toString(), model.getText().toString()));
                newModelArray.add(new ModelCommand(model.getText().toString(), commandname.getText().toString(), command.getText().toString()));
                mbview("Add To List", "Added to Temporary");
            }
        });

        Button btnaddall = (Button) findViewById(R.id.btnAddAllListed);
        btnaddall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addContents(newBrandArray);
                db.addContentsFromArraylist(newModelArray);
                brand.setText("");
                commandname.setText("");
                command.setText("");
                newBrandArray.clear();
                newModelArray.clear();
                startActivity(new Intent(gpsTrackerNewBrand.this, gpsTrackerAdd.class).putExtra("uid", uid));
                finish();
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
