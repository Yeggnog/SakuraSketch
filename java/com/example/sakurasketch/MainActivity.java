package com.example.sakurasketch;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends SingleFragmentActivity {

    //private static final String TAG = "TitleActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new SketchFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        //Log.d(TAG, "---- WE BOOTED INTO THIS AGAIN ----");
    }

    protected void onResume(){
        super.onResume();
    }

    protected Fragment createFragment() {
        return new SketchFragment();
    }
}
