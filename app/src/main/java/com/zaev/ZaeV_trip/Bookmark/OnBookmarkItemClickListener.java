package com.zaev.ZaeV_trip.Bookmark;

public interface OnBookmarkItemClickListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
}