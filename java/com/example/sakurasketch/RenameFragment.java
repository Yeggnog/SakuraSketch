package com.example.sakurasketch;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RenameFragment extends DialogFragment {

    private static String name;
    private String newName;
    private EditText temmie;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rename, null);
        temmie = v.findViewById(R.id.rename_edit);
        // more crud where we pass in the whole implementation
        temmie.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newName = s.toString();
            }
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Rename Sketch").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendResult(0, newName);
            }
        }).create();
    }

    private void sendResult(int resultCode, String name) {
        if (getTargetFragment() == null) {
            return;
        }
        SketchInfo.getSketch().setName(name);
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static RenameFragment newInstance(String namae){
        name = namae;
        return new RenameFragment();
    }
}
