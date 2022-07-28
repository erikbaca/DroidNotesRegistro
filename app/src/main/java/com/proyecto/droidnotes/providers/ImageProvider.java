package com.proyecto.droidnotes.providers;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proyecto.droidnotes.models.Message;
import com.proyecto.droidnotes.utils.CompressorBitmapImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class ImageProvider {

    // VARIABLE GLOBALES ==========================================================================
    StorageReference mStorage;
    FirebaseStorage mFirebaseStorage;
    int index = 0;
    MessagesProvider mMessageProvider;
    // ============================================================================================

    public ImageProvider()
    {
        // INSTANCIAS EN EL CONSTRUCTOR ===================================
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorage = mFirebaseStorage.getReference();
        mMessageProvider = new MessagesProvider();
       // =================================================================
    }

    public UploadTask save(Context context, File file)
    {
        byte[] imageByte = CompressorBitmapImage.getImage(context, file.getPath(), 500, 500);
        StorageReference storage = mStorage.child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    //METODO PARA ALMACENAR MULTIPLES ARCHIVOS
    public void uploadMultiple(final Context context, ArrayList<Message> messages){
        Uri[] uri = new Uri[messages.size()];
        for (int i = 0; i < messages.size(); i++){
            File file = CompressorBitmapImage.reduceImageSize(new File(messages.get(i).getUrl()));

            uri[i] = Uri.parse("file://" + file.getPath());

            final StorageReference ref = mStorage.child(uri[i].getLastPathSegment());
            // VERIFICAMOS SI YA SE TERMINO DE GUARDAR LA IMAGEN EN FIREBASE
            ref.putFile(uri[i]).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()){
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            // NOS DEVUELVE LA URL ASIGANDA POR FIREBASE A LA IMG
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                messages.get(index).setUrl(url);
                                mMessageProvider.create(messages.get(index));
                                index++;
                            }
                        });

                    }else{
                        Toast.makeText(context, "Hubo un error al almacenar la image!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }



    // Metodo que nos retornara la URL de la imagen que guardaremos
    public Task<Uri> getDownloadUri()
    {
        return  mStorage.getDownloadUrl();
    }


    // METODO QUE NOS PERMITA ELIMINAR UNA IMAGEN A TRAVES DE LA URL
    public Task<Void> delete(String url)
    {
        return  mFirebaseStorage.getReferenceFromUrl(url).delete();
    }
}
