package com.example.ZaeV_trip.Schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ZaeV_trip.Bookmark.BookmarkItem;
import com.example.ZaeV_trip.Bookmark.BookmarkListAdapter;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.ItemTouchHelperCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleFragment extends Fragment {
    RecyclerView bookmarkRecyclerView;
    BookmarkListAdapter listAdapter;
    //ItemTouchHelperCallback helper;
    ArrayList<BookmarkItem> items = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        BookmarkViewModel bookmarkViewModel =
//                new ViewModelProvider(this).get(BookmarkViewModel.class);

        //final TextView textView = binding.bookmarkText;

        //bookmarkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        View v =  inflater.inflate(R.layout.fragement_schedule, container, false);

        //recyclerview
        bookmarkRecyclerView = (RecyclerView) v.findViewById(R.id.scheduleRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //ItemTouchHelper 생성
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(bookmarkRecyclerView);

        //Bundle
        Bundle bundle = getArguments();
        Integer position = 0;
        if(bundle!= null){
            position = bundle.getInt("position");
            Log.e("position", String.valueOf(position));
        }

        //Adapter에 데이터 추가
        Integer finalPosition = position;
        db.collection("Schedule").document(uid).collection("schedule").orderBy("Time", Query.Direction.ASCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult()){
                    String docId = document.getId();
                    db.collection("Schedule").document(uid).collection("schedule").document(docId).collection(String.valueOf(finalPosition)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot document : task.getResult()){
                                items.add(new BookmarkItem(String.valueOf(document.getData().get("img")),String.valueOf(document.getData().get("name")),String.valueOf(document.getData().get("location")),String.valueOf(document.getData().get("info"))));
                            }
                            listAdapter = new BookmarkListAdapter(getActivity(),items);
                            bookmarkRecyclerView.setAdapter(listAdapter);
                        }
                    });
                }
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        travelActivity.fab.setVisibility(View.VISIBLE);
    }
}
