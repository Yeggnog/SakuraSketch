package com.example.sakurasketch;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}