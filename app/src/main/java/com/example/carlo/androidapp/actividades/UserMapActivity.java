/*Actividad encargada de configurar el mapa dinde el usuario puede ver los puntos de interés,
 recomendaciones y paradas*/
package com.example.carlo.androidapp.actividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.modelos.Place;
import com.example.carlo.androidapp.modelos.Tour;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String TAG = "UserMapActivity";

    //permisos
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 13f;

    //vars
    private Boolean mLocationPermissionGranted = true;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Tour tour;
    private boolean camaraMove = false;

    private Button puntosDeInteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);
        getLocationPermission();

        tour = (Tour)getIntent().getExtras().getSerializable("tour");

        /*Inicialización de widgets*/
        puntosDeInteres = (Button)findViewById(R.id.buttonPI);
        final Button parada = (Button)findViewById(R.id.buttonParada);
        final Button recomendacion = (Button) findViewById(R.id.buttonRec);

        /*Configuración de los Listeners de cada botón (Mismo proceso)*/

        puntosDeInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Se limpia el mapa*/
                mMap.clear();

                /*Si es la primera vez que se selecciona el boton, la cámara de centra en el primer punto de interés
                * de la lista*/
                if(!camaraMove){
                    Place places[] = tour.getPlaces();
                    Place p = places[0];
                    moveCamera(new LatLng(p.getLatitude(), p.getLongitude()), DEFAULT_ZOOM);
                    camaraMove = true;
                }

                /*Cambio de color del boton y de los otros dos botones*/
                puntosDeInteres.setBackground(getDrawable(R.drawable.presspuntointeresbtn));
                parada.setBackground(getDrawable(R.drawable.paradasbtn));
                recomendacion.setBackground(getDrawable(R.drawable.recomendacionesbtn));

                /*Colocación de los marcadores*/
                for(Place p : tour.getPlaces()){
                    if(p.getPlaceTypeId() == 1) {
                        LatLng location = new LatLng(p.getLatitude(), p.getLongitude());
                        String name = p.getName();
                        mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinpuntodeinteres)));
                    }
                }

            }
        });

        parada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                parada.setBackground(getDrawable(R.drawable.presparadabtn));
                puntosDeInteres.setBackground(getDrawable(R.drawable.puntodeinteresbtn));
                recomendacion.setBackground(getDrawable(R.drawable.recomendacionesbtn));
                for(Place p : tour.getPlaces()){
                    if(p.getPlaceTypeId() == 3) {
                        LatLng location = new LatLng(p.getLatitude(), p.getLongitude());
                        String name = p.getName();
                        mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinparada)));
                    }
                }
            }
        });

        recomendacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                recomendacion.setBackground(getDrawable(R.drawable.presrecomendacionbtn));
                parada.setBackground(getDrawable(R.drawable.paradasbtn));
                puntosDeInteres.setBackground(getDrawable(R.drawable.puntodeinteresbtn));
                for(Place p : tour.getPlaces()){
                    if(p.getPlaceTypeId() == 2) {
                        LatLng location = new LatLng(p.getLatitude(), p.getLongitude());
                        String name = p.getName();
                        mMap.addMarker(new MarkerOptions().position(location).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinrecomendacion)));
                    }
                }
            }
        });

        /*Configuración de navegación con la barra inferior*/

        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.botomNavigation);
        menu.setSelectedItemId(R.id.mapa);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tours :
                        Intent i = new Intent(UserMapActivity.this, MapsActivity.class);
                        startActivity(i);
                        break;
                    case R.id.tickets :
                        Intent mIntent = new Intent(UserMapActivity.this, showPurchaseActivity.class);
                        Bundle bundl = new Bundle();
                        bundl.putSerializable("tour", tour);
                        mIntent.putExtras(bundl);
                        startActivity(mIntent);
                        break;
                    case R.id.mapa :
                        break;
                    case R.id.salidas :
                        Intent in = new Intent(UserMapActivity.this, TimeIntervaleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tour", tour);
                        in.putExtras(bundle);
                        startActivity(in);
                        break;
                    case R.id.menus :
                        Intent intent = new Intent(UserMapActivity.this, OptionsMenuActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("tour", tour);
                        intent.putExtras(bundle3);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /*Método para inicializar mapa*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*Inicialización de objeto Google Map*/
        mMap = googleMap;

        /*Chequeo de permisos*/
        if (mLocationPermissionGranted) {
            /*Método para obtener ubicación del usuario*/
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            /*Activación de detalles de interfaz, activar ubicación, botón de reubicación y Listener para los marcadores*/
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnMarkerClickListener(this);
        }
    }

    /*Métodos para checar los permisos de coneccióna internet y ubicación de usuario*/
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                /*Si hay permisos, inicializar el mapa*/
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    /*Método para obtener ubicación del usuario*/
    private void getDeviceLocation() {
        /*Uso de FusedLocationProviderClient para la obtención de la ubicación*/
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            /*El mapa se centra en el centro de Puebla*/
                            moveCamera(new LatLng(19.0429, -98.198508), DEFAULT_ZOOM);
                        } else {
                            Toast.makeText(UserMapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }
    /*Método para mover la cámara a una ubicación específica, con un zoom determinado*/
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /*Inicialización asincrona del mapa*/
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(UserMapActivity.this);
    }

    /*Listener de los marcadores, para que muestren información relevante de lugares*/
    @Override
    public boolean onMarkerClick(Marker marker) {

        LatLng markerLocation = marker.getPosition();

        for(Place p : tour.getPlaces()){
            LatLng placeLocation = new LatLng(p.getLatitude(), p.getLongitude());
            if(markerLocation.equals(placeLocation)) {
                /*Intent para sacar el pop up del lugar y redirigir al usuario a PlacePopUpActivity*/
                Intent i = new Intent(UserMapActivity.this, PlacePopActivity.class);
                Bundle bundle = new Bundle();
                Bundle bundle2 = new Bundle();
                bundle.putSerializable("place", p);
                bundle2.putSerializable("tour", tour);
                i.putExtra("place_type", p.getPlaceTypeId());
                i.putExtras(bundle);
                i.putExtras(bundle2);
                startActivity(i);
            }
        }
        return false;
    }
}
