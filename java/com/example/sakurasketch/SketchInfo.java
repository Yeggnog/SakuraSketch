package com.example.sakurasketch;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.util.UUID;

public class SketchInfo {

    // this is where I put all the crud I want to keep around

    private static Sketch sketch;
    private static UUID sketchId;
    private static Color brushColor;
    private static int brushRadius;
    private static Color[] palette;
    private static Layer layer;
    private static SketchInfo info;

    private SketchInfo(Sketch sk){
        sketch = sk;
        sketchId = sk.getId();
        brushColor = new Color();
        brushRadius = 18;
        palette = new Color[6];
        for(int i=0; i<6; i++){
            palette[i] = new Color();
        }
    }

    public static SketchInfo get(Sketch sk){
        if(info == null) {
            info = new SketchInfo(sk);
        }else{
            setSketch(sk);
        }
        return info;
    }

    public static Sketch getSketch() {
        //Log.d("SketchInfo", "---- getSketch() returned "+sketch+" ----");
        return sketch;
    }

    public static void setSketch(Sketch sk) {
        sketch = sk;
        if(sketch != null) {
            sketchId = sk.getId();
        }
    }

    public static UUID getSketchId() {
        Log.d("SketchInfo", "---- getSketchId() returned "+sketchId+" ----");
        return sketchId;
    }

    public static Color getBrushColor(){
        return brushColor;
    }
    public static void setBrushColor(Color brush){
        brushColor = brush;
    }

    public static int getBrushRadius(){
        return brushRadius;
    }
    public static void setBrushRadius(int brushSize){
        brushRadius = brushSize;
    }

    public static Color[] getPalette(){
        return palette;
    }
    public static void setPalette(Color[] pal){
        palette = pal;
    }

    public static Layer getLayer(){
        return layer;
    }
    public static void setLayer(Layer lyr){
        layer = lyr;
    }
}
