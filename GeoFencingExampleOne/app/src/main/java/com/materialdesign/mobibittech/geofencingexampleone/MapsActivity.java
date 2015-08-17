package com.materialdesign.mobibittech.geofencingexampleone;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import android.location.Address;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.messages.Message;
import com.materialdesign.mobibittech.geofencingexampleone.beans.GeofenceData;
import com.materialdesign.mobibittech.geofencingexampleone.dialogfragments.DesisitionDialog;
import com.materialdesign.mobibittech.geofencingexampleone.extra.Validation;
import com.google.android.gms.common.api.Status;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    public Location mylocation;
    public LatLng markerlocation;
    private String TAG="MapsActivity";
    LinearLayout fencing_linearlayout;
    Button add_fencingButton;
    EditText fencingName;
    SeekBar radiusPB;
    Spinner duration;
    int actualDuration;
    TextView progressTV;
    private MarkerOptions markerOptions;
    public static  List<GeofenceData> mGeofenceDataList;
    public static final String FENCING_ID = "com.materialdesign.mobibittech.geofencingexampleone.FENCING_ID";

    private PendingIntent mGeofencePendingIntent;
    private Intent intent;
    private List<Geofence> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       buildGoogleApiClient();
        setContentView(R.layout.activity_maps);
        if(mGeofenceDataList==null) {
            mGeofenceDataList = new ArrayList<>();
        }

        mGeofencePendingIntent =null;
        setUpMapIfNeeded();



        getList();
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {

        mMap.setMyLocationEnabled(true);

        /*mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                DesisitionDialog desistionDialog = new DesisitionDialog() {
                    @Override
                    public void currrentLocation() {

                    }

                    @Override
                    public void cancel() {

                    }
                };
                desistionDialog.show(getSupportFragmentManager(),"");

                return false;
            }
        });*/
      /*  mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                setLog("marke_location"+latLng.latitude+","+latLng.longitude);
                LinearLayout infolayout = (LinearLayout)findViewById(R.id.fencinglayout);
                infolayout.setVisibility(View.VISIBLE);

                    markerlocation = latLng;

              markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions);


            }
        });*/
    }

   public void setLog(String log)
   {
       Log.e(TAG,"message  :"+log);
   }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        this.intent =intent;
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
private void addGeofencing()
{
    try {
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                // The GeofenceRequest object.
                getGeofencingRequest(),
                // A pending intent that that is reused when calling removeGeofences(). This
                // pending intent is used to generate an intent when a matched geofence
                // transition is observed.
                getGeofencePendingIntent()
        ).setResultCallback(this); // Result processed in onResult().
    } catch (Exception  securityException) {
        // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        logSecurityException(securityException);
    }

}

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.

        builder.addGeofences(getList());


        // Return a GeofencingRequest.
        return builder.build();
    }

    @Override
    public void onConnected(Bundle bundle) {


if(mGoogleApiClient.isConnected()) {
   final  Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    CameraPosition cameraPositionbuilder = new CameraPosition.Builder()
            .target(new LatLng(location.getLatitude(), location.getLongitude()))
            .zoom(13).build();
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionbuilder));
    final EditText placeName = (EditText)findViewById(R.id.placeET);
    final SeekBar rangeSB = (SeekBar)findViewById(R.id.radiusPB);
    rangeSB.setProgress(1);
    rangeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
if(rangeSB.getProgress()<=0)
{
    rangeSB.setProgress(1);
    Toast.makeText(MapsActivity.this,"Not equal to zero",Toast.LENGTH_LONG).show();
}
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });

    Button fencingButton = (Button)findViewById(R.id.add_fencingButton);
    fencingButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Validation.isValidLength(placeName, 3, "more then three words")

                   ) {

                    GeofenceData gdata = new GeofenceData();
                    gdata.setId(placeName.getText().toString());
                    gdata.setName(placeName.getText().toString());
                    gdata.setDescription("Range : " + rangeSB.getProgress() * 10 + "m");

                    gdata.setRadius(rangeSB.getProgress() * 10);
                    gdata.setLatLong(new LatLng(location.getLatitude(), location.getLongitude()));
                gdata.setFencing();
                    mGeofenceDataList.add(gdata);

                addCircleAndMarker(gdata);
                    addGeofencing();
                placeName.setText("");
                rangeSB.setProgress(1);
                }
                {

                }
            }

    });
  /* Button  removeButton = (Button)findViewById(R.id.removeButton);
    removeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            removeGeofencesButtonHandler();
            mGeofenceDataList.clear();
            mMap.clear();
        }
    });*/


  new AsyncTask<LatLng,Void,String>()
  {
      @Override
      protected String doInBackground(LatLng... latLngs) {

          LatLng loc =latLngs[0];
          Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

          String result = null;
          try {
              List<Address> addressList = geocoder.getFromLocation(
                      loc.latitude, loc.longitude, 1);
              if (addressList != null && addressList.size() > 0) {
                  Address address = addressList.get(0);
                  StringBuilder sb = new StringBuilder();
                  for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                      sb.append(address.getAddressLine(i)).append("\n");
                  }
                  sb.append(address.getLocality()).append("\n");
                  sb.append(address.getPostalCode()).append("\n");
                  sb.append(address.getCountryName());
                  result = sb.toString();
              }
          } catch (Exception e) {
              Log.e(TAG, "Unable connect to Geocoder", e);
          } finally {

          }
          return result;
      }

      @Override
      protected void onPostExecute(String s) {
          super.onPostExecute(s);
          TextView addressTV = (TextView)findViewById(R.id.addressTV);
          addressTV.setText(s);
      }
  }.execute(new LatLng(location.getLatitude(),location.getLongitude()));

}
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
//            setButtonsEnabledState();

            Toast.makeText(
                    this,
                   "Done Successfully"
                    ,Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }

    }


    private void logSecurityException(Exception securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    private List<Geofence>getActiveFencing()
    {
        intent = new Intent(MapsActivity.this,GeofenceTransitionsIntentService.class);
        List<Geofence> activelist;
        if(intent !=null) {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
           activelist = geofencingEvent.getTriggeringGeofences();
            return  activelist;

        }else
        {
            Log.e(TAG,"intent is null");
            return null;
        }
    }

    public List<Geofence> getList() {
        List<Geofence> list = new ArrayList<>();
        for (GeofenceData  data : mGeofenceDataList)

        {
        addCircleAndMarker(data);
list.add(data.getFencing());
        }
        return list;
    }

    public void removeGeofencesButtonHandler() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.

            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }
  private void addCircleAndMarker(GeofenceData geofencedata)
  {
      MarkerOptions markeroption = new MarkerOptions();
      markeroption.position(geofencedata.getLatLong());
      markeroption.title(geofencedata.getName());
      markeroption.snippet(geofencedata.getDescription());
      CircleOptions circleOptions = new CircleOptions();
      circleOptions.center(geofencedata.getLatLong());
      circleOptions.radius(geofencedata.getRadius());
      circleOptions.fillColor(getResources().getColor(R.color.trans));
     circleOptions.strokeWidth(0);
      mMap.addMarker(markeroption);
      mMap.addCircle(circleOptions);


  }

}
