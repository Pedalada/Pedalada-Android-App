package pedalada.pedalapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    // declare location objects
    private LocationManager locationManager; // accesses location services

    // declare views
    TextView mSpeed = null;
    TextView mDistance = null;
    TextView mTime = null;
    TextView mProvider = null;
    TextView mPrecision = null;
    TextView mLatitude = null;
    TextView mLongitude = null;
    static TextView serverTV = null;

    // some cool graphics to handled later
    Drawable dStop = null;
    Drawable dBike = null;

    // the buttons
    FloatingActionButton fab = null;
    Button bStop = null;

    // declare ESRI objects
    public MapView mapView = null;
    public LocationDisplayManager ldm = null;
    GraphicsLayer graphicsLayer = null;

    // the record assets
    static public Double[] currentCoordinates = new Double[2]; // Double[0] <- Longitude
    static ArrayList<Double[]> record = new ArrayList<>();
    static long startTime;
    static long finishTime;
    static boolean firstUpdate = true;

    // helper for JSON
    String riderUsername = "yourMom";
    Ride ride;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        handleLocationPermissions();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();
        configureButtons();
        configureMap();
        ride = new Ride();
    }

    // to save battery or not to save battery, that is the question.
    @Override protected void onPause() {super.onPause(); mapView.pause();}
    @Override protected void onResume() {super.onResume(); mapView.unpause();}

    public void configureMap() {
        // to add tracking points
        graphicsLayer = new GraphicsLayer();
        mapView.addLayer(graphicsLayer);
        // http://stackoverflow.com/questions/30251063/why-is-projection-not-working-using-my-mapviews-spatial-reference-arcgis-sdk

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            // https://gist.github.com/phpmaps/5d766fe34b032fd6aaa5
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if ((status == STATUS.INITIALIZED) && (o instanceof MapView )) {
                    Log.d("StatusChangedListener","Map initialization succeeded");
                    ldm = mapView.getLocationDisplayManager();
                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    ldm.setShowLocation(true);
                    ldm.setLocationListener(new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if(location != null) {
                                if (firstUpdate) {
                                    ldm.setShowLocation(true);
                                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                                    // Log.d("LocationLISTENER", "lat: " + location.getLatitude() + " long: " + location.getLongitude());
                                    // Log.d("LocationLISTENER", "Location found....");
                                    updateUI(location);
                                    updateRecord(location);
                                    addTrackingDot(location);
                                    startTime = location.getTime();
                                    firstUpdate = false;
                                } else {
                                    ldm.setShowLocation(true);
                                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                                    // Log.d("LocationLISTENER", "lat: " + location.getLatitude() + " long: " + location.getLongitude());
                                    // Log.d("LocationLISTENER", "Location found....");
                                    updateUI(location);
                                    updateRecord(location);
                                    addTrackingDot(location);
                                    finishTime = location.getTime();
                                }
                                
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.d("LocationLISTENER", "onStatusChanged");
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.d("LocationLISTENER", "LOCATION PROVIDER ENABLED");
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Log.d("LocationLISTENER", "LOCATION PROVIDER DISABLED");
                            // ldm.setShowLocation(false);
                        }
                    });
                    ldm.start();
                }
            }
        });
    }

    public void addTrackingDot(Location location) {

        float speed = location.getSpeed();
        Point geometricPoint = (Point) GeometryEngine.project(new Point(location.getLongitude(), location.getLatitude()), SpatialReference.create(4326), mapView.getSpatialReference());

        if (speed == 0) {
            SimpleMarkerSymbol stoppedSymbol = new SimpleMarkerSymbol(Color.RED, 5, SimpleMarkerSymbol.STYLE.CIRCLE);
            Graphic pointGraphic = new Graphic(geometricPoint, stoppedSymbol);
            graphicsLayer.addGraphic(pointGraphic);
        } else {
            SimpleMarkerSymbol movingSymbol = new SimpleMarkerSymbol(Color.GREEN, 5, SimpleMarkerSymbol.STYLE.DIAMOND);
            Graphic pointGraphic = new Graphic(geometricPoint, movingSymbol);
            graphicsLayer.addGraphic(pointGraphic);
        }

    }

    public void updateRecord(Location location) {
        currentCoordinates[0] = location.getLongitude();
        currentCoordinates[1] = location.getLatitude();
        record.add(currentCoordinates);
    }

    public void initializeViews() {
        // initialize the buttons
        fab = (FloatingActionButton) findViewById(R.id.fab);
        bStop = (Button) findViewById(R.id.stopbutton);

        // initialize TextViews
        mSpeed = (TextView) findViewById(R.id.velocidade);
        mDistance = (TextView) findViewById(R.id.distancia);
        mTime = (TextView) findViewById(R.id.tempo);
        mProvider = (TextView) findViewById(R.id.fornecedor);
        mPrecision = (TextView) findViewById(R.id.precisao);
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);

        // initialize map
        mapView = (MapView) findViewById(R.id.map);

        // server text view
        serverTV = (TextView) findViewById(R.id.serverResponseTextView);
    }

    public void updateUI(Location location) {
        String longitudeStr = "Longitude: " + location.convert(location.getLongitude(), location.FORMAT_SECONDS);
        String latitudeStr = "Latitude: " + location.convert(location.getLatitude(), location.FORMAT_SECONDS);
        String precisionStr = String.format("Precisão: %.2f", location.getAccuracy());
        String providerStr = "Fornecedor: " + location.getProvider();
        String speedStr = String.format("Velocidade: %.1fkm/h", location.getSpeed() * 3.6);

        mLatitude.setText(latitudeStr);
        mLongitude.setText(longitudeStr);
        mSpeed.setText(speedStr);
        mProvider.setText(providerStr);
        mPrecision.setText(precisionStr);
    }

    public void handleLocationPermissions() {
        // permission handling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // request permissions
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                // handle permission results by calling back to onRequestPermissionsResults()
                return;
            }
        }
        // permission handling in android goes like
        // 1 -> checkSelfPermission()
        // 2 -> requestPermissions() -> this method calls back to onRequestPermissionResults() that is overriden
        // 3 -> onRequestPermissionResults() -> handles the permission results that result from requestPermissions()
    }

    // this method is a callback of requestPermissions()
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                // if permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if not enabled after granted permission intent that will take the user to the control panel to turn GPS on
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                    // http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled
                }
                return;
        }
    }

    public void configureButtons() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // later work on the animation -> https://developer.android.com/training/material/animations.html#Reveal
                if (bStop.getVisibility() == View.INVISIBLE) {
                    bStop.setVisibility(View.VISIBLE);
                } else {
                    bStop.setVisibility(View.INVISIBLE);
                }
            }

        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = getRideAsJSON(ride).toString();
                Server_PostJSON post = new Server_PostJSON();
                post.setUp();
                post.execute(data);
                finish();
            }
        });
    }

    public boolean greenLight() {
        ConnectivityManager cnnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cnnMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private JSONObject getRideAsJSON(Ride ride) {

        Bitmap snapshot = mapView.getDrawingMapCache((float) 1.0, (float) 1.0, 1, 1);
        String stringedBitmap = Utils.getStringFromBitmap(snapshot);

        ride.setUsername(riderUsername);
        ride.setStartTime(startTime);
        ride.setFinishTime(finishTime);
        ride.setRouteRecord(record);
        ride.setSnapshot(stringedBitmap);

        JSONObject obj = new JSONObject();
        try {
            obj.put("rider", ride.getUsername()); // a String
            obj.put("startTime", String.valueOf(ride.getStartTime()));
            obj.put("finishTime", String.valueOf(ride.getFinishTime()));
            obj.put("coordinates", ride.getRouteRecord().toString());
            obj.put("snapshot", stringedBitmap);
            return obj;
        } catch (JSONException e) {
            Log.d("-> JSONifization failed", e.getMessage());
            return null;
        }

    }

}

