package com.elisoft.recarga.notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.elisoft.recarga.R;


/**
 * Created by ROMAN on 24/11/2016.
 */

public class MyNotificationManager {


    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    private Context mCtx;


  //  Notification.Builder builder = new Notification.Builder(mContext);
    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //el método mostrará una notificación grande con una imagen
    //los parámetros son título para el título del mensaje, mensaje para el texto del mensaje,
    //url de la imagen grande y una intención que se abrirá
    //cuando toque en la notificación


    //el método mostrará una pequeña notificación
    //los parámetros son título para el título del mensaje,notificacion_conductor_en_proceso
    //mensaje para el texto del mensaje y una intención que se abrirá
    //cuando toque en la notificación

    public void notificacion_con_activity(String title, String message, Intent intent) {
        Uri sonido = Uri.parse("android.resource://"+ this.mCtx.getPackageName() + "/" + R.raw.notificacion);//sonido



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pintent = PendingIntent.getActivity(mCtx,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mCtx);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(sonido)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentInfo(title)
                .setContentIntent(pintent);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(0 /* Request Code */, mBuilder.build());

    }

}






