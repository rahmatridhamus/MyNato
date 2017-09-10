package my.mynato.rahmatridham.mynato.FirebaseConsole;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import my.mynato.rahmatridham.mynato.LandingPage;
import my.mynato.rahmatridham.mynato.R;

import static java.security.AccessController.getContext;

/**
 * Created by rahmatridham on 4/13/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        Toast.makeText(getApplicationContext(), remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
        final String string = remoteMessage.getNotification().getBody();

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
                Notification n = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Notifikasi MyNato")
                        .setContentText(string)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true).build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, n);
            }
        });
    }


}

