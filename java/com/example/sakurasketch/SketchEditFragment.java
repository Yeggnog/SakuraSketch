package com.example.sakurasketch;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SketchEditFragment extends Fragment {

    private int canvasWidth;
    private int canvasHeight;
    private static Color tempBrush;
    private Color[] tempPal;
    private GridLayout paletteMenu;
    private Button[] palleteButtons;
    private Button paletteButton;
    private int tempRad;
    private String tempSel;
    private static Button colorButton;
    private TextView sizeEdit;
    private TextView layerName;
    private Button eraseButton;
    private boolean erasing;
    private Button layerButton;
    private SketchEditView vw;
    private boolean paletteIsInit;
    //private TextView sketchName;
    private Sketch sketch;
    public static UUID id;
    private static final String TAG = "SketchEditFragment";

    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG,"---- did constuctor ----");
        super.onCreate(savedInstanceState);
        sketch = SketchList.get(getActivity()).getSketch(id);
        palleteButtons = new Button[6];
        tempPal = SketchInfo.getPalette();
        tempBrush = SketchInfo.getBrushColor();
        tempRad = SketchInfo.getBrushRadius();
        erasing = false;
        if(SketchInfo.getLayer() != null) {
            tempSel = SketchInfo.getLayer().getName();
        }
        setHasOptionsMenu(true);
    }

    public static SketchEditFragment newInstance(UUID sketchId, SketchList sketchList){
        Bundle args = new Bundle();
        SketchEditFragment fragment = new SketchEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void updateSelection(String sel){
        tempSel = sel;
    }

    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"---- created view ----");
        View v = inflater.inflate(R.layout.fragment_sketch_editor, container, false);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                vw = (SketchEditView) getView().findViewById(R.id.edit_canvas);
                vw.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // fetch width and height
                canvasWidth = vw.getWidth();
                canvasHeight = vw.getHeight();
                if(sketch == null){
                    sketch = SketchInfo.getSketch();
                }
                if(sketch.getLayers().size() <= 0){
                    sketch.bitmapInit(canvasWidth,canvasHeight);
                }
                vw.setSketch(sketch);
                vw.updateColors(tempBrush, tempPal);
                vw.updateBrush(tempRad);
            }
        });
        paletteMenu = (GridLayout) v.findViewById(R.id.palette_menu);
        // swap colors with palette
        palleteButtons[0] = (Button) v.findViewById(R.id.color1_button);
        palleteButtons[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { tempBrush = tempPal[0]; updateBrush(); vw.setBrushColor(tempPal[0]); paletteMenu.setVisibility(View.INVISIBLE); }
        });
        palleteButtons[1] = (Button) v.findViewById(R.id.color2_button);
        palleteButtons[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { tempBrush = tempPal[1]; updateBrush(); vw.setBrushColor(tempPal[1]); paletteMenu.setVisibility(View.INVISIBLE); }
        });
        palleteButtons[2] = (Button) v.findViewById(R.id.color3_button);
        palleteButtons[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { tempBrush = tempPal[2]; updateBrush(); vw.setBrushColor(tempPal[2]); paletteMenu.setVisibility(View.INVISIBLE); }
        });
        palleteButtons[3] = (Button) v.findViewById(R.id.color4_button);
        palleteButtons[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { tempBrush = tempPal[3]; updateBrush(); vw.setBrushColor(tempPal[3]); paletteMenu.setVisibility(View.INVISIBLE); }
        });
        palleteButtons[4] = (Button) v.findViewById(R.id.color5_button);
        palleteButtons[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { tempBrush = tempPal[4]; updateBrush(); vw.setBrushColor(tempPal[4]); paletteMenu.setVisibility(View.INVISIBLE); }
        });
        palleteButtons[5] = (Button) v.findViewById(R.id.color6_button);
        palleteButtons[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { tempBrush = tempPal[5]; updateBrush(); vw.setBrushColor(tempPal[5]); paletteMenu.setVisibility(View.INVISIBLE); }
        });
        // init colors
        for(int i=0; i<6; i++){
            if(palleteButtons != null && palleteButtons[i] != null){
                palleteButtons[i].setBackgroundColor(tempPal[i].toArgb());
            }
        }

        // bottom bar
        colorButton = (Button) v.findViewById(R.id.color_button);
        //colorButton.setBackgroundColor(tempBrush.toArgb());
        updateBrush();
        colorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to the color picker
                Intent i = ColorActivity.goToColor(getActivity(), sketch, vw.getBrushColor(), vw.getPalette());
                startActivity(i);
            }
        });
        sizeEdit = (TextView) v.findViewById(R.id.size_edit);
        sizeEdit.setText(String.valueOf(tempRad));
        sizeEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to the brush size editor
                Intent i = BrushActivity.goToBrush(getActivity(), sketch, vw.getBrushRadius());
                startActivity(i);
            }
        });
        paletteButton = (Button) v.findViewById(R.id.palette_button);
        paletteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open the palette menu
                paletteMenu.setVisibility(View.VISIBLE);
            }
        });
        eraseButton = (Button) v.findViewById(R.id.erase_button);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // enter / exit erase mode
                if(erasing){
                    erasing = false;
                    eraseButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.erase_icon));
                }else{
                    erasing = true;
                    eraseButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.erase_icon_light));
                }
                vw.updateErasing(erasing);
            }
        });
        layerName = (TextView) v.findViewById(R.id.layer_name_menu);
        if(tempSel != null) {
            layerName.setText(tempSel);
        }
        layerButton = (Button) v.findViewById(R.id.layer_button);
        layerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // go to the layer list
                Intent i = LayerListActivity.goToLayer(getActivity(), sketch);
                startActivity(i);
            }
        });
        //sketchName = (TextView) v.findViewById(R.id.sketch_name);
        //getActivity().findViewById(R.id.color_button).setBackgroundColor(Color.argb(255,0,87,75));
        //sketchName.setText(sketch.getName());
        return v;
    }

    @SuppressLint("NewApi")
    public void updateColors(Color brushColor, Color[] palette){
        Log.d(TAG,"---- updated colors ----");
        tempBrush = brushColor;
        Log.d(TAG,"---- set palette to: ----");
        for(Color col : palette){
            Log.d(TAG,"---- "+col+" ----");
        }
        tempPal = palette;
        for(int i=0; i<6; i++){
            if(palleteButtons[i] != null){
                palleteButtons[i].setBackgroundColor(tempPal[i].toArgb());
                Log.d(TAG,"---- set BG color ----");
            }
        }
        paletteIsInit = true;
        getView().invalidate();
    }

    public void updateBrush(int brushRad){
        tempRad = brushRad;
        if(vw.findViewById(R.id.size_edit) != null) {
            ((TextView) vw.findViewById(R.id.size_edit)).setText(brushRad);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            return;
        }
        if (requestCode == 0) {
            if(getActivity() != null && ((SketchEditActivity)getActivity()).getSupportActionBar() != null){
                ((SketchEditActivity)getActivity()).getSupportActionBar().setTitle(sketch.getName());
            }
        }
    }

    @SuppressLint("NewApi")
    public static void updateBrush(){
        GradientDrawable temp = (GradientDrawable) colorButton.getBackground();
        temp.setColor(tempBrush.toArgb());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sketch_edit, menu);
        //getActivity().getActionBar().setTitle(sketch.getName());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                // save the sketch
                saveFile();
                return true;
            case R.id.menu_item_exit:
                // exit the editor
                Intent i = new Intent(getActivity(), SketchListActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_item_rename:
                // rename the sketch
                FragmentManager manager = getFragmentManager();
                RenameFragment dialog = new RenameFragment();
                dialog.setTargetFragment(SketchEditFragment.this,0);
                dialog.show(manager, "");
                return true;
            case R.id.menu_item_delete:
                // delete the sketch
                SketchList.removeSketch(sketch);
                SketchInfo.setSketch(null);
                Intent in = new Intent(getActivity(), SketchListActivity.class);
                startActivity(in);
                return true;
            case R.id.menu_item_export:
                // export the sketch
                Intent intent = ExportActivity.goToExport(getActivity(), sketch);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean saveFile(){
        File dir = getContext().getFilesDir();
        String adaptedSketchName = sketch.getName().replace(' ','_');
        if(!dir.exists()){
            // create the directory
            Log.e(TAG,"---- Directory "+dir.getAbsolutePath()+" does not exist. ----");
            return false;
        }
        // deal with SDK versions
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, adaptedSketchName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + dir.getAbsolutePath());
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try{
                fos = resolver.openOutputStream(imageUri);
            }catch(Exception e){
                Log.e(TAG,"---- Failed to open OutputStream, threw "+e+" ----");
            }
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).toString() + File.separator + dir.getAbsolutePath();

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, adaptedSketchName + ".png");
            try{
                fos = new FileOutputStream(image);
            }catch(Exception e){
                Log.e(TAG,"---- Failed to open OutputStream, threw "+e+" ----");
            }
        }
        // save file
        File file = new File(dir,(adaptedSketchName+"_"+sketch.getId().toString()));
        if(file.exists()){
            file.delete();
        }
        if(fos != null) {
            ObjectOutputStream objOut = null;
            try{
                objOut = new ObjectOutputStream(fos);
            }catch(Exception e){
                Log.e(TAG,"---- Failed to open ObjectOutputStream, threw "+e+" ----");
            }
            if(objOut != null) {
                try {
                    objOut.writeObject(sketch);
                } catch (Exception e) {
                    Log.e(TAG, "---- Failed to write sketch, threw " + e + " ----");
                }
                Log.d(TAG, "---- Saved file " + (adaptedSketchName + "_" + sketch.getId().toString()) + " to " + dir + " ----");
                try {
                    objOut.close();
                } catch (Exception e) {
                    Log.e(TAG, "---- Failed to close ObjectOutputStream, threw " + e + " ----");
                }
            }
            try{
                fos.close();
            }catch(Exception e){
                Log.e(TAG,"---- Failed to close OutputStream, threw "+e+" ----");
            }
        }
        /*
        try {
            FileOutputStream output = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(output);
            objOut.writeObject(sketch);
            Log.d(TAG,"---- Saved file "+(adaptedSketchName+"_"+sketch.getId().toString())+" to "+dir+" ----");
            objOut.close();
            output.close();
        }catch(Exception e){
            Log.e(TAG,"---- Failed to open output stream, threw "+e+" ----");
        }
        */
        return true;
    }
}
