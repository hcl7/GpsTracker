package com.example.seven.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

public class GpsTrackerViewList extends AppCompatActivity {

    final Context context = this;
    String uid = "";
    String tid = "";
    String cmdEdit = "";
    int cid;
    int itemPosition;
    boolean isLongClick = false;
    int keyCount = 0;
    final pafap_db dbt = new pafap_db(this);
    final ArrayList<TRACKER> trackerList = new ArrayList<TRACKER>();
    final ArrayList<ModelCommand> commandList = new ArrayList<ModelCommand>();
    final ArrayList<ModelCommand> menucommandList = new ArrayList<ModelCommand>();
    String phonenumber = "";
    String password = "";
    String command = "";
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_view_list);

        //user id from other activity
        uid = getIntent().getStringExtra("uid");

        final ListView listview = (ListView) findViewById(R.id.lvtrackers);
        //fill listview with data
        try{
            bindListView(listview);
        }catch(Exception ex){
            mbview("bindListLiew Error!", ex.getMessage());
        }


        //refresh button listview;
        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                trackerList.clear();
                bindListView(listview);
            }
        });

        //click listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                isLongClick = false;
                itemPosition = position;
                tid = String.valueOf(trackerList.get(itemPosition).getID());
                password = trackerList.get(itemPosition).getPassword();
                phonenumber = trackerList.get(itemPosition).getPhoneNumber();
                listview.getChildAt(itemPosition).setBackgroundColor(getResources().getColor(R.color.bg_long_click));
                invalidateOptionsMenu();
                showFilterPopup(view);
            }
        });

        //longClick listview;
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                isLongClick = true;
                itemPosition = position;
                tid = String.valueOf(trackerList.get(itemPosition).getID());
                phonenumber = trackerList.get(itemPosition).getPhoneNumber();
                invalidateOptionsMenu();
                listview.getChildAt(itemPosition).setBackgroundColor(getResources().getColor(R.color.bg_long_click));
                return isLongClick;
            }
        });
        listview.refreshDrawableState();
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
        inflater.inflate(R.menu.viewlist_menu, menu);
        if(isLongClick){
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);

        }
        else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addnew:
                startActivity(new Intent(GpsTrackerViewList.this, gpsTrackerAdd.class).putExtra("uid", uid));
                this.finish();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(GpsTrackerViewList.this, gpsTrackerSettings.class).putExtra("tid", tid));
                //this.finish();
                return true;
            case R.id.action_delete:
                try{
                    dbt.deleteTrackerCommContent(trackerList.get(itemPosition).getID());
                    dbt.deleteTrackerContent(trackerList.get(itemPosition).getName());
                    dbt.deleteTrackerLogsContent(trackerList.get(itemPosition).getPhoneNumber());
                    mbview("Delete Tracker", "Tracker deleted!");
                    finish();
                    startActivity(getIntent());
                }catch (Exception ex){
                    mbview("db error!", ex.getMessage());
                }
                return true;
            case R.id.action_edit:
                startActivity(new Intent(GpsTrackerViewList.this, gpsTrackerEdit.class).putExtra("tid", tid));
                this.finish();
                return true;
            case R.id.action_view_map:
                startActivity(new Intent(GpsTrackerViewList.this, gps_tracker_view_map.class).putExtra("uid", uid));
                //this.finish();
                return true;
            case R.id.action_logs:
                Intent intent = new Intent(GpsTrackerViewList.this, gpsTrackerLogs.class);
                intent.putExtra("uid", uid);
                intent.putExtra("sim", phonenumber);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(isLongClick){
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
            menu.getItem(5).setVisible(true);
        }
        else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(true);
        }
        return true;
    }

    public void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        try{
            Cursor cs = dbt.getTableContents("SELECT id, model, name, command FROM trcommands WHERE tid = " + Integer.parseInt(tid) + " AND status = 'ACTIVATED'");
            if (cs.moveToFirst()){
                do{
                    ModelCommand mc = new ModelCommand();
                    mc.setId(Integer.parseInt(cs.getString(0)));
                    mc.setModel(cs.getString(1));
                    mc.setName(cs.getString(2));
                    mc.setCommand(cs.getString(3));
                    commandList.add(mc);
                    popup.getMenu().add(Menu.NONE, Menu.NONE, commandList.get(0).getId(), commandList.get(0).getName());
                    menucommandList.add(mc);
                    commandList.clear();
                }while(cs.moveToNext());
            }
            else mbview("error!", "table empty");
            cs.close();
        }catch (NumberFormatException ex){
            mbview("error!", ex.getMessage());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for (int i=0;i<menucommandList.size();i++){
                    if (menucommandList.get(i).getId() == item.getOrder()){
                        isLongClick = false;
                        invalidateOptionsMenu();
                        command = menucommandList.get(i).getCommand();
                        final pafap_strings cmdstr = new pafap_strings();
                        if(command.indexOf('%') != -1){
                            int key = command.indexOf('%');
                            String cmd = command.substring(key + 1, key + 2);
                            if(cmd.equals("1")){
                                //edit command
                                if(command.indexOf('$') != -1){
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                    alertDialog.setTitle(menucommandList.get(i).getModel() + " " + menucommandList.get(i).getName());
                                    final EditText input = new EditText(context);
                                    input.setText("");
                                    alertDialog.setView(input);
                                    alertDialog.setIcon(R.drawable.gps);
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {
                                            cmdEdit = cmdstr.replaceString(command, "$", input.getText().toString());
                                            cmdEdit = cmdstr.replaceString(command, "%1", password);
                                            sendSMS(phonenumber, cmdEdit);
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
                                else{
                                    String strcommand = cmdstr.replaceString(command, "%" + cmd, password);
                                    try{
                                        sendSMS(phonenumber, strcommand);
                                        mbview("Sent!", "Command Sent!\nWait for sms answer\nAnd press View Map on Menu!");
                                    }catch(Exception ex){
                                        mbview("SMS error!", ex.getMessage());
                                    }
                                }
                            }
                            else if(cmd.equals("2")){
                                String strcommand = cmdstr.replaceString(command, "%" + cmd, phonenumber);
                                try{
                                    callTracker(strcommand);
                                }catch(Exception ex){
                                    mbview("Call Error!", ex.getMessage());
                                }
                            }
                        }
                        else{
                            sendSMS(phonenumber, command);
                            mbview("Sent!", "Command Sent!\nWait for sms answer\nAnd press View Map on Menu!");
                        }
                    }
                }
                menucommandList.clear();
                return false;
            }
        });
        popup.show();
    }

    public void sendSMS(String phoneNumber, String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void callTracker(String number)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
        catch (ActivityNotFoundException activityException)
        {
            mbview("Call Failed!", activityException.toString());
        }
    }

    public void bindListView(ListView listview){
        pafap_db dbt = new pafap_db(this);
        Cursor cursor = dbt.getTableContents("SELECT id, uid, name, brand, model, phone, pass, stcolor FROM trackers WHERE uid = " + Integer.parseInt(uid) + "");
        if (cursor.moveToFirst()){
            do{
                TRACKER tracker = new TRACKER();
                tracker.setID(Integer.parseInt(cursor.getString(0)));
                tracker.setUID(Integer.parseInt(cursor.getString(1)));
                tracker.setName(cursor.getString(2));
                tracker.setBrand(cursor.getString(3));
                tracker.setModel(cursor.getString(4));
                tracker.setPhoneNumber(cursor.getString(5));
                tracker.setPassword(cursor.getString(6));
                tracker.setColor(cursor.getString(7));
                trackerList.add(tracker);
            }while (cursor.moveToNext());
        }
        dbt.close();

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < trackerList.size(); ++i) {
            list.add(trackerList.get(i).getName());
        }

        //listview actions
        StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, trackerList, list);
        listview.setAdapter(adapter);
    }
}


class StableArrayAdapter extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    ArrayList<TRACKER> objects;
    List<String> list;
    public StableArrayAdapter(Context context, int textViewResourceId, ArrayList<TRACKER> objects, List<String> list) {
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
        String item = getItem(position).toString();
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}