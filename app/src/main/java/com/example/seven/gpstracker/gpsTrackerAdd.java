package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.net.Uri;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class gpsTrackerAdd extends AppCompatActivity {

    Button btnAdd2List;
    Button btnVList;
    String uid = "";
    final Context context = this;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(gpsTrackerAdd.this, GpsTrackerViewList.class).putExtra("uid", uid));
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_add);

        uid = getIntent().getStringExtra("uid");
        final pafap_db db = new pafap_db(this);
        if(!db.Exists()){
            try{
                ArrayList<TypeModel> brandarraylist = new ArrayList<TypeModel>();
                brandarraylist.add(new TypeModel("Xexun", "TK102"));
                brandarraylist.add(new TypeModel("Xexun", "TK103"));
                brandarraylist.add(new TypeModel("Coban", "GPS103"));
                brandarraylist.add(new TypeModel("Coban", "GPS107"));
                brandarraylist.add(new TypeModel("OnerGPS", "MT01"));
                brandarraylist.add(new TypeModel("OnerGPS", "CT02"));
                brandarraylist.add(new TypeModel("OnerGPS", "OCT800"));
                db.addContents(brandarraylist);
            }
            catch(Exception ex){
                mbview("Error Model Insert db!", ex.getMessage());
            }

            try{
                ArrayList<ModelCommand> modelarraylist = new ArrayList<ModelCommand>();
                modelarraylist.add(new ModelCommand("TK102", "Monitor", "monitor%1"));
                modelarraylist.add(new ModelCommand("TK102", "Levizje", "move%1"));
                modelarraylist.add(new ModelCommand("TK102", "JoLevizje", "nomove%1"));
                modelarraylist.add(new ModelCommand("TK102", "Thirrje", "%2"));
                modelarraylist.add(new ModelCommand("TK102", "Vendodhje", "stockade%1 latitude;longitude"));
                modelarraylist.add(new ModelCommand("TK103", "Monitor",  "monitor%1"));
                modelarraylist.add(new ModelCommand("TK103", "Levizje",  "move%1"));
                modelarraylist.add(new ModelCommand("TK103", "JoLevizje", "nomove%1"));
                modelarraylist.add(new ModelCommand("TK103", "Vendodhje", "stockade%1 latitude;longitude"));
                modelarraylist.add(new ModelCommand("GPS103", "Monitor", "monitor%1"));
                modelarraylist.add(new ModelCommand("GPS107", "Monitor", "monitor%1"));
                modelarraylist.add(new ModelCommand("MT01", "Vendodhje", "W%1,100"));
                modelarraylist.add(new ModelCommand("MT01", "TejkalimShpejtesie", "W%1,005,$"));
                modelarraylist.add(new ModelCommand("MT01", "Levizje", "W%1,006,$"));
                modelarraylist.add(new ModelCommand("MT01", "RebotGsm&Gps", "W%1,900###"));
                modelarraylist.add(new ModelCommand("MT01", "GetImei", "W%1,601"));
                modelarraylist.add(new ModelCommand("MT01", "GetVersionNoSerialNo", "W%1,600"));
                modelarraylist.add(new ModelCommand("MT01", "CheckGpsParametersSettings", "WWW"));
                modelarraylist.add(new ModelCommand("MT01", "ActiveAudio", "W%1,050,11"));
                modelarraylist.add(new ModelCommand("MT01", "OpenCrashSensor", "W%1,028,1"));
                modelarraylist.add(new ModelCommand("MT01", "ACCOnSmsInfo", "W%1,029,1"));
                modelarraylist.add(new ModelCommand("MT01", "StopCar", "W%1,020,1,1"));
                modelarraylist.add(new ModelCommand("MT01", "StartCar", "W%1,020,1,0"));
                modelarraylist.add(new ModelCommand("CT02", "Vendodhje", "W%1,100"));
                modelarraylist.add(new ModelCommand("CT02", "TejkalimShpejtesie", "W%1,005,$"));
                modelarraylist.add(new ModelCommand("CT02", "Levizje", "W%1,006,$"));
                modelarraylist.add(new ModelCommand("CT02", "RebotGsm&Gps", "W%1,900###"));
                modelarraylist.add(new ModelCommand("CT02", "GetImei", "W%1,601"));
                modelarraylist.add(new ModelCommand("CT02", "GetVersionNoSerialNo", "W%1,600"));
                modelarraylist.add(new ModelCommand("CT02", "CheckGpsParametersSettings", "WWW"));
                modelarraylist.add(new ModelCommand("CT02", "ActiveAudio", "W%1,050,11"));
                modelarraylist.add(new ModelCommand("CT02", "OpenCrashSensor", "W%1,028,1"));
                modelarraylist.add(new ModelCommand("CT02", "ACCOnSmsInfo", "W%1,029,1"));
                modelarraylist.add(new ModelCommand("CT02", "StopCar", "W%1,020,1,1"));
                modelarraylist.add(new ModelCommand("CT02", "StartCar", "W%1,020,1,0"));
                modelarraylist.add(new ModelCommand("OCT800", "Vendodhje", "CHK#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "TejkalimShpejtesie", "SD$#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "Levizje", "MOVE$#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "RebotGsm&Gps", "REBOOT#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "GetImei", "IMEI#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "GetVersionNoSerialNo", "VER#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "ACCOnSmsInfo", "SOO1111#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "StopCar", "STP#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "StartCar", "SK#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "DetailAdress", "ADD#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "SetGPRSInterval", "TIME$#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "OpenDoor", "OpenCD#%1"));
                modelarraylist.add(new ModelCommand("OCT800", "CloseDoor", "CloseCD#%1"));
                db.addContentsFromArraylist(modelarraylist);
            }
            catch(Exception e){
                mbview("Error command Insert db!", e.getMessage());
            }
        }

        Cursor cursor = db.getTableContents("SELECT DISTINCT brand FROM brands");
        bindSpinnerFromCursor(cursor, R.id.spbrand);
        Spinner sp = (Spinner)findViewById(R.id.spbrand);
        String sel = sp.getSelectedItem().toString();
        bindSpinnerByArray(R.id.spmodel, sel);
        cursor.close();

        sp.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Spinner sp = (Spinner)findViewById(R.id.spbrand);
                String sel = sp.getSelectedItem().toString();
                Cursor cursor = db.getTableContent("brands", new String[]{"model"}, "brand", new String[]{sel});
                bindSpinnerFromCursor(cursor, R.id.spmodel);
                cursor.close();
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        //add button
        btnAdd2List = (Button) findViewById(R.id.btnAddList);
        btnAdd2List.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText txtname = (EditText)findViewById(R.id.txtTrackName);
                Spinner spb = (Spinner)findViewById(R.id.spbrand);
                Spinner spm = (Spinner)findViewById(R.id.spmodel);
                EditText txtpn = (EditText)findViewById(R.id.txtPhoneNumber);
                EditText txtps = (EditText)findViewById(R.id.txtpass);
                try{
                    Locale defloc = Locale.getDefault();
                    if(!txtname.getText().toString().equals("") && !txtpn.getText().toString().equals("") && !txtps.getText().toString().equals("")){
                        db.addTrackerContent(new TRACKER(Integer.parseInt(uid), txtname.getText().toString().toUpperCase(defloc), spb.getSelectedItem().toString(), spm.getSelectedItem().toString(), txtpn.getText().toString(), txtps.getText().toString(), "#909090"));
                        int lasttid = db.getLastId("id", "trackers");
                        ArrayList<ModelCommand> mctracker = new ArrayList<ModelCommand>();
                        mctracker = db.getCommandsByModel(spm.getSelectedItem().toString());
                        db.addTrackerCommandsFromArraylist(mctracker, lasttid);
                        mbview("Add", "Content Added!");
                        startActivity(new Intent(gpsTrackerAdd.this, GpsTrackerViewList.class).putExtra("uid", uid));
                        gpsTrackerAdd.this.finish();
                    }
                    else{
                        mbview("Add", "Fill Form!");
                    }
                }
                catch (SQLiteException ex){
                    mbview("Error db!", ex.getMessage());
                }
            }
        });
    }

    public String getLastSMS()
    {
        Uri uriSMS = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMS, null, null, null,null);
        String sms = "";
        if (cur.moveToFirst()){
            sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";
        }
        cur.close();
        return sms;
    }

    public void bindSpinner(int strarr, int spnr)
    {
        Spinner spinner = (Spinner) findViewById(spnr);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, strarr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_brand_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_brand:
                startActivity(new Intent(gpsTrackerAdd.this, gpsTrackerNewBrand.class).putExtra("uid", uid));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
