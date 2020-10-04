package com.example.sakurasketch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LayerListActivity extends SingleFragmentActivity {

    private static Fragment fragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new LayerListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    public static Intent goToLayer(Context packageContext, Sketch sk){
        Intent intent = new Intent(packageContext, LayerListActivity.class);
        setSketch(sk);
        return intent;
    }

    private static void setSketch(Sketch sk){
        if(fragment != null) {
            ((LayerListFragment) fragment).setSketch(sk);
        }
    }

    protected Fragment createFragment() {
        return null;
    }
}
