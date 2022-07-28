package com.proyecto.droidnotes.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.proyecto.droidnotes.channel.NotificationHelper;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {

    // METODO PARA CREAR UN TOKEN DE NOTIFICACIONES
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    // METODO QUE NOS SERVIRA PARA RECIBIR LOS DATOS DE LA NOTIFICACION
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // CAPTURAMOS LA INFORMACION QUE NOS ENVIAN EN LA NOTIFICACION
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        String idNotification = data.get("idNotification");

        if (title != null){
            showNotification(title, body, idNotification);
        }

    }

    // METODO PARA MOSTRAR LA NOTIFICACION
    private void showNotification(String title, String body, String idNotification) {
        NotificationHelper helper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = helper.getNotification(title, body);
        int id = Integer.parseInt(idNotification);
        Log.d("NOTIFICACION", "ID: " + id);
        helper.getManager().notify(id, builder.build());
    }
}
