package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class gpsTrackerEdit extends AppCompatActivity {

    final Context context = this;
    String tid = "";
    String uid = "";
    final ArrayList<TRACKER> trackerSelected = new ArrayList<TRACKER>();

    @Override
    public void onBackPressed() {
        startActivity(new Intent(gpsTrackerEdit.this, GpsTrackerViewList.class).putExtra("uid", uid));
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_edit);

        tid = getIntent().getStringExtra("tid");
        final pafap_db db = new pafap_db(this);
        Cursor cursor = db.getTableContents("SELECT DISTINCT brand FROM brands");
        bindSpinnerFromCursor(cursor, R.id.spEditBrand);
        Spinner sp = (Spinner)findViewById(R.id.spEditBrand);
        String sel = sp.getSelectedItem().toString();
        bindSpinnerByArray(R.id.spEditModel, sel);
        cursor.close();

        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Spinner sp = (Spinner)findViewById(R.id.spEditBrand);
                String sel = sp.getSelectedItem().toString();
                Cursor cursor = db.getTableContent("brands", new String[]{"model"}, "brand", new String[]{sel});
                bindSpinnerFromCursor(cursor, R.id.spEditModel);
                cursor.close();
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        try{
            TRACKER tracker = db.getTrackerContent(Integer.parseInt(tid));
            trackerSelected.add(tracker);
            db.close();

            EditText editplate = (EditText) findViewById(R.id.txteditplate);
            EditText editphone = (EditText) findViewById(R.id.txtEditPhoneNumber);
            EditText editpass = (EditText) findViewById(R.id.txtEditPassword);

            for (int i = 0; i < trackerSelected.size(); ++i) {
                editplate.setText(trackerSelected.get(i).getName());
                editphone.setText(trackerSelected.get(i).getPhoneNumber());
                editpass.setText(trackerSelected.get(i).getPassword());
                uid = String.valueOf(trackerSelected.get(i).getUID());
            }
        }catch(Exception ex) {
            mbview("db error! ", ex.getMessage());
        }

        //update tracker
        Button btnupdatetracker = (Button) findViewById(R.id.btnTrackerUpdate);
        btnupdatetracker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editplate = (EditText) findViewById(R.id.txteditplate);
                EditText editphone = (EditText) findViewById(R.id.txtEditPhoneNumber);
                EditText editpass = (EditText) findViewById(R.id.txtEditPassword);
                Spinner sptrbrand = (Spinner) findViewById(R.id.spEditBrand);
                Spinner sptrmodel = (Spinner) findViewById(R.id.spEditModel);
                String brandSelected = sptrbrand.getSelectedItem().toString();
                String modelSelected = sptrmodel.getSelectedItem().toString();
                Locale defloc = Locale.getDefault();
                if (!editplate.getText().toString().equals("") && !editphone.getText().toString().equals("") && !editpass.getText().toString().equals("")){
                    db.updateTrackerContent(new TRACKER(Integer.parseInt(tid), editplate.getText().toString().toUpperCase(defloc), brandSelected.toString(), modelSelected.toString(), editphone.getText().toString(), editpass.getText().toString(), "CHECK"));
                    db.deleteTableContent("trcommands", "tid", Integer.parseInt(tid));
                    ArrayList<ModelCommand> mctracker = new ArrayList<ModelCommand>();
                    mctracker = db.getCommandsByModel(sptrmodel.getSelectedItem().toString());
                    db.addTrackerCommandsFromArraylist(mctracker, Integer.parseInt(tid));
                    mbview("Update Tracker! ", "Tracker Updated!");
                    db.close();
                    startActivity(new Intent(gpsTrackerEdit.this, GpsTrackerViewList.class).putExtra("uid", uid));
                    gpsTrackerEdit.this.finish();
                }
            }
        });
    }

    public void bindSpinnerFromCursor(Cursor cs, int spnr){
        List<String> tmp = new ArrayList<String>();
        if(cs.moveToFirst()){
            do{
                tmp.add(cs.getString(0).toString());
            }while(cs.moveToNext());
        }
        Spinner sp = (Spinner) findViewById(spnr);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tmp);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
    }

    public void bindSpinnerByArray(int spnr, String key)
    {
        ArrayList<TRACKER> arraylist = new ArrayList<TRACKER>();
        arraylist = fillArray();
        ArrayList<String> tmp = new ArrayList<String>();
        for (int i = 0;i<arraylist.size();i++){
            if(arraylist.get(i).getBrand().toString().equals(key)){
                tmp.add(arraylist.get(i).getModel().toString());
            }
        }
        Spinner sp = (Spinner) findViewById(spnr);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tmp);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
    }

    public ArrayList<TRACKER> fillArray()
    {
        ArrayList<TRACKER> arraylist = new ArrayList<TRACKER>();
        arraylist.add(new TRACKER("Xexun", "TK102"));
        arraylist.add(new TRACKER("Xexun", "TK103"));
        arraylist.add(new TRACKER("Coban", "GPS103"));
        arraylist.add(new TRACKER("Coban", "GPS107"));
        arraylist.add(new TRACKER("OnerGPS", "MT01"));
        arraylist.add(new TRACKER("OnerGPS", "CT02"));
        return arraylist;
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
