package com.example.sakurasketch;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SketchList {

    private static SketchList sketchList;
    private static List<Sketch> sketches;
    private Context context;

    private SketchList(Context context){
        // put this here
        sketches = new ArrayList<Sketch>();

        this.context = context;
        if(filesAreSaved()){
            // load files
            File dir = context.getFilesDir();
            File[] filesToRead = dir.listFiles();
            for (File f : filesToRead) {
                if(f.exists()) {
                    try {
                        FileInputStream input = new FileInputStream(f);
                        ObjectInputStream objInput = new ObjectInputStream(input);
                        Sketch in = (Sketch) objInput.readObject();
                        sketches.add(in);
                        input.close();
                        objInput.close();
                    } catch (Exception e) {
                        Log.e("SketchList", "---- [loading] Failed to open input stream, threw " + e + " ----");
                        // fill with dummy data for now
                        for (int i = 0; i < 5; i++) {
                            Sketch sk = new Sketch();
                            sk.setName("Sketch" + i);
                            sketches.add(sk);
                        }
                    }
                }else{
                    Log.e("SketchList","---- File "+f.getAbsolutePath()+" does not exist. ----");
                }
            }
        } else {
            // fill with dummy data for now
            Sketch sk = new Sketch();
            sk.setName("Sample Sketch");
            sketches.add(sk);
        }
    }

    public List<Sketch> getSketches() {
        return sketches;
    }

    public Sketch getSketch(UUID id) {
        for (Sketch sk : sketches) {
            if (sk.getId().equals(id)) {
                return sk;
            }
        }
        return null;
    }

    public void addSketch(Sketch sk){
        sketches.add(0,sk);
    }

    public static SketchList get(Context context){
        if(sketchList == null) {
            sketchList = new SketchList(context);
        }
        return sketchList;
    }

    public static boolean containsSketch(Sketch sk){
        return sketches.contains(sk);
    }

    public static void removeSketch(Sketch sk){
        if(sketches.contains(sk)) {
            sketches.remove(sk);
        }
    }

    public static int getNumSketches(){
        return sketches.size();
    }

    public void updateWH(int width, int height){
        for(Sketch sk : sketches){
            sk.bitmapInit(width,height);
        }
    }

    private boolean filesAreSaved(){
        File dir = context.getFilesDir();
        File[] filesToRead = dir.listFiles();
        if(filesToRead.length > 0) {
            try {
                // it says "java.io.FileNotFoundException: /data/user/0/com.example.sakurasketch/files (Is a directory)", FIX THAT
                return true;
            } catch (Exception e) {
                Log.e("SketchList", "---- [checking] Failed to open input stream, threw " + e + " ----");
            }
        }
        return false;
    }
}
