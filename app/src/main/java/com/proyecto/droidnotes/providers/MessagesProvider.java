package com.proyecto.droidnotes.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.proyecto.droidnotes.models.Message;

public class MessagesProvider {

    CollectionReference mCollection;

    public MessagesProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Messages");
    }

    public Task<Void> create(Message message){
        //OBTENER EL ID DE LA COLECCION MESSAGES
        DocumentReference document = mCollection.document();
        message.setId(document.getId());
        return document.set(message);
    }

    // CONSULTA A LA BASE DE DATOS // OBTENER LOS MENSAJES POR CHAT
    public Query getMessageByChat(String idChat){
        // OBTENIENDO MENSAJES CUYO ID SEA DEL ID DEL CHAT y ORDENAR LA INFO A TRAVES DEL TIMESTAMP
        return mCollection.whereEqualTo("idChat", idChat).orderBy("timestamp", Query.Direction.ASCENDING);


    }
}
