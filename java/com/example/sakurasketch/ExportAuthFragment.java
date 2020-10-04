package com.example.sakurasketch;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class ExportAuthFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_export_auth, null);
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Overwrite file").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // user hit ok
                sendResult(Activity.RESULT_OK);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // user hit cancel
                sendResult(Activity.RESULT_CANCELED);
            }
        }).create();
    }

    public void sendResult(int result){
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), result, intent);
    }
}
