package com.proyecto.droidnotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.activities.ChatActivity;
import com.proyecto.droidnotes.models.Chat;
import com.proyecto.droidnotes.models.Message;
import com.proyecto.droidnotes.models.User;
import com.proyecto.droidnotes.providers.AuthProvider;
import com.proyecto.droidnotes.providers.UsersProvider;
import com.proyecto.droidnotes.utils.RelativeTime;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {

    // VARIABLES ===================================================================================
    Context context;
    AuthProvider authProvider;
    UsersProvider usersProvider;
    User user;
    ListenerRegistration listener;
    // =============================================================================================

    //CREAMOS CONSTRUCTOR PARA LA CLASE PRINCIPAL
    public MessagesAdapter(FirestoreRecyclerOptions options, Context context)
    {
        super(options);
        this.context = context;
        // INSTANCIAS ===============================================
        authProvider = new AuthProvider();
        usersProvider = new UsersProvider();
        user = new User();
        // ==========================================================
    }


    // ESTABLECEMOS LA INFORMACION
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Message message) {
        //obtenemos el mensaje que queremos mostrar
        holder.textViewMessage.setText(message.getMessage());
        // CONVETIR LA FECHA DE COLLECTION
        holder.textViewDate.setText(RelativeTime.timeFormatAMPM(message.getTimestamp(), context));

    }



    public ListenerRegistration getListener(){
        return listener;
    }



    // INSTANCIAMOS LA VISTA O EL LAYOUT
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent,false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // INSTANCIARLOS ==========================================================================
        TextView textViewMessage, textViewDate;
        ImageView imageViewCheck;

        View myView;
        public ViewHolder(View view){
            super(view);
            //VARIABLE QUE REPRESENTA A CADA UNO DE LOS ITEMS DE LA LISTA DE CONTACTS
            myView = view;

            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDate);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
        }
    }


}
