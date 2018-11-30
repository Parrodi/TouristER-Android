/*Actividad encargada de inicializar el mapa y desplegar carrusel con información de cada tour*/
package com.example.carlo.androidapp.actividades;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.adapters.ViewPagerAdapter;
import com.example.carlo.androidapp.modelos.DateInformation;
import com.example.carlo.androidapp.modelos.Place;
import com.example.carlo.androidapp.modelos.Tour;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ViewPager.OnPageChangeListener {

    public static final String TAG = "MapActivity";

    //permisos
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    SharedPreferences prf;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 13f;

    private static String accesstoken;

    int j = 0;

    //vars
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    //Request
    private RequestQueue mRequestQueue;
    String url = "https://er-citytourister.appspot.com/";
    String PATH_TO_TOURS = "tour";
    private List<Tour> listaDeTours;
    private List<Place> listaDePlaces;
    private List<DateInformation> listaDeDates;

    ViewPager viewPager;

    /*Método para evitar que el usuario regresa a LoginActivity*/
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*Inicialización de herramientas*/
        listaDeTours = new ArrayList<>();
        listaDePlaces = new ArrayList<>();
        listaDeDates = new ArrayList<>();
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        accesstoken = prf.getString("token", null);

        /*Llamada al método para conseguir permisos*/
        getLocationPermission();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        /*Llamada al método que hará el request a la base de datos*/
        sendRequest();

        /*Configuración de navegación con la barra inferior*/
        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.botomNavigation);
        menu.setSelectedItemId(R.id.tours);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tours :
                        break;
                    case R.id.tickets :
                        Intent mIntent = new Intent(MapsActivity.this, showPurchaseActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("tour", listaDeTours.get(j));
                        mIntent.putExtras(bundle1);
                        startActivity(mIntent);
                        break;
                    case R.id.mapa :
                        Intent i = new Intent(MapsActivity.this, UserMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tour", listaDeTours.get(j));
                        i.putExtras(bundle);
                        startActivity(i);
                        break;
                    case R.id.salidas :
                        Intent intent = new Intent(MapsActivity.this, TimeIntervaleActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("tour", listaDeTours.get(j));
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        break;
                    case R.id.menus :
                        Intent in = new Intent(MapsActivity.this, OptionsMenuActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("tour", listaDeTours.get(j));
                        in.putExtras(bundle3);
                        startActivity(in);
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
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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

        mapFragment.getMapAsync(MapsActivity.this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

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
            /*Activación de detalles de interfaz, activar ubicación, botón de reubicación*/
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    /*Método para conseguir datos de la base*/
    private void sendRequest() {
        /*Request Queue necesaria para hacer request con Volley*/
        mRequestQueue = Volley.newRequestQueue(this);

        /*Request de arreglos de JSON, se define el url de los tours*/
        JsonArrayRequest requestLocation = new JsonArrayRequest(url + PATH_TO_TOURS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;
                /*Se procesa cada uno de los elementos del arreglo de JSON de tours, guardando toda la información en un
                * arraylist de tours*/
                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        String url = jsonObject.getString("image");

                        /*Se jala otro arreglo de JSON de places por cada tour y se guardan*/
                        JSONArray jsonArrayPlace = jsonObject.getJSONArray("places");
                        for (int j = 0; j < jsonArrayPlace.length(); j++) {
                            JSONObject jsonObjectPlaces = jsonArrayPlace.getJSONObject(j);

                            String name = jsonObjectPlaces.getString("name");
                            String description = jsonObjectPlaces.getString("description");
                            int placeType = jsonObjectPlaces.getInt("place_type_id");
                            double latitude = jsonObjectPlaces.getDouble("latitude");
                            double longitude = jsonObjectPlaces.getDouble("longitude");

                            Place lugar = new Place(name, description, placeType, latitude, longitude);
                            lugar.setId(jsonObjectPlaces.getInt("id"));
                            lugar.setNarrativeUrl(jsonObjectPlaces.getString("narrative_url"));
                            lugar.setImageUrl(jsonObjectPlaces.getString("image_url"));
                            listaDePlaces.add(lugar);

                        }

                        /*Se jala otro arreglo de JSON de date information y se guarda*/
                        JSONArray jsonArrayDateInfo = jsonObject.getJSONArray("dateinformations");
                        for(int k = 0; k < jsonArrayDateInfo.length(); k++){
                            JSONObject jsonObjectDateInfo = jsonArrayDateInfo.getJSONObject(k);

                            int dateId = jsonObjectDateInfo.getInt("date_id");
                            int hourId = jsonObjectDateInfo.getInt("hour_id");
                            int id = jsonObjectDateInfo.getInt("id");
                            DateInformation date = new DateInformation(dateId, hourId);
                            date.setId(id);
                            listaDeDates.add(date);

                        }

                        DateInformation date[] = new DateInformation[listaDeDates.size()];
                        date = listaDeDates.toArray(date);

                        Place lugares[] = new Place[listaDePlaces.size()];
                        lugares = listaDePlaces.toArray(lugares);

                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        listaDeTours.add(new Tour(id, name, new URL(url), description, lugares, date));

                    } catch (JSONException | MalformedURLException e) {

                        Toast.makeText(MapsActivity.this, "Hubo un error de conexión",
                                Toast.LENGTH_LONG).show();

                    }
                }
                setupViewPager(listaDeTours);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this, "Hubo un error de conexión",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth", accesstoken);
                return headers;
            }
        };

        mRequestQueue.add(requestLocation);

        viewPager.addOnPageChangeListener(this);
    }

    /*Método para configurar el ViewPager utilizado apra el carrusel*/
    private void setupViewPager(List<Tour> listaDeTours) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MapsActivity.this, listaDeTours);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
    /*Método para que al cambiar de imagen en el carrusel se cambian los puntos desplegados sobre el mapa*/
    @Override
    public void onPageSelected(int i) {
        mMap.clear();
        Tour tour = listaDeTours.get(i);
        Place place[] = tour.getPlaces();
        j = i;
        for (Place p : place) {
            makeMarker(new LatLng(p.getLatitude(), p.getLongitude()), p.getName(), p.getPlaceTypeId());
        }
        moveCamera(new LatLng(place[0].getLatitude(), place[0].getLongitude()), DEFAULT_ZOOM);
    }

    private void makeMarker(LatLng latLng, String description, int placeTypeId) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(description));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

