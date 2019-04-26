package com.example.inspirationrewards;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

// Value for permissions granted
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static java.net.HttpURLConnection.HTTP_OK;

public class MainActivity extends AppCompatActivity {

    // Progress bar when signing in
    ProgressBar progressBar;

    // User's account name
    EditText userName;

    // User's password
    EditText password;

    // Checkbox reference to remember the user's credentials
    CheckBox checkBox;

    // locationManager contains a reference to the system service for the device's location
    LocationManager locationManager;

    // The user's current location
    private Location currentLocation;

    // Criteria for the location information
    private Criteria criteria;

    // Request code for permission to ask for user's location
    private static int locationRequestCode = 1;

    // The user's city and state will be stored below
    public String city = "";
    public String state = "";

    // SharedPreferences if the user wants to save their credentials
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize progressBar and set the visibility to gone
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.GONE);

        // Initialize userName
        userName = findViewById(R.id.username_field);

        // Initialize password
        password = findViewById(R.id.password_field);

        // Initialize checkBox
        checkBox = findViewById(R.id.remember_user);

        // Get a reference to the system's location provider
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Setting up the criteria for the location
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);


        // Check if user did not grant permissions to location

        // If the permission has not been asked before or denied
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            // Ask for Location and File permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locationRequestCode);
        }

        // Permission for location has been accepted
        else {
            getAddressAndState();
        }

        // Initialize preferences with the SharedPreferences of Main Activity
        preferences = this.getSharedPreferences("REMEMBER_CREDENTIALS", Context.MODE_PRIVATE);
        editor = preferences.edit();

        // Initialize userName and password. These values will only exist if the user checked the remember my credentials
        if (preferences.contains("username") && preferences.contains("password")) {
            // Set editText to the username and mark the checkbox
            String savedUserName = preferences.getString("username", "");
            userName.setText(savedUserName);

            // Set editText to the password and mark the checkbox
            String savedPassword = preferences.getString("password", "");
            password.setText(savedPassword);

            // Set the checkbox to true
            checkBox.setChecked(true);
        }
    }

    // Function to sign in user
    public void signIn(View view) {
        // If username and password fields are not empty
       if (!userName.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
           // Set visibility for progress bar
           progressBar.setVisibility(View.VISIBLE);
           AsyncTaskLogin asyncTaskLogin = new AsyncTaskLogin(this, userName.getText().toString(), password.getText().toString(), city, state);
           asyncTaskLogin.execute();
       }
       else {
           Toast.makeText(this, "Please enter your username or password", Toast.LENGTH_SHORT).show();
       }
    }

    // Function to continue on after user logs in
    public void goToProfile(UserProfile myProfile) {

        // Hide progress bar
        progressBar.setVisibility(View.GONE);

            // User enabled location services. Once a user logs in, update profile of their current log in location
            AsyncTaskUpdateLocation asyncTaskUpdateLocation = new AsyncTaskUpdateLocation(myProfile);
            asyncTaskUpdateLocation.execute();


            Intent goToProfileIntent = new Intent(this, Profile.class);

            goToProfileIntent.putExtra("userProfile", myProfile);

            // Go to profile activity
             startActivity(goToProfileIntent);
    }

    // Function to remember the username and password if the marks the checkbox. The credentials will be stored in the shared preferences
    public void rememberCredentials(View view) {
        boolean checked = checkBox.isChecked();

        // If the user wants to the app to remember their credentials
        if (checked) {
            // Check if both username and password fields are not empty
            String userNameToSave = userName.getText().toString();
            String passwordToSave = password.getText().toString();

            // Username and password are not empty
            if (!userNameToSave.isEmpty() && !passwordToSave.isEmpty()) {
                // Save the username and password as sharedPreferences
                editor.putString("username", userNameToSave);
                editor.putString("password", passwordToSave);
                editor.apply();

                Log.d("Cred", "password: " + preferences.getString("password", ""));
            }

            // Either username and/or password is empty
            else {
                Toast.makeText(this, "Please fill in username and password to save credentials", Toast.LENGTH_SHORT).show();
                checkBox.setChecked(false);
            }
        }

        // The user un-checks the checkbox
        else {
            // Remove all pairs in sharedPreferences
            editor.clear();
            editor.apply();
        }
    }

    // Function to create a new user
    public void createAccount(View view) {
        Intent i = new Intent(this, CreateProfile.class);
        i.putExtra("City", city);
        i.putExtra("State", state);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Toast.makeText(this, "Request Code: " + grantResults.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Permissions", "onRequestPermissionsResult: " + grantResults[0]);
            getAddressAndState();
        }
    }

    // Function to get user's address and state from their longitude and latitude
    @SuppressLint("MissingPermission")
    private void getAddressAndState() {
        String bestProvider = locationManager.getBestProvider(criteria, true);

        currentLocation = locationManager.getLastKnownLocation(bestProvider);

        // Retrieve user's latitude and longitude
        if (currentLocation != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();

            try {
                // List of addresses the geocoder will return given the latitude and longitude
                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);

                // Initialize the user's city and state
                city = address.get(0).getLocality();
                state = address.get(0).getAdminArea();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}


