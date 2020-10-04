package com.example.sakurasketch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;

import java.util.UUID;

public class SketchEditActivity extends SingleFragmentActivity {

    private static final String TAG = "SketchEditActivity";
    private static final String EXTRA_SKETCH_ID = "com.example.sakuraSketch.sketch_id";
    private static final String EXTRA_SKETCH_LIST = "com.example.sakuraSketch.sketch_list";
    private static Fragment fragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            UUID sketchId = (UUID) getIntent().getSerializableExtra(EXTRA_SKETCH_ID);
            //SketchList list = (SketchList) getIntent().getSerializableExtra(EXTRA_SKETCH_LIST);
            fragment = new SketchEditFragment();
            SketchEditFragment.id = sketchId;
            Sketch sketch = SketchList.get(this).getSketch(sketchId);
            if(sketch == null){
                sketch = SketchInfo.getSketch();
            }
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            // put all the actionBar setup here
            getSupportActionBar().setTitle(sketch.getName());
            getSupportActionBar().setLogo(R.drawable.logo);
        }
    }

    public static void updateColors(Color brushColor, Color[] palette){
        if(fragment != null){
            Log.d(TAG,"---- We got here ----");
            ((SketchEditFragment)fragment).updateColors(brushColor,palette);
        }
    }

    public static void updateBrush(int brushRad){
        if(fragment != null){
            ((SketchEditFragment)fragment).updateBrush(brushRad);
        }
    }

    protected Fragment createFragment() {
        Log.d(TAG, "---- DID WE EVEN GET HERE AT ALL ----");
        UUID sketchId = (UUID) getIntent().getSerializableExtra(EXTRA_SKETCH_ID);
        SketchList list = (SketchList) getIntent().getSerializableExtra(EXTRA_SKETCH_LIST);
        return SketchEditFragment.newInstance(sketchId, list);
    }

    public static Intent goToEdit(Context packageContext, UUID sketchId) {
        Intent intent = new Intent(packageContext, SketchEditActivity.class);
        intent.putExtra(EXTRA_SKETCH_ID, sketchId);
        //intent.putExtra(EXTRA_SKETCH_LIST, list);
        return intent;
    }

    public static Intent returnFromColor(Context packageContext, UUID sketchId, Color brushColor, Color[] palette){
        Intent intent = new Intent(packageContext, SketchEditActivity.class);
        intent.putExtra(EXTRA_SKETCH_ID, sketchId);
        updateColors(brushColor, palette);
        return intent;
    }

    public static Intent returnFromBrush(Context packageContext, UUID sketchId, int brushRad){
        Intent intent = new Intent(packageContext, SketchEditActivity.class);
        intent.putExtra(EXTRA_SKETCH_ID, sketchId);
        updateBrush(brushRad);
        return intent;
    }

    public static Intent returnFromLayer(Context packageContext, UUID sketchId, String layerName){
        Intent intent = new Intent(packageContext, SketchEditActivity.class);
        intent.putExtra(EXTRA_SKETCH_ID, sketchId);
        ((SketchEditFragment)fragment).updateSelection(layerName);
        return intent;
    }

    public static Intent returnFromExport(Context packageContext, UUID sketchId){
        Intent intent = new Intent(packageContext, SketchEditActivity.class);
        intent.putExtra(EXTRA_SKETCH_ID, sketchId);
        return intent;
    }
}
