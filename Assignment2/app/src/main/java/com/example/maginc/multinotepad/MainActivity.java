package com.example.maginc.multinotepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    // Declare recyclerView
    private RecyclerView recyclerView;

    // Declare and initialize a List containing Note instances
    private List<Note> noteList = new ArrayList<>();

    // Declare the adapter used for the recyclerView
    private NoteAdapter noteAdapter = new NoteAdapter(noteList, this);

    // Will be use to keep track of the edit position
    private int editPos = -1;

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

        // Set the adapter for the recyclerView
        recyclerView.setAdapter(noteAdapter);

        // Separate each item in the recyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(15));

        // Load up JSON file if there is one
        this.loadFile();

        // Set title bar of Activity to include the number of notes
        if (noteList.size() > 0) {
            ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().setTitle(getString(R.string.app_name) + " (" + noteList.size() + ")");
        }
    }

    @Override
    protected void onStop() {
        this.writeNotesToFile();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Instantiate a MenuInflator object for this Context
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
                Intent goToInfo = new Intent(this, InfoActivity.class);
                startActivity(goToInfo);
                break;

            // If the new note Menu Item is selected
            case R.id.add_note_item:
                // Instantiate Intent object and use it go to Edit Activity
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
            // Request code was for a new note
            case 1:
                // The new note successfully saved
                if (resultCode == 0) {
                    // Retrieve the title and body of the new note
                    String noteTitle = data.getStringExtra("CHANGE_TITLE");
                    String noteBody = data.getStringExtra("CHANGE_BODY");

                    // Set timeStamp
                    Date now = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d, h:mm a");
                    String time = dateFormat.format(now).toString();

                    // Append the new note to the noteList
                    noteList.add(0, new Note(noteTitle, noteBody, time));

                    // Set title bar of Activity to include the number of notes
                    ActionBar actionBar = getSupportActionBar();
                    getSupportActionBar().setTitle(getString(R.string.app_name) + " (" + noteList.size() + ")");

                    // Tell adapter that a new note has been added to the noteList
                    noteAdapter.notifyDataSetChanged();
                }
                break;

            // Request code was to edit a note
            case 2:
                // The previous was successfully edited
                if (resultCode == 0) {
                    // Retrieve the title and body of the new note
                    String noteTitle = data.getStringExtra("CHANGE_TITLE");
                    String noteBody = data.getStringExtra("CHANGE_BODY");

                    // Make changes to the old note in noteList
                    Note currNote = noteList.get(editPos);
                    currNote.setTitle(noteTitle);
                    currNote.setBody(noteBody);

                    // Set the time the changes were saved
                    Date now = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d, h:mm a");
                    String time = dateFormat.format(now).toString();
                    currNote.setNewTimeStampe(time);

                    // Move edited note to the top. So far this is not very an efficient way of doing this
                    noteList.remove(editPos);
                    noteList.add(0, new Note(noteTitle, noteBody, time));
                    editPos = -1;


                    // Tell adapter that a note has been added to the noteList
                    noteAdapter.notifyDataSetChanged();
                }

                // The previous note has not been edited
                else if (resultCode == -1) {
                    // Set edit position back to -1
                    editPos = -1;
                }
                break;
        }

    }

    // View v in both functions below represents the ViewHolder in the RecyclerView


    @Override // Edit current note
    public void onClick(View v) {
        Intent gotoEdit = new Intent(this, EditActivity.class);

        // Retrieve title and body TextViews
        TextView title = v.findViewById(R.id.display_title);
        TextView body = v.findViewById(R.id.display_body);

        // Put the title and body of the note into the intent object
        gotoEdit.putExtra("TITLE", title.getText().toString());
        gotoEdit.putExtra("BODY", body.getText().toString());

        // Set position number of the view holder
        editPos = recyclerView.getChildLayoutPosition(v);

        // Start activity with request code for edit note
        startActivityForResult(gotoEdit, 2);
    }

    @Override // Show dialog box and ask user if they would like to delete current note
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get position of ViewHolder in RecyclerView
        final int pos = recyclerView.getChildLayoutPosition(v);

        // If the user would like to delete the current note
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteList.remove(pos);

                // Set title bar of Activity to include the number of notes
                if (noteList.size() > 0) {
                    ActionBar actionBar = getSupportActionBar();
                    getSupportActionBar().setTitle(getString(R.string.app_name) + " (" + noteList.size() + ")");
                }
                else {
                    ActionBar actionBar = getSupportActionBar();
                    getSupportActionBar().setTitle(getString(R.string.app_name));
                }

                noteAdapter.notifyDataSetChanged();
            }
        });

        // If the user would not like to delete the current note
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Set dialog title
        TextView title = v.findViewById(R.id.display_title);
        builder.setTitle("Delete Note " + "'" + title.getText().toString() + "'?");

        // Display dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    // Save the notes through this function
    private void writeNotesToFile() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.notes_file), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");

            // Begin array. Within the array each object represents a note
            writer.beginArray();
            for (int i = 0; i < noteList.size(); i++) {
                writer.beginObject();
                writer.name("title").value(noteList.get(i).getTitle());
                writer.name("body").value(noteList.get(i).getBody());
                writer.name("timeStamp").value(noteList.get(i).getTimeStamp());
                writer.endObject();
            }
            writer.endArray();
            writer.close();

            Log.d("Foo", "writeNotesToFile: Finished successfully?");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the JSON file through this function
    private void loadFile() {
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.notes_file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, getString(R.string.encoding)));

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject noteObject = jsonArray.getJSONObject(i);
                String title = noteObject.getString("title");
                String body = noteObject.getString("body");
                String date = noteObject.getString("timeStamp");

                noteList.add(new Note(title, body, date));

            }

            noteAdapter.notifyDataSetChanged();
            Log.d("Foo", "loadFile: Loaded successfully?");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Error", "loadFile: FileNotFoundException" + e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("Error", "loadFile: UnsupportedEncodingException" + e);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "loadFile: IOException" + e);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Error", "loadFile: JSONException" + e);
        }
    }


}
