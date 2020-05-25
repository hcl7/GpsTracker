package com.example.seven.gpstracker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;

public class gpsTrackerViewMapLog extends FragmentActivity implements OnMapReadyCallback {

    final Context context = this;
    double latitude = 41.3275;
    double longitude = 19.818889;
    String title = "Tirana";
    String sms = "";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker_view_map_log);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pafap_strings ll = new pafap_strings();
        sms = getIntent().getStringExtra("sms");

        if (mMap == null){
            Toast.makeText(getApplicationContext(), "Unable to create maps!", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                String lat = ll.getLatFromSms(sms);
                String lng = ll.getLngFromSms(sms);
                if (!lat.equals("") && !lng.equals("")){
                    try{
                        title = ll.getSender(sms);
                        latitude = Double.parseDouble(lat);
                        longitude = Double.parseDouble(lng);
                        mbview("Your Tracker Location!", "Sender: " + title + " Lat: " + latitude + " Long: " + longitude);
                    }catch(NumberFormatException ex){
                        mbview("Convert Error!", ex.getMessage());
                    }
                }
                else mbview("Message Error!", "Cannot read message!");
            }catch (Exception exe){
                mbview("Read Error!", exe.getMessage());
            }

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            trackMarker(mMap, title, latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(19), 2000, null);
            mMap.moveCamera(center);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(17)
                    .bearing(360)
                    .tilt(30)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void trackMarker(GoogleMap map, String title, double lat, double lng){
        MarkerOptions trackermarker = new MarkerOptions().position(new LatLng(lat, lng)).title(title);
        map.addMarker(trackermarker);
    }

    public String getLastSMS() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = context.getContentResolver().query(uri, null, null ,null,null);
        String body = null;
        String sender = null;
        if(c.moveToFirst()) {
            sender = c.getString(c.getColumnIndexOrThrow("address")).toString();
            body = "from:" + sender + " " + c.getString(c.getColumnIndexOrThrow("body")).toString();
        }
        c.close();
        return body;
    }

    public void removeLastSMS(){
        Uri uriSMS = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMS, null, null, null,null);
        if (cur.moveToFirst()){
            context.getContentResolver().delete(uriSMS, null, null);
        }
        cur.close();
    }

    public void mbview(String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tirana = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(tirana).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tirana));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(17)
                .bearing(360)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
