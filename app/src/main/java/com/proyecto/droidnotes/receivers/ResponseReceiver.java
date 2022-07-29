package com.proyecto.droidnotes.receivers;

import static com.proyecto.droidnotes.services.MyFirebaseMessagingClient.NOTIFICATION_REPLY;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.gson.Gson;
import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.channel.NotificationHelper;
import com.proyecto.droidnotes.models.Message;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

public class ResponseReceiver extends BroadcastReceiver {

    //  PODREMOS OBTENER LOS PARAMETROS A TRAVES DEL INTENT, SEA DE LA CLASE MyFirebaseMessaging...
    @Override
    public void onReceive(Context context, Intent intent) {
        getMyImage(context, intent );
    }

    private void showNotification(Context context, Intent intent, Bitmap myBitmap) {
        // OBTENEMOS EL TEXTO DIGITADO POR EL USUARIO
        String message = getMessageText(intent).toString();

        int id = intent.getExtras().getInt("idNotification");
        String messagesJSON = intent.getExtras().getString("messages");
        String usernameSender = intent.getExtras().getString("usernameSender");
        String imageSender = intent.getExtras().getString("imageSender");
        String imageReceiver = intent.getExtras().getString("imageReceiver");


        Gson gson = new Gson();
        Message[] messages = gson.fromJson(messagesJSON, Message[].class);

        NotificationHelper helper = new NotificationHelper(context);



        Intent intentResponse = new Intent(context, ResponseReceiver.class);
        // PARAMETROS A ENVIAR
        intentResponse.putExtra("idNotification", id);
        intentResponse.putExtra("messages", messagesJSON);
        intentResponse.putExtra("usernameSender", usernameSender);
        intentResponse.putExtra("imageSender", imageSender);
        intentResponse.putExtra("imageReceiver", imageReceiver);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentResponse, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY).setLabel("Tu mensaje...").build();

        NotificationCompat.Action actionResponse = new NotificationCompat.Action.Builder(
                R.mipmap.ic_logoapp,
                "Responder",
                pendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder builder = helper.getNotificationMessage(messages, message, usernameSender, null, myBitmap,actionResponse );
        helper.getManager().notify(id, builder.build());


        Log.d("NOTIFICACION", "Mensaje input:" + message);
    }


    // METODO PARA OBTENER LA IMAGEN RECIBIDA
    private void getMyImage(Context context, Intent intent) {
        final String myImage = intent.getExtras().getString("imageReceiver");
        if (myImage == null){
            showNotification(context, intent, null);
            return;
        }
        if (myImage.equals("")){
            showNotification(context, intent, null);
            return;
        }
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(context)
                                .load(myImage)
                                .into(new Target() {
                                    // RETORNAR LA IMAGEN CORRECTAMENTE DESDE LA WEB
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        showNotification(context, intent, bitmap);
                                    }
                                    // EN CASO DE QUE LA IMAGEN NO EXISTA EN LA BDD
                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        showNotification(context, intent, null);
                                    }
                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });
                    }
                });
    }




    private CharSequence getMessageText(Intent intent){
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null){
            return remoteInput.getCharSequence(NOTIFICATION_REPLY);
        }
        return null;
    }
}
