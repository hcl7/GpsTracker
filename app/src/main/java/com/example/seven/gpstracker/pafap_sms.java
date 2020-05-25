package com.example.seven.gpstracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class pafap_sms extends BroadcastReceiver {
	final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	private ContentResolver resolver;
	int uid;
	public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        final pafap_db dblog = new pafap_db(context);
        final pafap_strings str = new pafap_strings();
        final pafap_strings strObj = new pafap_strings();
        String prefix = "";
        String getNumber = "";
        int index;
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    prefix = str.checkPrefix(senderNum);
                    index = senderNum.indexOf(prefix);
                    getNumber = senderNum.substring(index + prefix.length(), senderNum.length());
                    uid = dblog.getTrackerUidBySim(getNumber);
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message + String.valueOf(uid));
                    if(strObj.checkSMS(message)){
                    	dblog.addTrackerLog(uid, senderNum, "from:" + senderNum + ":" + message);
                    	pafap_strings sl = new pafap_strings();
                    	String color = sl.checkSMS4Color(message);
                    	int lid = dblog.getLastId("id", "logs");
                    	dblog.updateLogsColor(color, senderNum, lid);
                    	dblog.updateTrackerColor(color, senderNum);
                    	int uid = dblog.getTrackerUidBySim(senderNum);
                    	
                    	//new activity for trackers updated
                    	try{
                    		Intent data = new Intent(context, GpsTrackerViewList.class);
                            data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);                       
                            data.putExtra("uid", String.valueOf(uid));
                            context.startActivity(data);
                    	}catch(Exception e){
                    		Log.e("reciver error", e.getMessage());
                    	}
                    	removeLastSMS(context);
                    }
                }
            }
        } catch (Exception e) {
        	Toast.makeText(context, "Exception smsReceiver: " + e, Toast.LENGTH_LONG).show();             
        }
    }

	public String getLastSms(Context cnt) {
		Cursor cur = cnt.getContentResolver().query(SMS_INBOX, null, null, null,null);
		String sms = "";
		while (cur.moveToFirst()) {
			sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";
		}
		return sms;
	}
	
	public void removeLastSMS(Context context){
		try{
			Uri uriSMS = Uri.parse("content://sms/inbox");
		    Cursor cur = context.getContentResolver().query(uriSMS, null, null, null,null);
		    if (cur.moveToFirst()){
		    	context.getContentResolver().delete(uriSMS, null, null);
		    }
		    cur.close();
		}
		catch(Exception ex){
			Log.d("Remove Last SMS error!",  ex.getMessage());
		}
	}

	public int getMessageCountUnread(){
		Cursor c = resolver.query(SMS_INBOX, null, "read = 0", null, null);
		int unreadMessagesCount = c.getCount();
		return unreadMessagesCount;
	}

	public String getMessageAll(){
		Cursor cur = resolver.query(SMS_INBOX, null, null, null,null);
		String sms = "";
		while (cur.moveToNext()) {
			sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";
		}
		return sms;
	}

	public String getMessageUnread() {
		Cursor cur = resolver.query(SMS_INBOX, null, null, null,null);
		String sms = "";
		int hitung = 0;
		while (cur.moveToNext()) {
			sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";
			if(hitung == getMessageCountUnread())
				break;
			hitung++;
		}
		return sms;
	}
	
	//notification function
	public void Notification(Context context, String message) {
		//notification id
		int notificationID = 0;
        // Set Notification Title
        String strtitle = context.getString(R.string.notificationtitle);
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, GpsTrackerViewList.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", message);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
 
        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.drawable.gps)
                // Set Ticker Message
                .setTicker(message)
                // Set Title
                .setContentTitle(context.getString(R.string.notificationtitle))
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                .addAction(R.drawable.gps, "View", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(notificationID, builder.build());
    }
}
