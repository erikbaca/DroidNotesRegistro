package com.proyecto.droidnotes.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.proyecto.droidnotes.channel.NotificationHelper;
import com.proyecto.droidnotes.models.Message;

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

        if (title != null) {
            if (title.equals("MENSAJE")) {
                showNotificationMessage(data);
            } else {
                showNotification(title, body, idNotification);
            }
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

    // METODO PARA OBTENER LOS MENSAJES
    private void showNotificationMessage(Map<String, String> data) {
        // CAPTURAMOS LOS VALORES
        String idNotification = data.get("idNotification");
        String usernameSender = data.get("usernameSender");
        String usernameReceiver = data.get("usernameReceiver");
        String messagesJSON = data.get("messagesJSON");

        // CONVERTIR EL OBJETO GSON A UN OBJETO DE MENSAJES
        Gson gson = new Gson();
        Message[] messages = gson.fromJson(messagesJSON, Message[].class);

        NotificationHelper helper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = helper.getNotificationMessage(messages ,usernameReceiver, usernameSender);
        int id = Integer.parseInt(idNotification);
        Log.d("NOTIFICACION", "ID: " + id);
        Log.d("NOTIFICACION", "usernameSender: " + usernameSender);
        Log.d("NOTIFICACION", "usernameReceiver: " + usernameReceiver);
        helper.getManager().notify(id, builder.build());
    }
}
