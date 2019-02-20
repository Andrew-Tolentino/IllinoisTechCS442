package com.example.maginc.multinotepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
