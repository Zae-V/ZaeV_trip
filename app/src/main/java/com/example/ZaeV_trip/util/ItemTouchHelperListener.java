package com.example.ZaeV_trip.util;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
}
