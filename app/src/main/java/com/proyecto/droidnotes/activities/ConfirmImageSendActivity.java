package com.proyecto.droidnotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.proyecto.droidnotes.R;
import com.proyecto.droidnotes.adapters.OptionsPagerAdapter;
import com.proyecto.droidnotes.utils.ShadowTransformer;

import java.util.ArrayList;

public class ConfirmImageSendActivity extends AppCompatActivity {

    // VARIABLES GLOBALES =========================================================================
    ViewPager mViewPager;
    ArrayList<String> data;

    // ============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image_send);
        setStatusBarColor();

        mViewPager = findViewById(R.id.viewPager);

        data = getIntent().getStringArrayListExtra("data");

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