package com.example.maginc.multinotepad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> myList = new ArrayList<>();
    private MainActivity ma;

    public NoteAdapter(List<Note> list, MainActivity ma) {
        this.myList = list;
        this.ma = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflate itemView with the note_view_holder xml file
        View itemView = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_view_holder, viewGroup, false);

        // Set the onClick and onLongClick listeners to the view
        itemView.setOnClickListener(this.ma);
        itemView.setOnLongClickListener(this.ma);


        // Return the ViewHolder passing its layout as an argument
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        // Initialize the noteViewHolder template with the corresponding elements of the Note instance from myList
        Note currNote = this.myList.get(i);
        noteViewHolder.noteTitle.setText(currNote.getTitle());
        noteViewHolder.noteBody.setText((currNote.cutBody()));
        noteViewHolder.noteDate.setText(currNote.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return this.myList.size();
    }
}
