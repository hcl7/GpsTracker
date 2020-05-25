package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class gpsTrackerLogs extends AppCompatActivity {

    final Context context = this;
    final ArrayList<Logs> logs = new ArrayList<Logs>();
    String uid;
    String sms;
    String sim;
    int itemPosition;
    boolean isLongClick = false;
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_logs);

        uid = getIntent().getStringExtra("uid");
        sim = getIntent().getStringExtra("sim");
        final pafap_db dbt = new pafap_db(this);
        Cursor cursor = dbt.getTableContents("SELECT logs.*, trackers.name FROM logs, trackers WHERE logs.sim = trackers.phone AND logs.uid = " + Integer.parseInt(uid) + " AND trackers.phone = '" + String.valueOf(sim) + "' ORDER BY id DESC");
        final ListView listview = (ListView) findViewById(R.id.lvLogs);

        if (cursor.moveToFirst()){
            do{
                Logs log = new Logs();
                log.setID(Integer.parseInt(cursor.getString(0)));
                log.setUid(Integer.parseInt(cursor.getString(1)));
                log.setSim(cursor.getString(2));
                log.setSms(cursor.getString(3));
                log.setDate(cursor.getString(4));
                log.setColor(cursor.getString(5));
                log.setName(cursor.getString(6));
                logs.add(log);
            }while (cursor.moveToNext());
        }
        dbt.close();

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < logs.size(); ++i) {
            list.add(logs.get(i).getSim() + " " + logs.get(i).getName() + " " + logs.get(i).getDate());
        }

        //listview actions
        final StableArrayAdapterLogs adapter = new StableArrayAdapterLogs(this, android.R.layout.simple_list_item_1,logs, list);
        listview.setAdapter(adapter);

        //listview click logs;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = false;
                itemPosition = position;
                sms = logs.get(itemPosition).getSms();
                invalidateOptionsMenu();
                mbview("Logs View", "Uid: " + String.valueOf(logs.get(itemPosition).getUid()) + " Sim: " + logs.get(itemPosition).getSim() + " Message: " + logs.get(itemPosition).getSms() + " Date: " + logs.get(itemPosition).getDate());
            }
        });

        //listview longclick logs;
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = true;
                itemPosition = position;
                sms = logs.get(itemPosition).getSms();
                invalidateOptionsMenu();
                return isLongClick;
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
        inflater.inflate(R.menu.logs_menu, menu);
        if(isLongClick){
            menu.getItem(0).setVisible(true);
        }
        else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(isLongClick){
            menu.getItem(0).setVisible(true);
        }
        else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        pafap_db dbt = new pafap_db(this);
        switch (item.getItemId()) {
            case R.id.action_log_delete:
                try{
                    dbt.deleteTableContent("logs", "id", logs.get(itemPosition).getID());
                    mbview("Delete Log", "Log deleted!");
                    dbt.close();
                    finish();
                    startActivity(getIntent());
                }catch (Exception ex){
                    mbview("db error!", ex.getMessage());
                }
                return true;
            case R.id.action_log_view_map:
                startActivity(new Intent(gpsTrackerLogs.this, gpsTrackerViewMapLog.class).putExtra("sms", sms));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


class StableArrayAdapterLogs extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    ArrayList<Logs> objects;
    List<String> list;
    public StableArrayAdapterLogs(Context context, int textViewResourceId, ArrayList<Logs> objects, List<String> list) {
        super(context, textViewResourceId, list);
        this.objects = objects;
        this.list = list;
        for (int i = 0; i < this.list.size(); ++i) {
            mIdMap.put(this.list.get(i), i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position,convertView, parent);
        View view = convertView;
        view.setBackgroundColor(Color.parseColor(this.objects.get(position).getColor()));
        return view;
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}