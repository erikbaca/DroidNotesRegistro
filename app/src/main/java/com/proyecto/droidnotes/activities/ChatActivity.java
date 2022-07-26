package com.proyecto.droidnotes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.models.User;
import com.proyecto.droidnotes.providers.AuthProvider;
import com.proyecto.droidnotes.providers.UsersProvider;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    // VARIABLES GLOBALES ==========================================================================
    String mmExtraIdUser;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    ImageView mImageViewBack;
    TextView mTextViewUsername;
    CircleImageView mCircleImageUser;
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // INSTANCIAS ==============================================================================
        mmExtraIdUser = getIntent().getStringExtra("id");
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();


        // =========================================================================================
        showChatToolbar(R.layout.chat_toolbar);
        getUserInfor();
    }

    // VALIDACION EN TIEMPO REAL
    private void getUserInfor() {
        mUsersProvider.getUserInfo(mmExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                 if (documentSnapshot != null){
                     if(documentSnapshot.exists()){
                         //OBTENIENDO LA INFO DEL USUARIO
                         User user = documentSnapshot.toObject(User.class);
                         mTextViewUsername.setText(user.getUsername());

                         //MOSTRAR LA IMAGEN
                         if (user.getImage() != null){
                             if (!user.getImage().equals("")){
                                 Picasso.with(ChatActivity.this).load(user.getImage()).into(mCircleImageUser);
                             }
                         }
                     }
                 }
            }
        });
    }

    //TOOLBAR PERSONALIZADO
    private  void showChatToolbar(int resource){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);
        actionBar.setCustomView(view);


        // REGRESAR AL HOME ACTIVITY <-
        mImageViewBack = view.findViewById(R.id.imageViewBack);
        mTextViewUsername = view.findViewById(R.id.textViewUsername);
        mCircleImageUser = view.findViewById(R.id.circleImageUser);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}