package com.proyecto.droidnotes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.adapters.ChatsAdapter;
import com.proyecto.droidnotes.adapters.MessagesAdapter;
import com.proyecto.droidnotes.models.Chat;
import com.proyecto.droidnotes.models.Message;
import com.proyecto.droidnotes.models.User;
import com.proyecto.droidnotes.providers.AuthProvider;
import com.proyecto.droidnotes.providers.ChatsProvider;
import com.proyecto.droidnotes.providers.MessagesProvider;
import com.proyecto.droidnotes.providers.UsersProvider;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    // VARIABLES GLOBALES ==========================================================================
    String mExtraIdUser;
    String mExtraIdChat;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    MessagesProvider mMessageProvider;

    ImageView mImageViewBack;
    TextView mTextViewUsername;
    CircleImageView mCircleImageUser;

    // MESSAGE
    EditText mEditextMessage;
    ImageView mImageViewSend;
    // ==================

    ImageView mImageViewSelectPictures;

    MessagesAdapter mAdapter;
    RecyclerView mRecyclerViewMessages;
    LinearLayoutManager mLinearLayoutManager;

    Options mOptions;
    // Arreglo que almacene las url de las imagenes que seleccionemos
    ArrayList<String> mReturnValues = new ArrayList<>();
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // INSTANCIAS ==============================================================================
        mExtraIdUser = getIntent().getStringExtra("idUser");
        mExtraIdChat = getIntent().getStringExtra("idChat");

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();
        mMessageProvider = new MessagesProvider();

        mEditextMessage = findViewById(R.id.editTextMessage);
        mImageViewSend = findViewById(R.id.imageViewSend);
        mRecyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        mImageViewSelectPictures = findViewById(R.id.imageViewSelectPictures);


        // LA INFORMACION QUE SE MOSTRARA SE REFLEJARA UNA DEBAJO DEL OTRO
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewMessages.setLayoutManager(mLinearLayoutManager);

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(5)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                            //Pre selected Image Urls
                .setExcludeVideos(true)
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.All)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage


        // =========================================================================================
        showChatToolbar(R.layout.chat_toolbar);
        getUserInfor();


            checkIfExistChat();


        // EVENTO CLICK AL BOTON ENVIAR DEL CHAT
        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage();
            }
        });


        // AL SELECCIONAR LA IMAGEN ABRIRA LA SELECCION DE IMAGENES
        mImageViewSelectPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPix();
            }
        });

    }

    //  INSTANCIAR EL ADAPTER =================================================

    // SE EJECUTA AL ABRIR EL ACTIVITY
    @Override
    protected void onStart() {
        super.onStart();
        if (mAdapter != null){
            mAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null){
            mAdapter.stopListening();
        }

    }

    //INICIALIZA NUESTRA LIBRERIA PARA SELECCIONAR LA IMAGEN
    private void startPix()
    {
        Pix.start(ChatActivity.this, mOptions);
    }

    // ========================================================================

    //  CREACION DEL MENSAJE
    private void createMessage() {
        String textMessage = mEditextMessage.getText().toString();
        if (!textMessage.equals("")) {
            // CREAMOS MODELO DE TIPO MESSAGE
            Message message = new Message();
            // CHAT AL CUAL PERTENECEN LO MENSAJES QUE CREAREMOS
            message.setIdChat(mExtraIdChat);
            // NUESTRO USUARIO YA QUE ESTAMOS ESCRIBIENDO EL MENSAJE Y ENVIANDOLO
            message.setIdSender(mAuthProvider.getId());
            // USUARIO DE RECIBE EL MENSAJE
            message.setIdReceiver(mExtraIdUser);
            // TEXTO O MENSAJE
            message.setMessage(textMessage);
            message.setStatus("ENVIADO");
            // FECHA
            message.setTimestamp(new Date().getTime());

            // VALIDAMOS QUE LA INFORMACION SE HAYA CREADO CORRECTAMENTE
            mMessageProvider.create(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    mEditextMessage.setText("");
                    if (mAdapter != null){
                        mAdapter.notifyDataSetChanged();
//                        // Toast.makeText(ChatActivity.this, "El mensaje se creo correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Ingresa el mensaje", Toast.LENGTH_SHORT).show();
        }
    }

    // METODO PARA VERIFICAR SI EL CHAT EXISTE
    private void checkIfExistChat() {
        mChatsProvider.getChatByUser1AndUser2(mExtraIdUser, mAuthProvider.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null){
                    if (queryDocumentSnapshots.size() == 0){
                        createChat();
                    }
                    else {
                        //OTENEMOS EL ID DEL CHAT
                        mExtraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                        getMessageByChat();
                        updateStatus();
//                        Toast.makeText(ChatActivity.this, "El chat ya existe entre estos dos usuarios", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateStatus() {
        mMessageProvider.getMessageNotRead(mExtraIdChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot document: queryDocumentSnapshots.getDocuments()){
                    Message message = document.toObject(Message.class);

                    // UNICAMENTE VALIDAR QUE ACTUALICE EL ESTADO DE LOS MENSAJES QUE ME ENVIAn
                    if (!message.getIdSender().equals(mAuthProvider.getId())){
                        mMessageProvider.updateStatus(message.getId(), "VISTO");
                    }
                }
            }
        });
    }

    // METODO PARA OBTENER LOS MENSAJES
    private void getMessageByChat() {
        // CONSULTA A LA BASE DE DATOS
        Query query = mMessageProvider.getMessageByChat(mExtraIdChat);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        mAdapter = new MessagesAdapter(options, ChatActivity.this);
        mRecyclerViewMessages.setAdapter(mAdapter);
        // QUE EL ADAPTER ESCUCHE LOS CAMBIOS EN TIEMPO REAL
        mAdapter.startListening();

        // METODO PARA SABER SI SE CREO UN MENSAJE NUEVO EN LA BDD
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //CONFIGURACIONES
                updateStatus();
                int numberMessage = mAdapter.getItemCount();
                int LastMessagePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (LastMessagePosition == -1 || (positionStart >= (numberMessage -1) && LastMessagePosition == (positionStart -1))){
                    mRecyclerViewMessages.scrollToPosition(positionStart);
                }
            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setId(mAuthProvider.getId() + mExtraIdUser);
        chat.setTimestamp(new Date().getTime());

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mAuthProvider.getId());
        ids.add(mExtraIdUser);

        chat.setIds(ids);

        mExtraIdChat = chat.getId();

        // METODO PARA SABER SI LA INFORMACION SE CREO CORRECTAMENTE
        mChatsProvider.create(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // LLAMAMOS AL METODO OBTENER MENSAJES POR CHAT
                getMessageByChat();
//                Toast.makeText(ChatActivity.this, "El chat se creo correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // VALIDACION EN TIEMPO REAL
    private void getUserInfor() {
        mUsersProvider.getUserInfo(mExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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

    // NOS RETORNA LAS IMAGENES QUE SELECCIONAMOS Y ADEMAS LOS PERMISOS=========================================
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Intent intent = new Intent(ChatActivity.this, ConfirmImageSendActivity.class);
            intent.putExtra("data", mReturnValues);
            startActivity(intent);
        }
    }

    // Metodo para los permisos a uso de la camara y accesar a la galeria
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(ChatActivity.this, mOptions);
            } else {
                Toast.makeText(ChatActivity.this, "Por favor concede los permisos para accesar a la camara!!", Toast.LENGTH_LONG).show();
            }
        }
    }
// =======================================================================================================



}