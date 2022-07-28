package com.proyecto.droidnotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.adapters.OptionsPagerAdapter;
import com.proyecto.droidnotes.models.Message;
import com.proyecto.droidnotes.providers.AuthProvider;
import com.proyecto.droidnotes.providers.ChatsProvider;
import com.proyecto.droidnotes.providers.ImageProvider;
import com.proyecto.droidnotes.providers.MessagesProvider;
import com.proyecto.droidnotes.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.Date;

public class ConfirmImageSendActivity extends AppCompatActivity {

    // VARIABLES GLOBALES =========================================================================
    ViewPager mViewPager;
    // VARIABLES DE USUARIOS EMISOR Y RECEPTOR ====
    String mExtraIdChat;
    String mExtraIdReceiver;
    // CIERRE =====================================
    ArrayList<String> data;
    ArrayList<Message> messages = new ArrayList<>();

    ImageProvider mImageProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatProvier;
    // ============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image_send);
        setStatusBarColor();

        // INSTANCIAS ==============================================================================
        mViewPager = findViewById(R.id.viewPager);
        mAuthProvider = new AuthProvider();

        data = getIntent().getStringArrayListExtra("data");
        mExtraIdChat = getIntent().getStringExtra("idChat");
        mExtraIdReceiver = getIntent().getStringExtra("idReceiver");
        mImageProvider = new ImageProvider();
        mChatProvier = new ChatsProvider();
        // ========================================================================================
        if (data != null){
            for (int i = 0; i < data.size(); i++){
                Message m = new Message();
                m.setIdChat(mExtraIdChat);
                m.setIdSender(mAuthProvider.getId());
                m.setIdReceiver(mExtraIdReceiver);
                m.setStatus("ENVIADO");
                // FECHA
                m.setTimestamp(new Date().getTime());
                m.setType("imagen");
                // URL DE LA IMAGEN QUE SELECCIONAMOS DESDE EL CELULAR
                m.setUrl(data.get(i));
                // MENSAJE POR DEFECTO
                m.setMessage("\uD83D\uDCF7imagen");
                messages.add(m);
            }
        }

        OptionsPagerAdapter pagerAdapter = new OptionsPagerAdapter(
          getApplicationContext(),
          getSupportFragmentManager(),
                dpToPixels(2, this),
                data
        );
        ShadowTransformer transformer = new ShadowTransformer(mViewPager, pagerAdapter);
        transformer.enableScaling(true);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(false, transformer);

    }

    // METODO PARA IMPRIMIR LOS COMENTARIOS
    public void send(){
        for (int i = 0; i < messages.size(); i++){
            mImageProvider.uploadMultiple(ConfirmImageSendActivity.this, messages);
            finish();
        }

    }

    public void setMessage(int position, String message){
        messages.get(position).setMessage(message);
    }


    public static float dpToPixels(int dp, Context context){
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorFullBlack));
        }
    }
}