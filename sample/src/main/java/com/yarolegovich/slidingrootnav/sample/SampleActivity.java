package com.yarolegovich.slidingrootnav.sample;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.sample.adapters.AdapterServicios;
import com.yarolegovich.slidingrootnav.sample.conexion.Singleton;
import com.yarolegovich.slidingrootnav.sample.entity.ServiceDoctor;
import com.yarolegovich.slidingrootnav.sample.fragment.PagoFragment;
import com.yarolegovich.slidingrootnav.sample.fragment.PerfilFragment;
import com.yarolegovich.slidingrootnav.sample.menu.DrawerAdapter;
import com.yarolegovich.slidingrootnav.sample.menu.DrawerItem;
import com.yarolegovich.slidingrootnav.sample.menu.SimpleItem;
import com.yarolegovich.slidingrootnav.sample.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.sample.tools.GenericAlerts;
import com.yarolegovich.slidingrootnav.sample.tools.YourPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.services.Constants.PRECISION_6;

public class SampleActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final String TAG = "SampleActivity";

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CARD = 3;
    private static final int POS_HISTORY = 4;
    private static final int POS_FAVORITES = 5;
    private static final int POS_LOGOUT = 7;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private DirectionsRoute currentRoute;
    private MapboxDirections client;

    private Button mapUbicacion;

    private SlidingRootNav slidingRootNav;

    private MapboxMap mapBoxGeneral;

    private Position origenFinal;
    private Position destinoFinal;

    FragmentTransaction transaction;
    SupportMapFragment mapFragment;

    private Marker markerViews;

    private RecyclerView recyclerViewServicios;
    GeocoderAutoCompleteView autocomplete;

    GenericAlerts alertas;
    Gson gson;

    YourPreference preferencias;

    ArrayList<ServiceDoctor> listaItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapbox.getInstance(this, getString(R.string.token_app));

        recyclerViewServicios = findViewById(R.id.rcvServicios);
        recyclerViewServicios.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        preferencias = YourPreference.getInstance(this);

        asignarVariablesTemporales();

        if (listaItems.size() > 0 & recyclerViewServicios != null) {
            recyclerViewServicios.setAdapter(new AdapterServicios(SampleActivity.this,listaItems, recyclerViewServicios));
        }
        recyclerViewServicios.setLayoutManager(MyLayoutManager);

        gson = new Gson();
        alertas = new GenericAlerts();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

            // Create fragment
            transaction = getSupportFragmentManager().beginTransaction();

            LatLng lima = new LatLng(-12.04318, -77.02824);

            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.styleUrl(Style.MAPBOX_STREETS);
            options.logoEnabled(false);
            options.camera(new CameraPosition.Builder()
                    .target(lima)
                    .zoom(10)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapbox.map");
            transaction.commit();

            // Set up autocomplete widget
            autocomplete = (GeocoderAutoCompleteView) findViewById(R.id.query);
            mapUbicacion = findViewById(R.id.btnMiUbicacion);
            autocomplete.setAccessToken(Mapbox.getAccessToken());
            autocomplete.setType(GeocodingCriteria.TYPE_POI);
            autocomplete.setCountry("PE");
            autocomplete.setLanguages("ES");
            autocomplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
                @Override
                public void onFeatureClick(CarmenFeature feature) {
                    hideOnScreenKeyboard();
                    Position position = feature.asPosition();
                    destinoFinal = updateMap(position.getLatitude(), position.getLongitude());
                }
            });

            autocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    boolean action = false;
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                    {
                        //hide keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                        busquedaLugar(textView.getText().toString());
                        action = true;
                    }
                    return action;
                }
            });

            mapUbicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRoute(origenFinal,destinoFinal);
                }
            });

        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        origenFinal = Position.fromCoordinates(-77.02776552199401,-12.093380082915322);
        //getRoute(origenFinal,destinoFinal);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Customize map with markers, polylines, etc.
                mapBoxGeneral = mapboxMap;
            }
        });

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CARD),
                createItemFor(POS_HISTORY),
                createItemFor(POS_FAVORITES),
                new SpaceItem(10),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }

    private void hideOnScreenKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                recyclerViewServicios.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Position updateMap(final double latitude, final double longitude) {
        recyclerViewServicios.setVisibility(View.VISIBLE);

        // Animate camera to geocoder result location
        if(markerViews!=null) {
            mapBoxGeneral.removeMarker(markerViews);
        }

        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(16)
                .bearing(50)
                .tilt(30)
                .build();

        markerViews = mapBoxGeneral.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .snippet("Servicio " + "- para:" + "Nombre de Usuario")
                .title(getString(R.string.geocode_activity_marker_options_title)));
        mapBoxGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);

        Position destino = Position.fromCoordinates(longitude,latitude);

        return destino;
    }

    private void getRoute(Position origin, Position destination) {

        client = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setProfile(DirectionsCriteria.PROFILE_CYCLING)
                .setAccessToken(getString(R.string.token_app))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(call.request().url().toString());

                // You can get the generic HTTP info about the response
                //Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    //Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().getRoutes().size() < 1) {
                    //Log.e(TAG, "No se encontraron rutas");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                //Log.d(TAG, "Distancia: " + currentRoute.getDistance());
                Toast.makeText(SampleActivity.this, String.format(getString(R.string.directions_activity_toast_message),
                        currentRoute.getDistance()), Toast.LENGTH_SHORT).show();

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                //Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(SampleActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), PRECISION_6);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        // Draw Points on MapView
        mapBoxGeneral.addPolyline(new PolylineOptions()
                .add(points)
                .color(getColor(R.color.linearColor))
                .width(5));
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finalizarActividad();
        }else if(position == 0){
            mostrarCampos();
            showFragment(mapFragment);
        }else if(position == POS_ACCOUNT){
            ocultarCampos();
            android.support.v4.app.Fragment perfil = new PerfilFragment();
            showFragment(perfil);
        }else if(position == POS_MESSAGES){
            ocultarCampos();
            Fragment perfil = new Fragment();
            showFragment(perfil);
        }else if(position == POS_CARD){
            ocultarCampos();
            android.support.v4.app.Fragment pago = new PagoFragment();
            showFragment(pago);
        }else if(position == POS_HISTORY){
            ocultarCampos();
            Fragment perfil = new Fragment();
            showFragment(perfil);
        }else if(position == POS_FAVORITES){
            ocultarCampos();
            Fragment perfil = new Fragment();
            showFragment(perfil);
        }
        slidingRootNav.closeMenu();
        //Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        //showFragment(selectedScreen);
    }

    private void showFragment(android.support.v4.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).addToBackStack(null)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i <= ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void busquedaLugar(String lugarBuscar) {

        lugarBuscar = lugarBuscar.replace(" ", "+");

        final String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + lugarBuscar;

        JsonObjectRequest respuestaLogin = new JsonObjectRequest(Request.Method.GET,url, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray nuevo = response.getJSONArray("results");
                    JSONObject componentesDireccion = (JSONObject) nuevo.get(0);
                    JSONObject geometria = (JSONObject) componentesDireccion.get("geometry");
                    JSONObject locacion = (JSONObject) geometria.get("location");
                        double latitudBusqueda = Float.parseFloat(locacion.get("lat").toString());
                        double longitudBusqueda = Float.parseFloat(locacion.get("lng").toString());
                    destinoFinal = updateMap(latitudBusqueda,longitudBusqueda);
                    }catch (JSONException e) {
                    e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        Singleton.getInstance(this).addToRequestQueue(respuestaLogin);
    }

    public void ocultarCampos(){
        recyclerViewServicios.setVisibility(View.GONE);
        autocomplete.setVisibility(View.GONE);
    }

    public void mostrarCampos(){
        recyclerViewServicios.setVisibility(View.VISIBLE);
        autocomplete.setVisibility(View.VISIBLE);
    }

    public void asignarVariablesTemporales(){
        ServiceDoctor inyectableIntramuscular = new ServiceDoctor();
            inyectableIntramuscular.setImagen(R.drawable.img_inyectable);
            inyectableIntramuscular.setServicio("Inyectable Intramuscular");
            inyectableIntramuscular.setPrecio(25);
            inyectableIntramuscular.setTiempoEstimado("Aproximadamente 15 minutos atención.");
        ServiceDoctor inyectableIntravenoso = new ServiceDoctor();
            inyectableIntravenoso.setImagen(R.drawable.img_inyectable_intravenoso);
            inyectableIntravenoso.setServicio("Inyectable Intramuscular");
            inyectableIntravenoso.setPrecio(25);
            inyectableIntravenoso.setTiempoEstimado("Aproximadamente 40 minutos atención.");

        ServiceDoctor colocacionSonda = new ServiceDoctor();
            colocacionSonda.setImagen(R.drawable.img_sonda);
            colocacionSonda.setServicio("Colocación de Sonda");
            colocacionSonda.setPrecio(25);
            colocacionSonda.setTiempoEstimado("Aproximadamente 30 minutos atención.");
        listaItems.add(inyectableIntravenoso);
        listaItems.add(inyectableIntramuscular);
        listaItems.add(colocacionSonda);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finalizarActividad();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finalizarActividad(){
        new LovelyStandardDialog(SampleActivity.this)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_enfermera)
                .setTitle("¿Desea cerrar sesión?")
                .setMessage("Se cerrará todas las sesiones iniciadas.")
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferencias.eliminarPreferencias(SampleActivity.this);
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(SampleActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}