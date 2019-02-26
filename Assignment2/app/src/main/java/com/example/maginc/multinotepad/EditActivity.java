package com.example.maginc.multinotepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Set title to the EditText title
        title = findViewById(R.id.edit_title);

        // Set the body to the EditText body
        body = findViewById(R.id.edit_body);

        // Set the scrolling motion for the body
        body.setMovementMethod(new ScrollingMovementMethod());

        // Check if the intent passed by Main Activity contains any values. If so that means the user wishes to edit a note
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            title.setText(intent.getStringExtra("TITLE"));
            body.setText(intent.getStringExtra("BODY"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Instantiate an MenuInflator object for this Context
        MenuInflater inflater = getMenuInflater();

        // Inflate the xml file of the edit_options to the menu for this Context
        inflater.inflate(R.menu.edit_options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Save Menu Item is pressed
        if (item.getItemId() == R.id.save_item) {
            // Instantiate Intent object for the result
            Intent goToMainActivity = new Intent();

            // If the title has no text go to Main Activity and Toast nothing saved
            if (title.getText().toString().isEmpty()) {
                setResult(-1, goToMainActivity);
                finish();
                Toast.makeText(this, "The Untitled Note was not saved!", Toast.LENGTH_SHORT).show();
            }
            // Save the note
            else {
                Intent intent = getIntent();

                // User wished to edit a previous note. Check if they made any new changes to it
                if (intent.getExtras() != null) {
                    String currTitle = title.getText().toString();
                    String currBody = body.getText().toString();

                    // If the user did not make any changes
                    if (currTitle.equals(intent.getStringExtra("TITLE")) && currBody.equals(intent.getStringExtra("BODY"))) {
                        setResult(-1, goToMainActivity);
                        finish();
                    }

                    // If the user made changes
                    else {
                        goToMainActivity.putExtra("CHANGE_TITLE", title.getText().toString());
                        goToMainActivity.putExtra("CHANGE_BODY", body.getText().toString());
                        setResult(0, goToMainActivity);
                        finish();
                    }

                }

                // User wishes to create a new note
                else {
                    goToMainActivity.putExtra("CHANGE_TITLE", title.getText().toString());
                    goToMainActivity.putExtra("CHANGE_BODY", body.getText().toString());
                    setResult(0, goToMainActivity);
                    finish();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If title of note is empty
        if (title.getText().toString().isEmpty()) {
            Intent goToMainActivity = new Intent();
            setResult(-1, goToMainActivity);
            Toast.makeText(this, "The Untitled Note was not saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
        // If the title of note is not empty
        else {
            Intent i = new Intent();
            Intent intent = getIntent();

            // User wished to edit a previous note
            if (intent.getExtras() != null) {
                String currTitle = title.getText().toString();
                String currBody = body.getText().toString();

                // If the user did not make any changes go back to Main Activity without prompting dialog box
                if (currTitle.equals(intent.getStringExtra("TITLE")) && currBody.equals(intent.getStringExtra("BODY"))) {
                    setResult(-1, i);
                    finish();
                }

                // If the user did make changes show dialog box
                else {
                    // Set up go back dialog box
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // If user would like to save note
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent goToMainActivity = new Intent();
                            goToMainActivity.putExtra("CHANGE_TITLE", title.getText().toString());
                            goToMainActivity.putExtra("CHANGE_BODY", body.getText().toString());
                            setResult(0, goToMainActivity);
                            finish();
                        }
                    });

                    // If user would not like to save note
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent goToMainActivity = new Intent();
                            setResult(-1, goToMainActivity);
                            finish();

                        }
                    });

                    // Body for dialog box
                    builder.setMessage("Save note " + "'" + title.getText().toString() + "'");

                    // Title for dialog box
                    builder.setTitle("Your note is not saved!");

                    // Instantiate an Alert Dialog and display it on the screen
                    AlertDialog goBackDialog = builder.create();
                    goBackDialog.show();
                }
            }

            // User wishes to save a new note
            else {
                // Set up go back dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // If user would like to save note
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goToMainActivity = new Intent();
                        goToMainActivity.putExtra("CHANGE_TITLE", title.getText().toString());
                        goToMainActivity.putExtra("CHANGE_BODY", body.getText().toString());
                        setResult(0, goToMainActivity);
                        finish();
                    }
                });

                // If user would not like to save note
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goToMainActivity = new Intent();
                        setResult(-1, goToMainActivity);
                        finish();

                    }
                });

                // Body for dialog box
                builder.setMessage("Save note " + "'" + title.getText().toString() + "'");

                // Title for dialog box
                builder.setTitle("Your note is not saved!");

                // Instantiate an Alert Dialog and display it on the screen
                AlertDialog goBackDialog = builder.create();
                goBackDialog.show();
            }

        }

    }
}
