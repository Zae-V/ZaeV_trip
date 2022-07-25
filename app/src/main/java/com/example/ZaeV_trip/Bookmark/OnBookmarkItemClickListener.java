package com.example.ZaeV_trip.Bookmark;

import android.view.View;

public interface OnBookmarkItemClickListener {
    public void onItemClick(BookmarkListAdapter.ItemViewHolder holder, View view, int position);
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
}