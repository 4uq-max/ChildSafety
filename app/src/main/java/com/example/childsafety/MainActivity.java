package com.example.childsafety;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public static double Lat;
    public static double Lon;
    private static MainActivity inst;
    private String smsMessage;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public void updateMessage(final String message){
        smsMessage = message;
        Log.d("sms received: ", smsMessage);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GetPermissions();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Lat = location.getLatitude();
                    Lon = location.getLongitude();
                    Log.d("Latitude: ", String.valueOf(Lat));
                    Log.d("Longitude: ", String.valueOf(Lon));
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.d("Latitude", "disable");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.d("Latitude", "enable");
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    Log.d("Latitude", "status");
                }
            });
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        784);
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        785);

            }
        }
        setContentView(R.layout.activity_main);
    }

    public void sendCoordinates(View view){
        EditText no = (EditText)findViewById(R.id.editText);
        String address = no.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        String m = String.valueOf(MainActivity.Lat)+" "+String.valueOf(MainActivity.Lon);
        smsManager.sendTextMessage(address, null, m, null, null);

    }

    public void getGoogleMap(View view){
        //MapsActivity fragment = new MapsActivity();
        System.out.println("Tested");
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        //intent.putExtra("Lat",Lat);
        //intent.putExtra("Lon", Lon);
        startActivity(intent);

        //For fragment
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragment);
        transaction.commit();*/

    }


    public void GetPermissions()
    {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Give Location")
                        .setMessage("Please provide location to proceed")
                        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        99);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        99);
            }
        }

    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("request code: ",String.valueOf(requestCode));
        for(String s: permissions){
            Log.d("permissions: ",s);
        }
        for(int i: grantResults){
            Log.d("grantResults: ",String.valueOf(i));
        }

        switch (requestCode) {
            case 99: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Lat = location.getLatitude();
                                Lon = location.getLongitude();
                                Log.d("Latitude: ",String.valueOf(Lat));
                                Log.d("Longitude: ",String.valueOf(Lon));
                            }
                            @Override
                            public void onProviderDisabled(String provider) {
                                Log.d("Latitude","disable");
                            }
                            @Override
                            public void onProviderEnabled(String provider) {
                                Log.d("Latitude","enable");
                            }
                            @Override
                            public void onStatusChanged(String provider, int status,
                                                        Bundle extras) {
                                Log.d("Latitude","status");
                            }
                        });
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS )
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.RECEIVE_SMS},
                                    784);
                        }
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS )
                                    != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.SEND_SMS},
                                        785);

                        }

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                break;
            }
            case 784: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 785: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
}
