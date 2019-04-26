package com.example.inspirationrewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateProfile extends AppCompatActivity {

    // Profile picture for user
    ImageView newProfilePicture;

    ImageView addPhotoIcon;

    // File of the image
    private File currentImageFile;

    // Camera and Gallery request numbers
    private int CAMERA_REQUEST = 1;
    private int GALLERY_REQUEST = 2;

    // Base64 String encoding of profile image
    String encodedImage = "";

    // Checkbox button
    CheckBox admin;

    // Boolean to see if new user is an admin user
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        // Initialize newProfilePicture
        newProfilePicture = findViewById(R.id.user_photo_icon);

        // Initialize checkbox view
        admin = findViewById(R.id.admin_user);

        // Set up check box listener
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAdmin = isChecked;
            }
        });

        // Initialize addPhotoIcon
        addPhotoIcon = findViewById(R.id.add_icon);

        // Code before ovverides the security measures and allows you to save files to the device. These files would be the images the users take with their camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Set title bar of Activity to Create Profile
        getSupportActionBar().setTitle("Create Profile");
    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.create_profile_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Function that will handle callback when menu items are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Save menu item is pressed
        if (item.getItemId() == R.id.create_profile_save) {
            // Create AlertDialog for user to either cancel or save their profile

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set title
            builder.setTitle("Save Changes?");

            // Set icon
            builder.setIcon(R.drawable.logo);

            // User wishes to create profile
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToProfile();
                }
            });

            // User wishes to cancel the option to create their account
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            // Show AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    // Function that will set up new account to DB and then goes to profile activity
    public void goToProfile() {
        Toast.makeText(CreateProfile.this, "Account created!", Toast.LENGTH_SHORT).show();
        // Before creating account check fields

        // Check if username is empty
        String username;
        EditText usernameView = findViewById(R.id.new_username);
        if (usernameView.getText().toString().isEmpty()) {
            Toast.makeText(CreateProfile.this, "Please create a username", Toast.LENGTH_SHORT).show();
            return;
        }
        username = usernameView.getText().toString();

        // Check if password is empty
        String password;
        EditText passwordView = findViewById(R.id.new_password);
        if (passwordView.getText().toString().isEmpty()) {
            Toast.makeText(CreateProfile.this, "Please create a password", Toast.LENGTH_SHORT).show();
            return;
        }
        password = passwordView.getText().toString();

        // Check if first name is empty
        String firstName;
        EditText firstNameView = findViewById(R.id.new_first_name);
        if (firstNameView.getText().toString().isEmpty()) {
            Toast.makeText(CreateProfile.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            return;
        }
        firstName = firstNameView.getText().toString();

        // Check if last name is empty
        String lastName;
        EditText lastNameView = findViewById(R.id.new_last_name);
        if (lastNameView.getText().toString().isEmpty()) {
            Toast.makeText(CreateProfile.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            return;
        }
        lastName = lastNameView.getText().toString();

        // Check if department is empty
        String department;
        EditText departmentView = findViewById(R.id.department_field);
        if (departmentView.getText().toString().isEmpty()) {
            Toast.makeText(CreateProfile.this, "Please enter your department", Toast.LENGTH_SHORT).show();
            return;
        }
        department = departmentView.getText().toString();

        // Check if position is empty
        String position;
        EditText positionView = findViewById(R.id.position_field);
        if (positionView.getText().toString().isEmpty()) {
            Toast.makeText(CreateProfile.this, "Please enter your position", Toast.LENGTH_SHORT).show();
            return;
        }
        position = positionView.getText().toString();

        // New user has filled in all criteria above. New users do not have to put an image or write anything for their story

        String story = "";

        // New user did write something for their story
        EditText storyView = findViewById(R.id.new_bio);
        if (!storyView.getText().toString().isEmpty()) {
            story = storyView.getText().toString();
        }

        // Get location
        String city = getIntent().getStringExtra("City");
        String state = getIntent().getStringExtra("State");
        String location = city + ", " + state;

        // Add new user to database
        AsyncTaskCreateProfile createProfile = new AsyncTaskCreateProfile(username, password, firstName, lastName, department, story, position, isAdmin, location, encodedImage);
        createProfile.execute();
    }

    // Function that will prompt user to either cancel action, add an image from camera, or add an image from gallery
    public void addImage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set  title
        builder.setTitle("Profile Picture");

        // Set message
        builder.setMessage("Take picture from:");

        // Set icon
        builder.setIcon(R.drawable.logo);

        // User wishes to add an image through the Gallery
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doGallery();
            }
        });

        // User wishes to add an image through the Camera
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doCamera();
            }
        });

        // User cancels action
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

        // Set text color for neutral button
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#E3946F"));

        // Set text color for positive button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#728C87"));

        // Set text color for negative button
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#728C87"));

    }

    // Function that will go to the device's camera
    public void doCamera() {
        currentImageFile = new File(getExternalCacheDir(), "profileimage" + System.currentTimeMillis() + ".jpg");
        Intent pictureFromCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pictureFromCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(pictureFromCameraIntent, CAMERA_REQUEST);
    }

    // Function that will go to the device's gallery
    public void doGallery() {
        Intent pictureFromGalleryIntent = new Intent(Intent.ACTION_PICK);
        pictureFromGalleryIntent.setType("image/*");
        startActivityForResult(pictureFromGalleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // User takes a picture
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            processCamera();

            // Encode image to Base64
            encodedImage = convertToBase64();
        }

        // User gets a picture from gallery
        else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            processGallery(data);

            // Encode image to Base64
            encodedImage = convertToBase64();
        }
    }

    // Function to set image taken from the camera as the profile image
    private void processCamera() {
        Uri imageRef = Uri.fromFile(currentImageFile);
        newProfilePicture.setImageURI(imageRef);

        currentImageFile.delete();
    }

    // Function to set image retrieved from the gallery as the profile image
    private void processGallery(Intent data) {
        Uri imageRef = data.getData();

        if (imageRef != null) {
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageRef);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            newProfilePicture.setImageBitmap(selectedImage);
        }
    }

    // Function to convert images to base64 encoding
    public String convertToBase64() {
        Bitmap origBitmap = ((BitmapDrawable) newProfilePicture.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapAsByteArrayStream);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d("Base64", "convertToBase64: " + imgString);

        return imgString;
    }
}
