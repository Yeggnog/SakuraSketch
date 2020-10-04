package com.example.sakurasketch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class SketchEditView extends View {

    private Paint BGPaint;
    private Paint brushPaint;
    private Paint cursorPaint;
    public int brushColor;
    private int eraseColor;
    private Color[] palette;
    private int brushRadius;
    private Rect layerTarget;
    private Sketch sketch;
    private ArrayList<Layer> layers;
    private static int layer;
    private boolean erasing;

    private boolean setupComplete = false;
    private static final String TAG = "SketchEditView";

    private boolean drawing;
    private int cursor_x;
    private int cursor_y;

    // code
    public SketchEditView(Context context) {
        this(context, null);
    }
    // XML
    public SketchEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Log.d(TAG,"---- Did constructor ----");
        BGPaint = new Paint();
        //BGPaint.setColor(0xfff8efe0);
        BGPaint.setColor(Color.WHITE);
        brushRadius = 18;
        brushColor = Color.argb(255,46,118,215);
        brushPaint = new Paint();
        brushPaint.setStyle(Paint.Style.FILL);
        brushPaint.setColor(brushColor);
        eraseColor = Color.argb(0,0,0,0);
        cursorPaint = new Paint();
        cursorPaint.setColor(Color.BLACK);
        cursorPaint.setStyle(Paint.Style.STROKE);
        cursorPaint.setStrokeWidth(5);
        drawing = false;
        erasing = false;
        layerTarget = new Rect();
    }

    @SuppressLint("NewApi")
    public void updateColors(Color brushCol, Color[] pal){
        if(brushCol == null){
            brushCol = SketchInfo.getBrushColor();
        }
        if(pal == null){
            pal = SketchInfo.getPalette();
        }
        //Log.d(TAG,"---- AND here ----");
        //Log.d(TAG,"---- putting in ARGB color ("+brushCol.alpha()+", "+(255*brushCol.red())+", "+(255*brushCol.green())+", "+(255*brushCol.blue())+") ----");
        brushColor = brushCol.toArgb();
        brushPaint.setColor(brushCol.toArgb());
        if(findViewById(R.id.color_button) != null) {
            findViewById(R.id.color_button).setBackgroundColor(brushColor);
        }
        palette = pal;
    }

    public static void setLayer(int selected){
        layer = selected;
        //Log.d(TAG,"---- set current layer index to "+layer+" ----");
    }

    public static boolean isLayerSelected(Layer lyr){
        return (SketchInfo.getSketch().getLayers().indexOf(lyr) == layer);
    }

    public void updateBrush(int brushRad){
        if(brushRad <= 0){
            brushRad = SketchInfo.getBrushRadius();
        }
        brushRadius = brushRad;
    }

    public void updateErasing(boolean era){
        erasing = era;
    }

    @SuppressLint("NewApi")
    public Color getBrushColor(){
        return Color.valueOf(brushColor);
    }

    @SuppressLint("NewApi")
    public void setBrushColor(Color brush){
        if(brush.alpha() < 1.0){
            Color temmie = Color.valueOf(brush.red(),brush.green(),brush.blue(),(float)1.0);
            brush = temmie;
        }
        brushColor = brush.toArgb();
    }

    public int getBrushRadius(){
        return brushRadius;
    }

    @SuppressLint("NewApi")
    public Color[] getPalette(){
        return palette;
    }

    protected void onDraw(Canvas canvas) {
        if(setupComplete) {
            // Fill the background
            canvas.drawPaint(BGPaint);
            // draw the layers
            for(Layer lyr : layers) {
                //canvas.drawBitmap(currentLayer().getContent(), null, layerTarget, brushPaint);
                if(lyr.isVisible()) {
                    canvas.drawBitmap(lyr.getContent(), null, layerTarget, brushPaint);
                }
            }
            // draw the cursor
            if (drawing && (cursor_x >= 0 && cursor_x < canvas.getWidth()) && (cursor_y >= 0 && cursor_y < canvas.getHeight())) {
                canvas.drawCircle(cursor_x, cursor_y, brushRadius + 40, cursorPaint);
            }
        }else{
            finishSetup();
            this.invalidate();
        }
    }

    private void finishSetup(){
        int[] l = new int[2];
        getLocationOnScreen(l);
        layerTarget.set(l[0],0,l[0]+this.getWidth(),this.getHeight());
        setupComplete = true;
    }

    public void setSketch(Sketch sk){
        sketch = sk;
        layers = sk.getLayers();
    }

    @SuppressLint("NewApi")
    public boolean onTouchEvent(MotionEvent event) {
        PointF tap = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                drawing = true;
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                // draw to the bitmap
                cursor_x = (int) tap.x;
                cursor_y = (int) tap.y;
                Bitmap lyr = currentLayer().getContent();
                //Log.d(TAG,"---- drawing to layer "+layer+" ----");
                if (lyr != null) {
                    //int[] l = new int[2];
                    //getLocationOnScreen(l);
                    float adaptedX = (tap.x / this.getWidth() * (lyr.getWidth()));
                    float adaptedY = (tap.y / this.getHeight() * (lyr.getHeight()));
                    for (Double theta = 0.0; theta < 360.0; theta++) {
                        for (int r = 0; r < brushRadius; r++) {
                            int drawX = (int) (adaptedX + (r * Math.cos(theta)));
                            int drawY = (int) (adaptedY + (r * Math.sin(theta)));
                            if ((drawX >= 0 && drawX < lyr.getWidth()) && (drawY >= 0 && drawY < lyr.getHeight())) {
                                if(!erasing) {
                                    lyr.setPixel(drawX, drawY, brushColor);
                                }else{
                                    lyr.setPixel(drawX, drawY, eraseColor);
                                }
                            }
                        }
                    }
                    //Log.d(TAG,"---- drawing with ARGB color ("+brushCol.alpha()+", "+(255*brushCol.red())+", "+(255*brushCol.green())+", "+(255*brushCol.blue())+") ----");
                    // update the display with the current bitmap
                    this.invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                drawing = false;
                this.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
        }
        return true;
    }

    private Layer currentLayer(){
        return layers.get(layer);
    }
}
