package ir.picky.app.mapdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ir.picky.app.mapdemo.services.MyService;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng source, destination;
    boolean buttonStat = true;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean locationFlag = true;
    boolean doubleBack = false;
    public final long delayBackPressed = 2000;
    Dialog barType, sourceDetail, decDetail;
    public int sourceLevel, sourceUnit, decLevel, decUnit , distanceInmeter;
    String sourceDesc, decDesc;
    private final String TAG = "PLACECOMPLETE_EXERCISE";
    private final int PLACE_AUTOCOMPLETE_REQUEST = 1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        mMap.setMyLocationEnabled(true);
                        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            }
        }
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        mMap.setMyLocationEnabled(true);
                        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        ImageView Iv = findViewById(R.id.imageView2);
        //khandan ax az assets
        String pinSrcSpace = "pinsrcspace.png";
        try {
            InputStream st = getAssets().open(pinSrcSpace);
            Drawable d = Drawable.createFromStream(st, null);
            Iv.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (locationFlag) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    //mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                    locationFlag = false;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                mMap.setMyLocationEnabled(true);
                //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                mMap.setMyLocationEnabled(true);
                //mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }
        //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location lastKnownLocation = new Location(bestProvider);
        LatLng userLocation;

        // Get location from GPS رضا
        //Location lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);

        if (lastKnownLocation == null || lastKnownLocation.getLatitude() == 0) {
            userLocation = new LatLng(32.657351, 51.677506);   //meidon emam
        } else {
            userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }
        //mMap.setMapType(googleMap.MAP_TYPE_HYBRID);
        //Zoom Level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
    }

    protected void selectAPlace() {
        try {
            Intent intent;
            // A filter can be used to restrict the kinds of predictions that
            // the autocomplete widget provides. In this case, the code
            // only displays places that are business establishments
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build();

            // Mode can either be MODE_FULLSCREEN or MODE_OVERLAY
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                    .setBoundsBias(new LatLngBounds.Builder()
//                            .include(new LatLng(31,52))
//                            .include(new LatLng(32,53))
//                            .build())
                    .setFilter(filter).build(this);

            // Start the Autocomplete Activity. the result will be returned
            // in the onActivityResult function
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void onSearchClicked(View view) {
        selectAPlace();
    }

    // Updates the TextView in the main Activity with the selected Place details
    private void updateUI(Place chosenPlace) {

        if (chosenPlace == null) {
            Toast.makeText(this, "مکانی انتخاب نشد.", Toast.LENGTH_SHORT).show();
        } else {
            LatLng searched = chosenPlace.getLatLng();
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(searched));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(searched));
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                updateUI(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
                updateUI(null);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message =
                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            distanceInmeter = getDistance(message);
        }
    };

    public int getDistance(String distanceRaw) {
        JSONObject json = null;
        try {
            json = new JSONObject(distanceRaw);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray newTempARr = routes.getJSONArray("legs");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            return Integer.valueOf(distOb.getString("value"));
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void sourceConfirmButtonClickHandler(View view) {

        locationFlag = false;
        int ht_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 47, getResources().getDisplayMetrics());
        int wt_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        Button btn = findViewById(R.id.sourceConfirmButton);
        if (!buttonStat) {
            destination = mMap.getCameraPosition().target;
            //LatLng sorce2 = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
            mMap.addMarker(new MarkerOptions().position(destination)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pindec", ht_px, wt_px))));

            ImageView clearScreen = findViewById(R.id.imageView2);
            clearScreen.setVisibility(View.INVISIBLE);

            String distanceUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + source.latitude + "," + source.longitude +
                    "&destination=" + destination.latitude + "," + destination.longitude +
                    "&sensor=false";

            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(distanceUrl));
            startService(intent);

//            View v = findViewById(R.id.mapdimout);
//            v.setVisibility(View.VISIBLE);

            startBarTypeDialog();

//            v = findViewById(R.id.sourceConfirmButton);
//            v.setVisibility(View.INVISIBLE);
            //startActivity(new Intent(MapsActivity.this, ChooseServiceTypePopUp.class));
        }
        if (buttonStat) {
            source = mMap.getCameraPosition().target;
            mMap.addMarker(new MarkerOptions().position(source)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pinsrc", ht_px, wt_px))));

            btn.setText("تایید مقصد");

            ImageView Iv = findViewById(R.id.imageView2);
            String pinSrcSpace = "pindecspace.png";
            try {
                InputStream st = getAssets().open(pinSrcSpace);
                Drawable d = Drawable.createFromStream(st, null);
                Iv.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Double newPositionLat = source.latitude + 0.003d;
            Double newPositionLong = source.longitude + 0.003d;
            LatLng newPosition = new LatLng(newPositionLat, newPositionLong);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
            buttonStat = false;
        }
    }

    private void startBarTypeDialog() {
        barType = new Dialog(this);
        barType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        barType.setContentView(R.layout.bartype_dialog);
        barType.show();
        barType.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        ImageButton barbutton =  barType.findViewById(R.id.barbutton);
        barbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, CarType.class) ;
                startActivity(intent);
            }
        });
        ImageButton asbabbutton =  barType.findViewById(R.id.asbabbutton);
        asbabbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               asbabClicked();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (doubleBack) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
//            if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.LOLLIPOP)
//                finishAndRemoveTask();
//            else
//                finish();
        }

        this.doubleBack = true;
        Toast.makeText(this, "برای خروج بازگشت را دوباره بزنید.", Toast.LENGTH_SHORT).show();

        mMap.clear();
        Button btn = findViewById(R.id.sourceConfirmButton);
        btn.setText("تایید مبدا");
        buttonStat = true;
        ImageView Iv = findViewById(R.id.imageView2);
        String pinSrcSpace = "pinsrcspace.png";
        try {
            InputStream st = getAssets().open(pinSrcSpace);
            Drawable d = Drawable.createFromStream(st, null);
            Iv.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (source != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                doubleBack = false;
            }
        }, delayBackPressed);
    }

    public void asbabClicked() {
        sourceDetail = new Dialog(this);
        sourceDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sourceDetail.setContentView(R.layout.sourcedetail_dialog);
        sourceDetail.show();
        Button sourceCancel = sourceDetail.findViewById(R.id.cancelsource);
        sourceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceDetail.hide();
            }
        });
        Button nextStepSource = sourceDetail.findViewById(R.id.nextstepsource);
        nextStepSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText view = sourceDetail.findViewById(R.id.sourceunit);
                if (view.getText().length() == 0) {
                    sourceUnit = 0;
                } else {
                    sourceUnit = Integer.valueOf(view.getText().toString());
                }
                view = sourceDetail.findViewById(R.id.sourcedetailbar);
                if (view.getText().length() == 0) {
                    sourceDesc = "NaN";
                } else {
                    sourceDesc = view.getText().toString();
                }
                view = sourceDetail.findViewById(R.id.sourcefloor);
                if (view.getText().length() == 0) {
                    Toast.makeText(MapsActivity.this, "طبقه را به درستی وارد کنید.", Toast.LENGTH_SHORT).show();
                } else {
                    sourceLevel = Integer.valueOf(view.getText().toString());
                    startDestination();
                }
            }
        });
    }

    public void startDestination() {
        decDetail = new Dialog(this);
        decDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        decDetail.setContentView(R.layout.decdetail_dialog);
        decDetail.show();
        Button decCancel = decDetail.findViewById(R.id.cancelDestination);
        decCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decDetail.hide();
            }
        });
        Button nextStepDestination = decDetail.findViewById(R.id.nextstepDestination);
        nextStepDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText view = decDetail.findViewById(R.id.Destinationunit);
                if (view.getText().length() == 0) {
                    decUnit = 0;
                } else {
                    decUnit = Integer.valueOf(view.getText().toString());
                }
                view = decDetail.findViewById(R.id.destinationdetailbar);
                if (view.getText().length() == 0){
                    decDesc = "NaN";
                } else {
                    decDesc = view.getText().toString();
                }
                view = decDetail.findViewById(R.id.Destinationfloor);
                if (view.getText().length() == 0){
                    Toast.makeText(MapsActivity.this, "طبقه را به درستی وارد کنید.", Toast.LENGTH_SHORT).show();
                } else {
                    decLevel = Integer.valueOf(view.getText().toString());
                    storeInDatabase();
                    startActivity(new Intent(MapsActivity.this, CarType.class));
                }
            }
        });
    }

    private void storeInDatabase() {

    }

}
