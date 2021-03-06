package com.example.autolocation;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonStartLocationUpdates).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
            }
            else {
                startLocationService();
            }
        });
        findViewById(R.id.buttonStopLocationUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else {
                Toast.makeText(this,"permission Denied!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager= (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager!=null){
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
                if (LocationServices.class.getName().equals(service.service.getClassName())){
                    if (service.foreground){
                        return false;
                    }
                }
            }
            return true;
        }
        return true;
    }
    private void startLocationService(){
        if (isLocationServiceRunning()){
            Intent intent= new Intent(getApplicationContext(),LocationServices.class);
            intent.setAction((Constants.ACTION_START_LOCATION_SERVICE));
            startService(intent);
            Toast.makeText(this,"location service started",Toast.LENGTH_SHORT).show();
        }

    }
    private void stopLocationService(){
        if (isLocationServiceRunning()){
            Intent intent= new Intent(getApplicationContext(),LocationServices.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"location Service stopped",Toast.LENGTH_SHORT).show();
        }
    }
}