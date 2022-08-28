package com.example.ZaeV_trip.Schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleFragment extends Fragment {
    RecyclerView bookmarkRecyclerView;
    BookmarkListAdapter listAdapter;
    //ItemTouchHelperCallback helper;
    ArrayList<BookmarkItem> items = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String docId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TravelActivity travelActivity = (TravelActivity) getActivity();
        travelActivity.bottomNavigationView.setVisibility(View.GONE);
        travelActivity.fab.setVisibility(View.GONE);

        View v =  inflater.inflate(R.layout.fragement_schedule, container, false);

        //recyclerview
        bookmarkRecyclerView = (RecyclerView) v.findViewById(R.id.scheduleRecycler);
        bookmarkRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        bookmarkRecyclerView.setLayoutManager(mLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //ItemTouchHelper 생성
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(listAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(bookmarkRecyclerView);

        //Bundle
        Bundle bundle = getArguments();
        Integer position = 0;
        String name = null;
        String date = null;
        String img = null;

        if(bundle!= null){
            position = bundle.getInt("position");
            name = bundle.getString("name");
            date = bundle.getString("date");
            img = bundle.getString("img");
        }

        //xml
        TextView travelName = v.findViewById(R.id.travel_name);
        travelName.setText(name);
        TextView travelDate = v.findViewById(R.id.travel_date);
        travelDate.setText(date);



        //Adapter에 데이터 추가
        Query query = db.collection("Schedule").document(uid).collection("schedule").whereEqualTo("name",name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult()){
                    docId = document.getId();
                    long days = document.getLong("days");
                    for(int i = 1; i <= days+1 ; i ++){
                        db.collection("Schedule").document(uid).collection("schedule").document(docId).collection(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
            }
        });

        CircleImageView travelImg = v.findViewById(R.id.travel_img);
        Glide.with(this)
                .load(img)
                .placeholder(R.drawable.default_bird_img)
                .error(R.drawable.default_bird_img)
                .fallback(R.drawable.default_bird_img)
                .into(travelImg);

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
