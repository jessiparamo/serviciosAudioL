package com.example.churm.lectorlibro;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

//jessica SP
public class MyService extends Service implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    public MyService() {

    }

    Activity activity;
    View view;
    public int libroID;

    private final IBinder binder = new miBinder();
    private final Random random = new Random();

    MediaPlayer mediaPlayer;
    MediaController mediaController;

    public void setMediaPlayer(Activity activity, Libro libro, View view, int idLibro) {
        this.activity=activity;
        this.view= view;
        this.libroID=idLibro;

        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(activity);
        Uri audio = Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(activity, audio);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
        }
    }




    @Override
    public void onPrepared(MediaPlayer mp) {
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(activity);

            mediaPlayer.start();

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(view);
        mediaController.setPadding(0, 0, 0,110);
        mediaController.setEnabled(true);
        mediaController.show();

        Intent intent = new Intent(activity.getApplicationContext(),MyService.class);


    }




    String channel_name="audioLibrosChannel";
    String channel_id = "AudioLibrosForegroundService";

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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    public void notificar(){

        Toast.makeText(activity, "Intendando notificar", Toast.LENGTH_SHORT).show();
        createNotificationChannel();
        // Create an explicit intent for an Activity in your app
        Intent intentN = new Intent(this, this.getClass());
        intentN.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentN, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.books)
                .setContentTitle("Audio libros")
                .setContentText("Reproduciendo")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(354,builder.build());

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(activity, "onstartcomand", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(activity, "onstart", Toast.LENGTH_SHORT).show();
        super.onStart(intent, startId);
    }



    @Override
    public ComponentName startService(Intent service) {
        Toast.makeText(activity, "startService", Toast.LENGTH_SHORT).show();
        return super.startService(service);
    }



    public void stopping(){
        mediaController.hide();
        try {
            mediaPlayer.pause();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
    }





    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public MediaController getMediaController(){
        return this.mediaController;
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }



    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    public class miBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Cargando, espere por favor", Toast.LENGTH_SHORT).show();
        return binder;
    }

    public void showMedia(){
        mediaController.show();
    }
    public int getRandomNumber(){
        return random.nextInt(10);
    }
}
