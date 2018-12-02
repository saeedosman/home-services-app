package com.example.aaron.groupprojectseg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeOwnerRatingListFragment extends Fragment {

    ArrayList<Rating> ratings;
    ListView listView;
    String username;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_owner_rating_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.ratingList);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        username = sharedPref.getString("username", null);

        listRatings(view);

    }

    public void listRatings(final View view) {

        ratings = new ArrayList<>();
        database.child("ratings").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot ratingSnapshot: dataSnapshot.getChildren()) {
                        Rating rating = ratingSnapshot.getValue(Rating.class);
                        ratings.add(rating);
                    }

                    System.out.println(ratings.toString());

                    HomeOwnerRatingListAdapter adapter = new HomeOwnerRatingListAdapter(ratings, getActivity());
                    listView.setAdapter(adapter);
                }

                else
                {
                    System.out.println("You haven't rated any services.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}