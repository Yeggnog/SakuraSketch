package com.example.sakurasketch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class BrushFragment extends Fragment {

    private SeekBar size;
    private TextView size_text;
    private TextView size_swatch;

    private Button okButton;
    private Button cancelButton;
    private Sketch currentSketch;

    private int brushSize;

    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        brushSize = SketchInfo.getBrushRadius();
    }

    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brush, container, false);
        // buttons
        okButton = v.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // passes the info as an extra
                if(currentSketch == null){
                    currentSketch = SketchInfo.getSketch();
                    //Log.d("BrushFragment","---- got sketch "+currentSketch.getName()+" ----");
                }
                Intent i = SketchEditActivity.returnFromBrush(getActivity(), currentSketch.getId(), brushSize);
                SketchInfo.setBrushRadius(brushSize);
                startActivity(i);
            }
        });
        cancelButton = v.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // no info to pass
                Intent i = new Intent(getActivity(), SketchEditActivity.class);
                startActivity(i);
            }
        });
        // seekbar
        size = v.findViewById(R.id.size_seekbar);
        size_text = v.findViewById(R.id.size_text);
        size_swatch = v.findViewById(R.id.size_view);
        updateSize();
        size.setProgress(brushSize);
        size_text.setText(String.valueOf(brushSize));
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                if(fromUser) {
                    // updates the brush size
                    size_text.setText(String.valueOf(progress));
                    brushSize = progress;
                    updateSize();
                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        return v;
    }

    @SuppressLint("NewApi")
    private void updateSize(){
        int tempsize = (int)(((double)brushSize/150.0)*300);
        size_swatch.setLayoutParams(new LinearLayout.LayoutParams((tempsize*2),(tempsize*2)));
    }

    public void getBrush(Sketch sk, int brushRad){
        brushSize = brushRad;
        currentSketch = sk;
    }
}
