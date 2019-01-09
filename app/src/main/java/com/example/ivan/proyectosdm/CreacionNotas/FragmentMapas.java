package com.example.ivan.proyectosdm.CreacionNotas;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ivan.proyectosdm.MainActivity;
import com.example.ivan.proyectosdm.Notas.Nota;
import com.example.ivan.proyectosdm.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMapas extends Fragment implements OnMapReadyCallback {

    private GoogleMap gmap;
    private MapView mapView;
    private View view;
    private boolean permisos;
    private Marker posUsuario;
    private Nota nota;
    private String coordenada = "";
    private Context context;
    private EditText editText;
    private FloatingActionButton floatingActionButton;

    public FragmentMapas() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            nota = (Nota) getArguments().getSerializable(MainActivity.OBJETO_NOTA);
            coordenada = nota.getCoordenadas();
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.fragment_mapas, container, false);
        if(nota == null){
            nota = new Nota("asd","asd",0);
        }
        editText =(EditText) view.findViewById(R.id.editText3);
        floatingActionButton =(FloatingActionButton) view.findViewById(R.id.floatingActionButton3);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Busqueda(editText.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) this.view.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context);
        gmap = googleMap;
        gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (validaPermisos()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Aviso: Permisos Desactivados");
                dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
                dialog.create().show();
                return;
            }
            gmap.setMyLocationEnabled(true);
            gmap.getUiSettings().setMyLocationButtonEnabled(true);
            LatLng centro = new LatLng(40, -3);
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro,5.5f));
            gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    CargarMarker(latLng);
                }
            });
            if(!coordenada.equals("")){
                String[] aux = coordenada.split(",");
                double latitud = Double.parseDouble(aux[0]);
                double longitud = Double.parseDouble(aux[1]);
                LatLng latLng = new LatLng(latitud,longitud);
                CargarMarker(latLng);
            }


        }

    }

    private void Busqueda(String busqueda){
        if (busqueda != null && busqueda.trim().length() != 0) {
            gmap.clear();
            Geocoder geocoder = new Geocoder(context);
            List<Address> posiblesDirecciones = null;
            MarkerOptions markerOptions = new MarkerOptions();
            try {
                posiblesDirecciones = geocoder.getFromLocationName(busqueda.toUpperCase(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < posiblesDirecciones.size(); i++) {
                Address posiblesDireccione = posiblesDirecciones.get(i);
                LatLng latLng = new LatLng(posiblesDireccione.getLatitude(), posiblesDireccione.getLongitude());
                CargarMarker(latLng);
            }
        }
    }

    private void CargarMarker(LatLng latLng){
        coordenada = latLng.latitude + "," + latLng.longitude;
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1);
        } catch (IOException ioException) {
            Log.e("ERROR", "IO", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("ERROR", "ILEGALARGUMENT", illegalArgumentException);
        }
        Address address = addresses.get(0);
        ArrayList<String> addressFragments = new ArrayList<String>();
        if (addresses == null || addresses.size()  == 0) {
            Toast.makeText(context,"No se han encontrado resultados.",Toast.LENGTH_LONG).show();
        } else {

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
        }
        MarkerOptions marcadorOpciones = new MarkerOptions().position(latLng).title(addressFragments.get(0));
        if(posUsuario != null){
            posUsuario.remove();
        }
        posUsuario = gmap.addMarker(marcadorOpciones);
        posUsuario.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14)
                .build();
        CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
        gmap.animateCamera(cu);
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                permisos = true;
            }else{
                permisos = false;
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Aviso: Permisos Desactivados");
                dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
                dialog.create().show();
            }
        }
    }


    public String getCoordenada(){
        return coordenada;
    }

}
