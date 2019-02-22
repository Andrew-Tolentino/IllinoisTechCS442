package com.example.maginc.multinotepad;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the recyclerView to the RecyclerView in the MainActivity layout
        recyclerView = findViewById(R.id.note_list);

        // Since the content does not change the layout size
        recyclerView.setHasFixedSize(true);

        // Assign LayoutManager to recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Set up the adapter for the recyclerView

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Instantiate an MenuInflator object for this Context
        MenuInflater inflater = getMenuInflater();

        // Inflate the xml file of the main_options to the menu for this Context
        inflater.inflate(R.menu.main_options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switch case for which Menu Item is selected
        switch(item.getItemId()) {
            // If the info Menu Item is selected
            case R.id.info_item:
                Toast.makeText(this, "Info option selected", Toast.LENGTH_SHORT).show();
                break;

            // If the new note Menu Item is selected
            case R.id.add_note_item:
                Toast.makeText(this, "New Note option selected", Toast.LENGTH_SHORT).show();

                // Instantiate Intent object and use it to Edit Activity
                Intent goToEdit = new Intent(this, EditActivity.class);
                startActivityForResult(goToEdit, 1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Going back to the Main Activity

        switch(requestCode) {
            // request code was for a new note
            case 1:
                Toast.makeText(this, "New Note recieved", Toast.LENGTH_SHORT).show();
                Log.d("NewNote", "onActivityResult title: " + data.getStringExtra("NEW_TITLE"));
                Log.d("NewNote", "onActivityResult body: " + data.getStringExtra("NEW_BODY"));
                break;

            // reqest code was to edit a note
            case 2:
                Toast.makeText(this, "Note edited!", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
