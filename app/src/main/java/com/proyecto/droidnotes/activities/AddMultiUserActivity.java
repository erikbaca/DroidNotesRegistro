package com.proyecto.droidnotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.adapters.ContactsAdapter;
import com.proyecto.droidnotes.adapters.MultiUsersAdapter;
import com.proyecto.droidnotes.models.User;
import com.proyecto.droidnotes.providers.AuthProvider;
import com.proyecto.droidnotes.providers.UsersProvider;
import com.proyecto.droidnotes.utils.MyToolbar;

import java.util.ArrayList;

public class AddMultiUserActivity extends AppCompatActivity {

    ////////////////////// VARIABLES ////////////////////////////////////
    RecyclerView mRecyclerViewContacts;

    FloatingActionButton mFabCheck;
    MultiUsersAdapter mAdapter;

    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    ArrayList<User> mUsersSelected;
    //////////////////// CIERRE /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multi_user);

        MyToolbar.show(AddMultiUserActivity.this, "Añadir Grupo", true);

        // INSTANCIAS DE VARIABLES =================================================================
        mFabCheck = findViewById(R.id.fabCheck);

        mRecyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        // ==========================================================================================

        //PARA QUE LO ELEMENTOS SE POSICIONEN UNO DEBAJO DEL OTRO
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddMultiUserActivity.this);
        mRecyclerViewContacts.setLayoutManager(linearLayoutManager);

        // EVENTO CLICK PARA AÑADIR PARTICIPANTES AL GRUPO
        mFabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUsersSelected != null){
                    // RECORRER LA LISTA DE USUARIOS SELECCIONADOS
                    for (User u: mUsersSelected){
                        Log.d("USUARIOS", "nombre: " + u.getUsername());
                    }
                }

            }
        });
    }


    // METODO PARA ESTABLECER TODOS LOS USUARIOS SELECCIONADOS Y GUARDARLOS EN LA LISTA MUSERSSELECTED
    public void setUsers(ArrayList<User> users){
      mUsersSelected = users;
    }



    @Override
    public void onStart() {
        super.onStart();
        // CONSULTA A LA BASE DE DATOS
        Query query = mUsersProvider.getAllUserByname();
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        mAdapter = new MultiUsersAdapter(options, AddMultiUserActivity.this);
        mRecyclerViewContacts.setAdapter(mAdapter);
        // QUE EL ADAPTER ESCUCHE LOS CAMBIOS EN TIEMPO REAL
        mAdapter.startListening();
    }


    // DETENER EL METODO ONSTART
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}