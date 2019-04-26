package com.example.inspirationrewards;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewProfile extends AppCompatActivity {

    // The examined user's profile
    UserProfile profile;

    // TextView of user's name
    TextView name;

    // TextView of user's number of rewarded points
    TextView rewardedPoints;

    // TextView of user's department
    TextView department;

    // TextView of user's position
    TextView position;

    // TextView of user's story
    TextView story;

    // ImageView of user's profile image
    ImageView profileImage;

    // Edit Text representing the number of points you wish to reward this user
    EditText numPoints;

    // Edit Text representing the your comment to the user
    EditText comment;

    // Viewer's username
    private String username;

    // Viewer's password
    private String password;

    // Total rewarded points
    private int totalRewardedPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // Initialize profile
        profile = (UserProfile) getIntent().getSerializableExtra("profile");

        // Initialize viewer's username
        username = getIntent().getStringExtra("username");

        // Initialize viewer's password
        password = getIntent().getStringExtra("password");

        //

        // Initialize name
        name = findViewById(R.id.view_profile_name);
        name.setText(profile.getLastName() + ", " + profile.getFirstName());

        // Initialize rewardedPoints
        totalRewardedPoints = getIntent().getIntExtra("totalRewardedPoints", 0);
        rewardedPoints = findViewById(R.id.view_profile_points_awarded_content);
        rewardedPoints.setText(Integer.toString(totalRewardedPoints));

        // Initialize department
        department = findViewById(R.id.view_profile_department_content);
        department.setText(profile.getDepartment());

        // Initialize position
        position = findViewById(R.id.view_profile_position_content);
        position.setText(profile.getPosition());

        // Initialize story
        story = findViewById(R.id.view_profile_story_content);
        story.setText(profile.getStory());

        // Initialize profileImage
        profileImage = findViewById(R.id.view_profile_image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(profile.getProfileImage(), 0, profile.getProfileImage().length);
        profileImage.setImageBitmap(bitmap);

        // Initialize numPoints
        numPoints = findViewById(R.id.view_profile_reward_points_content);

        // Initialize comment
        comment = findViewById(R.id.view_profile_comment_content);

        // Set title bar of Activity to examine user's name
        getSupportActionBar().setTitle(profile.getLastName() + ", " + profile.getFirstName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.view_profile_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Save item is pressed
        if (item.getItemId() == R.id.view_profile_save) {
            // Create Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set Title
            builder.setTitle("Add Rewards Points");

            // Set Message
            builder.setMessage("Add rewards for " + profile.getFirstName() + " " + profile.getLastName() + "?");

            // Set icon
            builder.setIcon(R.drawable.icon);

            // Set positive button
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addReward();
                }
            });

            // Set negative button
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            // Show dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addReward() {
        // Check if there is a value the viewer gave
        if (!numPoints.getText().toString().isEmpty()) {
            int rewardedPoints = Integer.parseInt(numPoints.getText().toString());
            String name = profile.getFirstName() + " " + profile.getLastName();
            Date now = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String time = dateFormat.format(now).toString();

            String note = "";

            // If the comment is not empty
            if (!comment.getText().toString().isEmpty()) {
                note = comment.getText().toString();
            }

            // Send request to reward user
            AsyncTaskGiveReward asyncTaskGiveReward = new AsyncTaskGiveReward(this, profile.getUsername(), name, time, note,  rewardedPoints, username, password);
            asyncTaskGiveReward.execute();
        }

        else {
            Toast.makeText(this, "Something went wrong in rewarding this user points!", Toast.LENGTH_SHORT).show();
        }
    }
}
