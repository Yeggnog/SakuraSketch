package com.example.sakurasketch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SketchListFragment extends Fragment {

    private RecyclerView sketchRecyclerView;
    public static SketchAdapter adapter;
    private SketchList sketchList;
    private static final String TAG = "SketchListFragment";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sketch_list, container, false);
        sketchRecyclerView = (RecyclerView) view.findViewById(R.id.sketch_recycler_view);
        sketchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        TextView noSketch = view.findViewById(R.id.no_sketches);
        if(noSketch != null) {
            if (SketchList.getNumSketches() <= 0) {
                noSketch.setVisibility(View.VISIBLE);
            } else {
                noSketch.findViewById(R.id.no_sketches).setVisibility(View.INVISIBLE);
            }
        }
        /*Toolbar bar = getView().findViewById(R.id.sketch_list_toolbar);
        bar.setLogo(R.drawable.logo);
        getActivity().setActionBar(bar);*/
        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sketch_list, menu);
        Log.d(TAG,"---- got activity "+getActivity()+" ----");
        Log.d(TAG,"---- got action bar "+getActivity().getActionBar()+" ----");
        //getActivity().getActionBar().setLogo(R.drawable.logo);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_sketch:
                Sketch sk = new Sketch();
                sk.setName("Untitled Sketch");
                SketchList.get(getActivity()).addSketch(sk);
                Intent intent = SketchEditActivity.goToEdit(getActivity(), sk.getId());
                SketchInfo.get(sk);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        sketchList = SketchList.get(getActivity());
        List<Sketch> sketches = sketchList.getSketches();
        adapter = new SketchAdapter(sketches);
        sketchRecyclerView.setAdapter(adapter);
    }

    private class SketchHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private Button editButton;
        private Sketch sketch;

        public SketchHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.sketch_name);
            editButton = (Button) itemView.findViewById(R.id.edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // make an intent to go to the drawing thingy
                    Intent intent = SketchEditActivity.goToEdit(getActivity(), sketch.getId());
                    SketchInfo.get(sketch);
                    startActivity(intent);
                }
            });
        }

        public void bindSketch(Sketch sk){
            sketch = sk;
            nameTextView.setText(sketch.getName());
        }
    }

    private class SketchAdapter extends RecyclerView.Adapter<SketchHolder> {

        private List<Sketch> sketches;

        public SketchAdapter(List<Sketch> sketches) {
            this.sketches = sketches;
        }

        public SketchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_sketch, parent, false);
            return new SketchHolder(view);
        }

        public void onBindViewHolder(SketchHolder holder, int pos) {
            Sketch sk = sketches.get(pos);
            holder.bindSketch(sk);
        }

        public int getItemCount() {
            return sketches.size();
        }
    }
}
