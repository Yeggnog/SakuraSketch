package com.example.sakurasketch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ColorActivity extends SingleFragmentActivity {

    private static Fragment fragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ColorFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    public static Intent goToColor(Context packageContext, Sketch sk, Color brushColor, Color[] palette){
        Intent intent = new Intent(packageContext, ColorActivity.class);
        putColors(sk, brushColor, palette);
        return intent;
    }

    public static void putColors(Sketch sk, Color brushColor, Color[] palette){
        if(fragment != null){
            ((ColorFragment)fragment).getColor(sk,brushColor,palette);
        }
    }

    protected Fragment createFragment() {
        return new ColorFragment();
    }
}
