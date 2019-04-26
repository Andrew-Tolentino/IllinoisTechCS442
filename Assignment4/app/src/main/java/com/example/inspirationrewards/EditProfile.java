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
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfile extends AppCompatActivity {

    // Camera and Gallery request numbers
    private int CAMERA_REQUEST = 1;
    private int GALLERY_REQUEST = 2;

    // Base64 String encoding of profile image
    String encodedImage = "";

    // File of the image
    private File currentImageFile;

    // UserProfile object
    UserProfile currUser;

    // Username TextView
    TextView username;

    // Password TextView
    EditText password;

    // Admin Checkbox
    CheckBox admin;

    // First name TextView
    EditText firstName;

    // Last name TextView
    EditText lastName;

    // Profile ImageView
    ImageView profileImage;

    // Department TextView
    EditText department;

    // Position TextView
    EditText position;

    // Story TextView
    EditText story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Set title bar of Activity to Edit Profile
        getSupportActionBar().setTitle("Edit Profile");

        // Get UserProfile object from Profile Activity
        currUser = (UserProfile) getIntent().getSerializableExtra("userProfile");

        // Initialize username of user
        username =  findViewById(R.id.edit_profile_username);
        username.setText(currUser.getUsername());

        // Initialize password of user
        password = findViewById(R.id.edit_profile_password);
        password.setText(currUser.getPassword());

        // Initialize admin status of user
        admin = findViewById(R.id.edit_profile_admin_user);
        admin.setChecked(currUser.getAdmin());

        // Initialize first name of user
        firstName = findViewById(R.id.edit_profile_firstname);
        firstName.setText(currUser.getFirstName());

        // Initialize last name of user
        lastName = findViewById(R.id.edit_profile_lastname);
        lastName.setText(currUser.getLastName());

        // Initialize profile image
        Bitmap bitmap = BitmapFactory.decodeByteArray(currUser.getProfileImage(), 0, currUser.getProfileImage().length);
        profileImage = findViewById(R.id.edit_profile_image);
        profileImage.setImageBitmap(bitmap);

        // Initialize department of user
        department = findViewById(R.id.edit_profile_department_type);
        department.setText(currUser.getDepartment());

        // Initialize position of user
        position = findViewById(R.id.edit_profile_position_type);
        position.setText(currUser.getPosition());

        // Initialize story of user
        story = findViewById(R.id.edit_profile_your_story_content);
        story.setText(currUser.getStory());

        // Code before ovverides the security measures and allows you to save files to the device. These files would be the images the users take with their camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    // Inflate Activity with the edit profile menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Function that will handle callback when menu items are selected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // If user wishes to save their profile
        if (item.getItemId() == R.id.edit_profile_save) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set dialog title
            builder.setTitle("Save Changes?");

            // Set dialog icon
            builder.setIcon(R.drawable.icon);

            // If user wishes to save changes
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 // For now go back to profile
                    saveProfile();

                }
            });

            // If user cancels option to save changes
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            // Show dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        // User uses up navigation
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return  true;
        }

        return super.onOptionsItemSelected(item);
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
        currentImageFile = new File(getExternalCacheDir(), "editprofileimage" + System.currentTimeMillis() + ".jpg");
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
        profileImage.setImageURI(imageRef);

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
            profileImage.setImageBitmap(selectedImage);
        }
    }

    // Function to convert images to base64 encoding
    public String convertToBase64() {
        Bitmap origBitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapAsByteArrayStream);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d("Base64", "convertToBase64: " + imgString);

        return imgString;
    }


    public void saveProfile() {

        // Check if password is empty
        if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please do not leave password empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // User changed password
        else if (!password.getText().toString().equals(currUser.getPassword())) {
            currUser.setPassword(password.getText().toString());
        }


        // Check if first name is empty
        if (firstName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please do not leave first name empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // User changed first name
        else if (!firstName.getText().toString().equals(currUser.getFirstName())) {
            currUser.setFirstName(firstName.getText().toString());
        }


        // Check if last name is empty
        if (lastName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please do not leave last name empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // User changed last name
        else if (!lastName.getText().toString().equals(currUser.getLastName())) {
            currUser.setLastName(lastName.getText().toString());
        }


        // Check if department is empty
        if (department.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please do not leave your department field empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // User changed department field
        else if (!department.getText().toString().equals(currUser.getDepartment())) {
            currUser.setDepartment(department.getText().toString());
        }


        // Check if position is empty
        if (position.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please do not leave your position field empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // User changed position field
        else if (!position.getText().toString().equals(currUser.getPosition())) {
            currUser.setPosition(position.getText().toString());
        }

        // User changed their story
        if (!story.getText().toString().equals(currUser.getStory())) {
            currUser.setStory(story.getText().toString());
        }

        // User changed their profile image
        if (!encodedImage.isEmpty()) {
            // Change the byte array for the UserProfile
            byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            currUser.setProfileImage(imageBytes);
            AsyncTaskUpdateProfile asyncTaskUpdateProfile = new AsyncTaskUpdateProfile(this, currUser);
            asyncTaskUpdateProfile.execute();
        }
        else {
            AsyncTaskUpdateProfile asyncTaskUpdateProfile = new AsyncTaskUpdateProfile(this, currUser);
            asyncTaskUpdateProfile.execute();
        }



        // Toast that the user's changes have been saved. TODO: Make a custom toast
        Toast.makeText(this, "User Update Successful", Toast.LENGTH_SHORT).show();

        Intent goToProfileIntent = new Intent(this, Profile.class);


    }
}
