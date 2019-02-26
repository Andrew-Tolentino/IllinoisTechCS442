package com.example.maginc.multinotepad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView noteTitle;
    public TextView noteBody;
    public TextView noteDate;


    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.display_title);
        noteBody = itemView.findViewById(R.id.display_body);
        noteDate = itemView.findViewById(R.id.dispay_date);
    }
}
