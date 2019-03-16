package com.example.stockwatch;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

// This class is used to add separation to each item in the recycler view
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int verticalHeight;

    public DividerItemDecoration(int height) {
        this.verticalHeight = height;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = verticalHeight;
        }
    }
}
