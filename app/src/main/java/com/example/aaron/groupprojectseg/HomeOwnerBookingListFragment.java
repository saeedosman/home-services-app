package com.example.aaron.groupprojectseg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeOwnerBookingListFragment extends Fragment {

    ArrayList<Booking> bookings;
    ListView listView;
    String username;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_owner_booking_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.bookingList);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        username = sharedPref.getString("username", null);

        listBookings(view);

    }



    public void listBookings(final View view) {
        bookings = new ArrayList<>();
        database.child("bookings").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot bookingSnapshot: dataSnapshot.getChildren()) {
                        Booking booking = bookingSnapshot.getValue(Booking.class);
                        bookings.add(booking);
                    }

                    System.out.println(bookings.toString());

                    ArrayList<String> bookingList = new ArrayList<>();

                    HomeOwnerBookingListAdapter adapter = new HomeOwnerBookingListAdapter(bookings, getActivity());
                    listView.setAdapter(adapter);
                }

                else
                {
                    System.out.println("You haven't booked any services.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}