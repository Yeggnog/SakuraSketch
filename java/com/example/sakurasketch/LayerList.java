package com.example.sakurasketch;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LayerList {

    private static LayerList layerList;
    private static List<Layer> layers;
    private static Sketch sketch;

    private LayerList(Context context, Sketch sk){
        sketch = sk;
        layers = sk.getLayers();
        if(layers == null){
            layers = new ArrayList<Layer>();
        }
    }

    public static LayerList get(Context context, Sketch sk){
        if(layerList == null) {
            layerList = new LayerList(context, sk);
        }
        return layerList;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public Layer getLayer(UUID id) {
        for (Layer lyr : layers) {
            if (lyr.getId().equals(id)) {
                return lyr;
            }
        }
        return null;
    }

    public void addLayer(Layer lyr){
        layers.add(0,lyr);
    }

    public static boolean containsLayer(Layer lyr){
        return layers.contains(lyr);
    }

    public static void removeLayer(Layer lyr){
        if(layers.contains(lyr)) {
            layers.remove(lyr);
        }
    }

    public static int getNumLayers(){
        return layers.size();
    }
}
