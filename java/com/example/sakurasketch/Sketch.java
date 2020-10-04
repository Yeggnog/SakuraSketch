package com.example.sakurasketch;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Sketch implements Serializable {

    private UUID id;
    private String name;
    private ArrayList<Layer> layers;
    private int layerNameIndex;
    private int defaultWidth;
    private int defaultHeight;
    private static final String TAG = "Sketch";

    public Sketch(){
        // generate id
        id = UUID.randomUUID();
        layers = new ArrayList<Layer>();
        layerNameIndex = 0;
        /*
        // Need to get width and height of the current thing
        int w = 9;
        int h = 9;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        sketch = Bitmap.createBitmap(w, h, conf);
        */
    }

    public String getName() {
        return name;
    }

    public Layer getLayer(int index) {
        if(index < 0 || index >= layers.size()){
            index = 0;
        }
        return layers.get(index);
    }

    public ArrayList<Layer> getLayers(){ return layers; }

    public void addLayer(int pos, int width, int height){
        if(pos < layers.size()) {
            if(width == -1){
                width = defaultWidth;
            }
            if(height == -1){
                height = defaultHeight;
            }
            Layer temmie = new Layer("Layer" + layerNameIndex, width, height);
            layerNameIndex += 1;
            layers.add(pos, temmie);
            Log.d(TAG, "---- added layer " + temmie + " ----");
        }
    }

    public void removeLayer(Layer rem){
        layers.remove(rem);
    }

    public void setName(String name){
        this.name = name;
    }

    public UUID getId(){
        return id;
    }

    public void bitmapInit(int width, int height){
        String temp = "---- Made layers ";
        if(defaultWidth == 0){
            defaultWidth = width;
        }
        if(defaultHeight == 0){
            defaultHeight = height;
        }
        // add background layer
        Layer tem = new Layer("Background",width,height);
        Bitmap ret = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                ret.setPixel(x,y, Color.WHITE);
            }
        }
        tem.setContent(ret);
        layers.add(tem);
        temp += tem+", ";
        // add empty layers
        for(int i=0; i<3; i++){
            Layer temmie = new Layer("Layer"+layerNameIndex,width,height);
            layerNameIndex += 1;
            layers.add(temmie);
            temp += temmie;
            if(i < 2) {
                temp += ", ";
            }
        }
        temp += " ----";
        Log.d(TAG, temp);
    }
}
