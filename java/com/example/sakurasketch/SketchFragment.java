package com.example.sakurasketch;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SketchFragment extends Fragment {

    private Button beginButton;
    //private static final String TAG = "SketchFragment";
    //private EditText nameField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sketch, container, false);
        beginButton = (Button)v.findViewById(R.id.begin_button);
        //nameField = (EditText)v.findViewById(R.id.sketch_name);

        beginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Sends an intent to go to the list
                Intent i = new Intent(getActivity(), SketchListActivity.class);
                startActivity(i);
            }
        });

        // more crud where we pass in the whole implementation
        /*nameField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                sketch.setName(s.toString());
            }
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });*/

        return v;
    }
}
