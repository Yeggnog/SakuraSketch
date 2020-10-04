package com.example.sakurasketch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorFragment extends Fragment {

    private SeekBar red;
    private TextView red_text;
    private SeekBar green;
    private TextView green_text;
    private SeekBar blue;
    private TextView blue_text;
    private TextView color_swatch;

    private Button color_1;
    private LinearLayout color_1_swatch;
    private Button color_2;
    private LinearLayout color_2_swatch;
    private Button color_3;
    private LinearLayout color_3_swatch;
    private Button color_4;
    private LinearLayout color_4_swatch;
    private Button color_5;
    private LinearLayout color_5_swatch;
    private Button color_6;
    private LinearLayout color_6_swatch;

    private Button okButton;
    private Button cancelButton;
    private Sketch currentSketch;

    private Color[] palette;
    private Color brushColor;
    private int brushColor_r;
    private int brushColor_g;
    private int brushColor_b;

    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        brushColor = SketchInfo.getBrushColor();
        palette = SketchInfo.getPalette();
        brushColor_r = (int)(255*brushColor.red());
        brushColor_g = (int)(255*brushColor.green());
        brushColor_b = (int)(255*brushColor.blue());
    }

    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color, container, false);
        // buttons
        okButton = v.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // passes the info as an extra
                if(currentSketch == null){
                    currentSketch = SketchInfo.getSketch();
                    Log.d("BrushFragment","---- got sketch "+currentSketch.getName()+" ----");
                }
                if(!SketchList.containsSketch(currentSketch)) {
                    currentSketch = SketchList.get(getContext()).getSketches().get(0);
                }
                Intent i = SketchEditActivity.returnFromColor(getActivity(), currentSketch.getId(), brushColor, palette);
                SketchInfo.setBrushColor(brushColor);
                SketchInfo.setPalette(palette);
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
        // palette edit
        color_1 = v.findViewById(R.id.color1_edit_button);
        color_1_swatch = v.findViewById(R.id.color1_swatch);
        color_1_swatch.setBackgroundColor(palette[0].toArgb());
        color_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sets this color in palette
                palette[0] = brushColor;
                color_1_swatch.setBackgroundColor(palette[0].toArgb());
            }
        });
        color_2 = v.findViewById(R.id.color2_edit_button);
        color_2_swatch = v.findViewById(R.id.color2_swatch);
        color_2_swatch.setBackgroundColor(palette[1].toArgb());
        color_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sets this color in palette
                palette[1] = brushColor;
                color_2_swatch.setBackgroundColor(palette[1].toArgb());
            }
        });
        color_3 = v.findViewById(R.id.color3_edit_button);
        color_3_swatch = v.findViewById(R.id.color3_swatch);
        color_3_swatch.setBackgroundColor(palette[2].toArgb());
        color_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sets this color in palette
                palette[2] = brushColor;
                color_3_swatch.setBackgroundColor(palette[2].toArgb());
            }
        });
        color_4 = v.findViewById(R.id.color4_edit_button);
        color_4_swatch = v.findViewById(R.id.color4_swatch);
        color_4_swatch.setBackgroundColor(palette[3].toArgb());
        color_4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sets this color in palette
                palette[3] = brushColor;
                color_4_swatch.setBackgroundColor(palette[3].toArgb());
            }
        });
        color_5 = v.findViewById(R.id.color5_edit_button);
        color_5_swatch = v.findViewById(R.id.color5_swatch);
        color_5_swatch.setBackgroundColor(palette[4].toArgb());
        color_5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sets this color in palette
                palette[4] = brushColor;
                color_5_swatch.setBackgroundColor(palette[4].toArgb());
            }
        });
        color_6 = v.findViewById(R.id.color6_edit_button);
        color_6_swatch = v.findViewById(R.id.color6_swatch);
        color_6_swatch.setBackgroundColor(palette[5].toArgb());
        color_6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sets this color in palette
                palette[5] = brushColor;
                color_6_swatch.setBackgroundColor(palette[5].toArgb());
            }
        });
        // seekbars
        red = v.findViewById(R.id.red_seekbar);
        red.setProgress(brushColor_r);
        red_text = v.findViewById(R.id.red_text);
        color_swatch = v.findViewById(R.id.color_view);
        color_swatch.setBackgroundColor(brushColor.toArgb());
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                if(fromUser) {
                    // updates the color's red
                    red_text.setText(String.valueOf(progress));
                    brushColor_r = progress;
                    updateColor();
                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        green = v.findViewById(R.id.green_seekbar);
        green.setProgress(brushColor_g);
        green_text = v.findViewById(R.id.green_text);
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                // updates the color's green
                if(fromUser) {
                    green_text.setText(String.valueOf(progress));
                    brushColor_g = progress;
                    updateColor();
                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        blue = v.findViewById(R.id.blue_seekbar);
        blue.setProgress(brushColor_b);
        blue_text = v.findViewById(R.id.blue_text);
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                // updates the color's blue
                if(fromUser) {
                    blue_text.setText(String.valueOf(progress));
                    brushColor_b = progress;
                    updateColor();
                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        return v;
    }

    @SuppressLint("NewApi")
    private void updateColor(){
        brushColor = Color.valueOf(Color.rgb(brushColor_r,brushColor_g,brushColor_b));
        color_swatch.setBackgroundColor(brushColor.toArgb());
    }

    public void getColor(Sketch sk, Color brushCol, Color[] pal){
        brushColor = brushCol;
        palette = pal;
        currentSketch = sk;
        Log.d("ColorFragment","---- Set currentSketch ----");
    }
}
