package com.example.scarlet.Adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridLayoutDecoration extends RecyclerView.ItemDecoration {
    private int padding;
    private int margin;

    public GridLayoutDecoration(int padding, int margin) {
        this.padding = padding;
        this.margin=margin;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position=parent.getChildAdapterPosition(view);
        outRect.top = padding;
        outRect.bottom = padding;
        outRect.left = padding;
        outRect.right = padding;

        outRect.left = margin;
        outRect.right = margin;
        outRect.bottom=margin;
    }
}
