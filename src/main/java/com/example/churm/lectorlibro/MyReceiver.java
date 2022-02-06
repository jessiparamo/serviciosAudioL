package com.example.churm.lectorlibro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.getSystemService;

public class MyReceiver extends BroadcastReceiver {

    Context d ;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        this.d=context;
        Toast.makeText(context, "Algo", Toast.LENGTH_SHORT).show();

    }

    String channel_name="audioLibrosChannel";


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = "audioLibros";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(d,NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    String channel_id = "AudioLibrosForegroundService";
    public void notificar(){

        Toast.makeText(d, "Intendando notificar", Toast.LENGTH_SHORT).show();
        createNotificationChannel();
        // Create an explicit intent for an Activity in your app
        Intent intentN = new Intent(d,MyReceiver.class);
        intentN.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(d, 0, intentN, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(d, channel_id)
                .setSmallIcon(R.drawable.books)
                .setContentTitle("Audio libros")
                .setContentText("Reproduciendo")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(d);

        notificationManagerCompat.notify(354,builder.build());

    }
}
