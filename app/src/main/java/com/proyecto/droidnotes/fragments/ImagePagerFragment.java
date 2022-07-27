package com.proyecto.droidnotes.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.adapters.CardAdapter;

import java.io.File;

public class ImagePagerFragment extends Fragment {

    // VARIABLES GLOBALES ==========================================================================
    View mView;
    CardView mCardViewOptions;
    ImageView mImageViewPicture;
    ImageView mImageViewBack;

    // =============================================================================================
    public static Fragment newInstance(int position, String imagePath) {
        ImagePagerFragment fragment = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("image", imagePath);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //CONFIGURACION
        mView = inflater.inflate(R.layout.fragment_image_pager, container, false);
        mCardViewOptions = mView.findViewById(R.id.cardViewOptions);
        mCardViewOptions.setMaxCardElevation(mCardViewOptions.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        mImageViewPicture = mView.findViewById(R.id.imageViewPicture);
        mImageViewBack = mView.findViewById(R.id.imageViewBack);


        //OBTENER LA INFORMACION DEL METODO NEWINSTANCE
        String imagePath = getArguments().getString("image");
        if (imagePath != null){
            File file = new File(imagePath);
            // TRANFORMAR LA RUTA PARA MOSTRAR LA IMAGEN EN EL FRAGMENT
            mImageViewPicture.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return mView;
    }

    public CardView getCardView(){
        return mCardViewOptions;
    }
}