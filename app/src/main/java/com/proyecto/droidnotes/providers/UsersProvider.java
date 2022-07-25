package com.proyecto.droidnotes.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.droidnotes.models.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UsersProvider {

    private CollectionReference mCollection;

    // Para utlizar las propiedades de estas clase un constructor vacio
    public UsersProvider()
    {
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    //Metodo para verificar si el usuario existe
    public DocumentReference getUserInfo(String id)
    {
      return mCollection.document(id);
    }

    //Metodo que permita almacenar un usuario en la BDD
    public Task<Void> create(User user)
    {
        return mCollection.document(user.getId()).set(user);
    }

    // METODO PARA EL MANEJO DE CRUD Y EL MANEJO COMPLETO DE DATOS EN FIREBASE
    public Task<Void> update(User user)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("image", user.getImage());

        return mCollection.document(user.getId()).update(map);
    }

    // METODO PARA ELIMINAR/ACTUALIZAR EL CAMPPO IMAGE DE LA BDD
    public Task<Void> updateImage(String id, String url)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("image", url);
        return mCollection.document(id).update(map);
    }




}
