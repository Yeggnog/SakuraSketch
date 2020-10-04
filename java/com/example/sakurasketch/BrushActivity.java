package com.example.sakurasketch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BrushActivity extends SingleFragmentActivity {

    private static Fragment fragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new BrushFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    public static Intent goToBrush(Context packageContext, Sketch sk, int brushRadius){
        Intent intent = new Intent(packageContext, BrushActivity.class);
        putBrush(sk, brushRadius);
        return intent;
    }

    public static void putBrush(Sketch sk, int brushRad){
        if(fragment != null){
            ((BrushFragment)fragment).getBrush(sk,brushRad);
        }
    }

    protected Fragment createFragment() {
        return new BrushFragment();
    }
}
