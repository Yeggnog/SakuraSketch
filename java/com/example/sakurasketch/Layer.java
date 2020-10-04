package com.example.sakurasketch;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.UUID;

public class Layer implements Serializable {

    private String name;
    private UUID id;
    private BitmapDataObject content;
    private boolean visible;

    public Layer(String name, int width, int height){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        content = new BitmapDataObject(Bitmap.createBitmap(width, height, conf));
        this.name = name;
        id = UUID.randomUUID();
        visible = true;
    }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Bitmap getContent() { return content.getCurrentImage(); }
    public void setContent(Bitmap cont) { content.setCurrentImage(cont); }
}
