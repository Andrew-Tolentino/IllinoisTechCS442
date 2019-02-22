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
                goToMainActivity.putExtra("NEW_TITLE", title.getText().toString());
                goToMainActivity.putExtra("NEW_BODY", body.getText().toString());
                setResult(0, goToMainActivity);
                finish();
                Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If title of note is empty
        if (title.getText().toString().isEmpty()) {
            Toast.makeText(EditActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
            super.onBackPressed();

        }
        // If the title of note is not empty
        else {
            // Set up go back dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(EditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(EditActivity.this, "No from Dialog", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

            builder.setMessage("Save note");
            builder.setTitle("Your note is not saved!");
            AlertDialog goBackDialog = builder.create();
            goBackDialog.show();
        }

    }
}
