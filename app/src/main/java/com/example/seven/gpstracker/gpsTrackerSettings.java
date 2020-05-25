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
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class gpsTrackerSettings extends AppCompatActivity {

    final Context context = this;
    String tid = "";
    String cmdEdit = "";
    boolean isLongClick = false;
    int itemPosition;
    int cid;
    final ArrayList<ModelCommand> commandList = new ArrayList<ModelCommand>();
    final pafap_db dbt = new pafap_db(this);

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_settings);

        tid = getIntent().getStringExtra("tid");

        final pafap_db dbt = new pafap_db(this);
        Cursor cursor = dbt.getTableContents("SELECT id, name, model, command FROM trcommands WHERE tid = " + Integer.parseInt(tid) + "");
        final ListView listview = (ListView) findViewById(R.id.lvCommands);
        if (cursor.moveToFirst()){
            do{
                ModelCommand cmtracker = new ModelCommand();
                cmtracker.setId(Integer.parseInt(cursor.getString(0)));
                cmtracker.setName(cursor.getString(1));
                cmtracker.setModel(cursor.getString(2));
                cmtracker.setCommand(cursor.getString(3));
                commandList.add(cmtracker);
            }while (cursor.moveToNext());
        }
        dbt.close();

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < commandList.size(); ++i) {
            list.add(commandList.get(i).getName());
        }

        final TrackerCommandArrayAdapter adapter = new TrackerCommandArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        ColorDrawable lvcolor = new ColorDrawable(this.getResources().getColor(R.color.text_color));
        listview.setDivider(lvcolor);

        //select command on click;
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = false;
                itemPosition = position;
                cid = commandList.get(itemPosition).getId();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Edit Command!");
                final EditText input = new EditText(context);
                input.setText(commandList.get(itemPosition).getCommand());
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.gps);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        cmdEdit = input.getText().toString();
                        dbt.updateTableField("trcommands", "command", cmdEdit, "id", cid);
                        finish();
                        startActivity(getIntent());
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                invalidateOptionsMenu();
            }
        });

        //select command long click;
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = true;
                itemPosition = position;
                cid = commandList.get(itemPosition).getId();
                invalidateOptionsMenu();
                return isLongClick;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.commands_menu, menu);
        if(isLongClick){
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        }
        else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(isLongClick){
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
        }
        else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_activate:
                dbt.updateTableField("trcommands", "status", "ACTIVATED", "id", commandList.get(itemPosition).getId());
                mbview("Command", "Command Activated!");
                return true;
            case R.id.action_deactivate:
                dbt.updateTableField("trcommands", "status", "DEACTIVATED", "id", commandList.get(itemPosition).getId());
                mbview("Command", "Command Deactivated!");
                return true;
            case R.id.action_add_comm:
                startActivity(new Intent(gpsTrackerSettings.this, gpsTrackerAddCommand.class).putExtra("tid", tid));
                this.finish();
                return true;
            case R.id.action_del_comm:
                dbt.deleteTableContent("trcommands", "id", commandList.get(itemPosition).getId());
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


class TrackerCommandArrayAdapter extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    List<String> objects;
    public TrackerCommandArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
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