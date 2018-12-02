package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeOwnerBookingListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Booking> bookings;
    private Context context;
    String username;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public HomeOwnerBookingListAdapter(ArrayList<Booking> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bookings.size();
    }

    @Override
    public Object getItem(int pos) {
        return bookings.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.home_owner_booking_item, null);
        }


        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        username = sharedPref.getString("username", null);

        TextView itemNameText = (TextView) view.findViewById(R.id.item_name);
        itemNameText.setText(bookings.get(position).getServiceProvider().getName());

        String service = bookings.get(position).getService();

        TextView itemServiceText = (TextView) view.findViewById(R.id.item_services);
        itemServiceText.setText(Html.fromHtml("<b>Service:</b> " + service));

        AvailableTime time = bookings.get(position).getTime();

        TextView itemHoursText = (TextView) view.findViewById(R.id.item_hours);
        String timeString = time.getDay() + ", " + time.getStartTime() + "-" + time.getEndTime();
        itemHoursText.setText(Html.fromHtml("<b>Time:</b> " + timeString));

        ImageButton rateButton = view.findViewById(R.id.rateButton);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateService(v, bookings.get(position).getServiceProvider());
            }
        });

        return view;
    }

    public void rateService(View v, final ServiceProvider serviceProvider) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.home_owner_rate_service_dialog, null);
        dialogBuilder.setView(dialogView);

        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        final EditText comment = dialogView.findViewById(R.id.comment);

        ratingBar.setOnRatingBarChangeListener( new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged( final RatingBar ratingBar, final float rating, final boolean fromUser ) {
                if ( fromUser ) {
                    ratingBar.setRating( (float) Math.ceil(rating) );
                }
            }
        });


        dialogBuilder.setTitle("Rate");
        dialogBuilder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final Rating rating = new Rating(serviceProvider, username, ratingBar.getRating(), comment.getText().toString());

                database.child("ratings").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            System.out.println(dataSnapshot.toString());
                            for (final DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                                if ((ratingSnapshot.getValue(Rating.class).getServiceProvider().getName()).equals(serviceProvider.getName())) {
                                    ratingSnapshot.getRef().setValue(rating);
                                    showToast("Updated rating.");
                                    return;
                                }
                            }
                        }

                        dataSnapshot.getRef().push().setValue(rating, new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {

                                System.out.println("Value was set. Error = " + error);
                                if (error == null) {
                                    showToast("Rated service");
                                } else {
                                    showToast("Booking creation error: " + error);
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}