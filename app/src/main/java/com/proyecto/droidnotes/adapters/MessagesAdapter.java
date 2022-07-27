package com.proyecto.droidnotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        // METODO PARA SABER QUE NOSOTROS ENVIAMOS EL MENSAJE / TRABAJANDO CON ID-SENDER
        // INICIO DE LA CONFIGURACION

        // SI NOSOTROS ENVIAMOS EL MENSAJE
        if (message.getIdSender().equals(authProvider.getId())){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
              RelativeLayout.LayoutParams.WRAP_CONTENT,
              RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            // NUESTRO MENSAJE SE POSICIONARA A LA DERECHA
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // ESTABLECEMOS MARGENES
            params.setMargins(100, 0, 0, 0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(30,20,50,20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.bubble_corner_rigth));
            holder.textViewMessage.setTextColor(Color.WHITE);
            holder.textViewDate.setTextColor(Color.WHITE);
            holder.imageViewCheck.setVisibility(View.VISIBLE);

            // ESTADO DEL MENSAJE VISTO O NO VISTO
            if (message.getStatus().equals("ENVIADO")){
                holder.imageViewCheck.setImageResource(R.drawable.icon_double_check_gray);
            }else if (message.getStatus().equals("VISTO")){
                holder.imageViewCheck.setImageResource(R.drawable.icon_double_check_blue);
            }


        }else {
            // SI NOSOTROS RECIBIMOS MENSAJE
            //  PARA SABER SI SOMOS LOS USUARIOS RECEPTORES
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            // NUESTRO MENSAJE SE POSICIONARA A LA DERECHA
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0, 0, 100, 0);
            holder.linearLayoutMessage.setLayoutParams(params);
            holder.linearLayoutMessage.setPadding(80,20,30,20);
            holder.linearLayoutMessage.setBackground(context.getResources().getDrawable(R.drawable.bubble_corner_left));
            holder.textViewMessage.setTextColor(Color.BLACK);
            holder.textViewDate.setTextColor(Color.BLACK);
            holder.imageViewCheck.setVisibility(View.GONE);
        }


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
        // VARIABLES ==========================================================================
        TextView textViewMessage, textViewDate;
        ImageView imageViewCheck;
        LinearLayout linearLayoutMessage;

        View myView;
        // CIERRE DE VARIABLES ====================================================================

        public ViewHolder(View view){
            super(view);

            //VARIABLE QUE REPRESENTA A CADA UNO DE LOS ITEMS DE LA LISTA DE CONTACTS
            myView = view;

            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDate);
            imageViewCheck = view.findViewById(R.id.imageViewCheck);
            linearLayoutMessage = view.findViewById(R.id.linearLayoutMessage);

        }
    }


}
