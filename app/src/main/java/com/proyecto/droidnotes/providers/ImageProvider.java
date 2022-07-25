package com.proyecto.droidnotes.providers;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proyecto.droidnotes.utils.CompressorBitmapImage;

import java.io.File;
import java.util.Date;

public class ImageProvider {

    // VARIABLE GLOBALES ==========================================================================
    StorageReference mStorage;
    FirebaseStorage mFirebaseStorage;
    // ============================================================================================

    public ImageProvider()
    {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorage = mFirebaseStorage.getReference();

    }

    public UploadTask save(Context context, File file)
    {
        byte[] imageByte = CompressorBitmapImage.getImage(context, file.getPath(), 500, 500);
        StorageReference storage = mStorage.child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;
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
