package com.example.sakurasketch;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class LayerListFragment extends Fragment {

    public static LayerAdapter adapter;
    protected Sketch sketch;
    private LayerList layerList;
    private Button okButton;
    private Button newButton;
    private TextView current_layer;
    protected int selected;
    private RecyclerView layerRecyclerView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layer_list, container, false);
        // list
        if (sketch == null) {
            sketch = SketchInfo.getSketch();
            Log.d("LayerListFragment","---- sketch was null, changed to "+sketch+" ----");
        }
        if (!SketchList.containsSketch(sketch)) {
            sketch = SketchList.get(getContext()).getSketches().get(0);
        }
        layerRecyclerView = (RecyclerView) v.findViewById(R.id.layer_recycler_view);
        layerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        current_layer = (TextView) v.findViewById(R.id.current_layer_text);
        current_layer.setText(("Editing layer "+sketch.getLayers().get(selected).getName()));
        updateUI();
        // buttons
        okButton = v.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // passes the info as an extra
                if (sketch == null) {
                    sketch = SketchInfo.getSketch();
                }
                if (!SketchList.containsSketch(sketch)) {
                    sketch = SketchList.get(getContext()).getSketches().get(0);
                }
                Log.d("LayerListFragment","---- getting selected layer: "+selected+" ----");
                Intent i = SketchEditActivity.returnFromLayer(getActivity(), sketch.getId(), sketch.getLayer(selected).getName());
                SketchInfo.setLayer(sketch.getLayer(selected));
                startActivity(i);
            }
        });
        newButton = v.findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // new layer
                sketch.addLayer(1,-1,-1);
                updateUI();
            }
        });
        return v;
    }

    private void updateUI() {
        sketch = SketchInfo.getSketch();
        /*List<Layer> skList = sketch.getLayers();
        Log.d("LayerListFragment", "---- Input these layers: ----");
        for(Layer lyr : skList){
            Log.d("LayerListFragment", "---- "+lyr+" ----");
        }*/
        layerList = LayerList.get(getActivity(), sketch);
        List<Layer> layers = layerList.getLayers();
        adapter = new LayerAdapter(layers);
        layerRecyclerView.setAdapter(adapter);
    }

    public void setSketch(Sketch sk){ sketch = sk; }

    private class LayerHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private Button visButton;
        private Button editButton;
        private Button deleteButton;
        private Layer layer;

        public LayerHolder(final View itemView) {
            super(itemView);
            //Log.d("LayerHolder","---- made layerHolder ----");
            nameTextView = (TextView) itemView.findViewById(R.id.layer_name);
            /*if(SketchEditView.isLayerSelected(layer)){
                nameTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                Log.d("LayerHolder","---- set BG color ----");
            }*/
            editButton = (Button) itemView.findViewById(R.id.edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // toggle which layer we're editing
                    selected = SketchInfo.getSketch().getLayers().indexOf(layer);
                    SketchEditView.setLayer(selected);
                    current_layer.setText("Editing layer "+layer.getName());
                    getView().invalidate();
                }
            });
            visButton = (Button) itemView.findViewById(R.id.visibility_button);
            visButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // toggle the visibility
                    if(layer.isVisible()){
                        layer.setVisible(false);
                        itemView.findViewById(R.id.visibility_button).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.invisible_icon));
                    }else{
                        layer.setVisible(true);
                        itemView.findViewById(R.id.visibility_button).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.visible_icon));
                    }
                }
            });
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // delete this layer
                    adapter.Layers.remove(layer);
                    sketch.removeLayer(layer);
                    updateUI();
                }
            });
        }

        public void bindLayer(Layer lyr){
            layer = lyr;
            if(!layer.isVisible()) {
                visButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.invisible_icon));
            }else{
                visButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.visible_icon));
            }
            nameTextView.setText(layer.getName());
        }
    }

    private class LayerAdapter extends RecyclerView.Adapter<LayerListFragment.LayerHolder> {

        private List<Layer> Layers;

        public LayerAdapter(List<Layer> Layers) { this.Layers = Layers; }

        public LayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_layer, parent, false);
            return new LayerHolder(view);
        }

        public void onBindViewHolder(LayerListFragment.LayerHolder holder, int pos) {
            Layer sk = Layers.get(pos);
            holder.bindLayer(sk);
        }

        public int getItemCount() { return Layers.size(); }
    }

}
