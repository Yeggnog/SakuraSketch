package com.example.sakurasketch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportFragment extends Fragment {

    private Button okButton;
    private Button cancelButton;
    private String filename;
    private EditText filename_entry;
    private TextView filename_text;
    private File fileToExport;
    private Sketch currentSketch;
    private Bitmap mergedBitmap;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        filename = "sample_sketch";
        currentSketch = SketchInfo.getSketch();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_export, container, false);
        // buttons
        okButton = v.findViewById(R.id.export_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // exports the file

                // PUT EXPORT CODE HERE
                if(isExternalStorageWritable()){
                    // merge the bitmaps into one
                    List<Layer> temp = currentSketch.getLayers();
                    List<Bitmap> temmie = new ArrayList<Bitmap>();
                    for(Layer lyr : temp){
                        temmie.add(lyr.getContent());
                    }
                    mergedBitmap = mergeBitmaps(temmie);
                    // try to export
                    testSavingImage(mergedBitmap);
                }

                if(currentSketch == null){
                    currentSketch = SketchInfo.getSketch();
                }
                Intent i = SketchEditActivity.returnFromExport(getActivity(), currentSketch.getId());
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
        filename_text = (TextView) v.findViewById(R.id.file_path_text);
        filename_text.setText((getPublicAlbumStorageDir().getAbsolutePath()+"/"+filename+".jpg"));
        filename_entry = (EditText) v.findViewById(R.id.filename_entry);
        filename_entry.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filename = s.toString();
                filename_text.setText((getPublicAlbumStorageDir().getAbsolutePath()+"/"+filename+".jpg"));
            }
            public void afterTextChanged(Editable s) { }
        });
        return v;
    }

    public void getSketch(Sketch sk){
        currentSketch = sk;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /*public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), albumName);
        if (!file.mkdirs()) {
            Log.e("ExportFragment", "Directory not created");
        }
        return file;
    }*/

    private void testSavingImage(Bitmap toExport){
        // prep the file path
        File targetDir = getPublicAlbumStorageDir();
        targetDir.mkdirs();
        String fname = filename+".jpg";
        fileToExport = new File(targetDir, fname);

        if (fileToExport.exists()) {
            // file already exists, post a dialog to let the user choose
            /*FragmentManager manager = getFragmentManager();
            ExportAuthFragment dialog = new ExportAuthFragment();
            dialog.setTargetFragment(ExportFragment.this,0);
            dialog.show(manager, "");*/
            Toast.makeText(getActivity(), R.string.export_already_exists, Toast.LENGTH_SHORT).show();
        }else{
            saveImageToExternalStorage(mergedBitmap);
        }
    }

    // [!] we just need to grant permission in device settings before we export [!]

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        File targetDir = getPublicAlbumStorageDir();
        targetDir.mkdirs();
        String fname = filename+".jpg";
        File file = new File(targetDir, fname);
        ((ExportActivity)getActivity()).verifyStoragePermissions();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            // toast that on the "crispy" setting
            Toast.makeText(getActivity(), R.string.export_done_toast, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            if(e instanceof FileNotFoundException){
                Log.e("ExportFragment","---- I HATE EVERYTHING ----");
                Toast.makeText(getActivity(), R.string.export_permissions, Toast.LENGTH_LONG).show();
            }else {
                Log.e("ExportFragment", "---- exporting the file threw exception " + e + " ----");
                Toast.makeText(getActivity(), R.string.export_error_toast, Toast.LENGTH_SHORT).show();
            }
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getContext(), new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "---- Scanned " + path + ": ----");
                        Log.i("ExternalStorage", "---- -> uri=" + uri + " ----");
                    }
                });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // delete the file and export
            if (fileToExport.exists()) {
                fileToExport.delete();
            }
            saveImageToExternalStorage(mergedBitmap);
        }else if(resultCode == Activity.RESULT_CANCELED){
            // do nothing
            return;
        }
    }

    public File getPublicAlbumStorageDir() {
        // Get the directory for the user's public download directory.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!file.mkdirs()) {
            Log.e("ExportFragment", "---- Directory not created ----");
        }
        return file;
    }

    private Bitmap mergeBitmaps(List<Bitmap> lst){
        if(lst.size() <= 0){
            return null;
        }
        Bitmap temp = lst.get(0);
        Bitmap ret = Bitmap.createBitmap(temp.getWidth(),temp.getHeight(),temp.getConfig());
        // copy over non-transparent pixels
        for(Bitmap btm : lst){
            for(int x=0; x<btm.getWidth(); x++){
                for(int y=0; y<btm.getHeight(); y++){
                    Color temmie = Color.valueOf(btm.getPixel(x,y));
                    if(temmie.alpha() == 1.0){
                        // copy pixel
                        ret.setPixel(x,y,temmie.toArgb());
                    }
                }
            }
        }
        return ret;
    }
}
