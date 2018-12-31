package com.example.ivan.proyectosdm.CreacionNotas;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ivan.proyectosdm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdjuntos extends Fragment {
    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;
    private LinearLayout layoutFabFoto;
    private LinearLayout layoutFabVideo;
    private LinearLayout layoutFabUbi;


    public FragmentAdjuntos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_adjuntos, container, false);
        View fabFoto = inflater.inflate(R.layout.layout_fab_foto, container, true);
        View fabVideo = inflater.inflate(R.layout.layout_fab_video, container, true);
        View fabUbi = inflater.inflate(R.layout.layout_fab_ubicacion, container, true);
        // Inflate the layout for this fragment
        fabSettings = (FloatingActionButton) v.findViewById(R.id.fabAdjuntos);
        layoutFabFoto = (LinearLayout) fabFoto.findViewById(R.id.layoutFabFoto);
        layoutFabVideo = (LinearLayout) fabVideo.findViewById(R.id.layoutFabVideo);
        layoutFabUbi = (LinearLayout) fabUbi.findViewById(R.id.layoutFabUbi);
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        closeSubMenusFab();
        Toast.makeText(getContext(),"asdasd",Toast.LENGTH_LONG).show();
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getContext(),"qweqwe",Toast.LENGTH_LONG).show();

    }

    private void closeSubMenusFab(){
        layoutFabFoto.setVisibility(View.GONE);
        layoutFabVideo.setVisibility(View.GONE);
        layoutFabUbi.setVisibility(View.GONE);
        fabSettings.setImageResource(R.drawable.ic_add_white_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabFoto.setVisibility(View.VISIBLE);
        layoutFabVideo.setVisibility(View.VISIBLE);
        layoutFabUbi.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        closeSubMenusFab();
    }
}
