package com.fom.msesoft.fomapplication.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.activity.MainActivity_;
import com.fom.msesoft.fomapplication.adapter.OnNotifyListener;
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import lombok.Setter;

public class GCMNotificationIntentService extends IntentService {


	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	Vibrator vibrator;

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification(null);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification(null);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				CustomPerson customPerson = new CustomPerson();
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonString = extras.getString(Config.MESSAGE_KEY);
				try {
					customPerson = objectMapper.readValue(jsonString.substring(1),CustomPerson.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(jsonString.substring(0,1).equals("A"))
					sendNotificationActivity(customPerson);
				else if(jsonString.substring(0,1).equals("N"))
					sendNotification(customPerson);
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}


	private void sendNotification(CustomPerson customPerson) {

		String id = customPerson.getUniqueId();
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this,MainActivity_.class);
		intent.putExtra("uniqueId",customPerson.getUniqueId());
		intent.putExtra("notification","notify");
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.gcmicon)
				.setContentTitle("Friend Of Mine")
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_HIGH)
				.setVibrate(new long[]{500,300,300,300})
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setSound(uri)
				.setStyle(new NotificationCompat.BigTextStyle().bigText("Friend Of Mine"))
				.setContentText(customPerson.getFirstName()+" "+customPerson.getLastName()+" "
						+"Seninle bağlantı kurmak istiyor.");

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		//vibrator.vibrate(500);
		LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Config.REQUEST_RECIEVED));
	}

	private void sendNotificationActivity(CustomPerson customPerson) {

		String id = customPerson.getUniqueId();
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this,MainActivity_.class);
		//intent.putExtra("uniqueId",customPerson.getUniqueId());
		intent.putExtra("notification","activity");
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.gcmicon)
				.setContentTitle("Friend Of Mine")
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_HIGH)
				.setVibrate(new long[]{500,300,300,300})
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setSound(uri)
				.setStyle(new NotificationCompat.BigTextStyle().bigText("Friend Of Mine"))
				.setContentText(customPerson.getFirstName()+" "+customPerson.getLastName()+" "
						+"Bir Etkinlik Paylaştı.");

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		//vibrator.vibrate(500);
		LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Config.REQUEST_RECIEVED));
	}

}
