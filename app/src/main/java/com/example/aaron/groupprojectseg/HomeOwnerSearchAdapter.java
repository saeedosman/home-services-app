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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Pattern;

public class HomeOwnerSearchAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<ServiceProvider> serviceProviders;
    private ArrayList<ServiceProvider> searchResults;
    private HashSet<String> serviceProviderSet;
    private Context context;
    private String username;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public HomeOwnerSearchAdapter(ArrayList<ServiceProvider> serviceProviders, Context context) {
        this.serviceProviders = serviceProviders;
        this.context = context;
        this.searchResults = new ArrayList<>();
        this.searchResults.addAll(serviceProviders);
        this.serviceProviderSet = new HashSet<>();
        for (ServiceProvider serviceProvider : serviceProviders)
            serviceProviderSet.add(serviceProvider.getName());
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Object getItem(int pos) {
        return searchResults.get(pos);
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
            view = inflater.inflate(R.layout.home_owner_search_item, null);
        }

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        username = sharedPref.getString("username", null);


        TextView itemNameText = (TextView) view.findViewById(R.id.item_name);
        itemNameText.setText(searchResults.get(position).getName());

        ArrayList<String> services = searchResults.get(position).getServices();

        TextView itemServiceText = (TextView) view.findViewById(R.id.item_services);
        StringBuilder serviceTextBuilder = new StringBuilder();
        serviceTextBuilder.append("<b>Services:</b> ");
        for (int i = 0; i < services.size(); i++) {
            String service = services.get(i).split(Pattern.quote(" - $"))[0];
            if (i < services.size() - 1) serviceTextBuilder.append(service + ", ");
            else serviceTextBuilder.append(service);
        }
        itemServiceText.setText(Html.fromHtml(serviceTextBuilder.toString()));

        ArrayList<AvailableTime> hours = searchResults.get(position).getAvailableTimes();

        TextView itemHoursText = (TextView) view.findViewById(R.id.item_hours);
        StringBuilder hoursTextBuilder = new StringBuilder();
        hoursTextBuilder.append("<b>Hours:</b> ");
        for (int i = 0; i < hours.size(); i++) {
            String hoursText = hours.get(i).getDay() + " " + hours.get(i).getStartTime() + "-" + hours.get(i).getEndTime();
            if (i < hours.size() - 1) hoursTextBuilder.append(hoursText + ", ");
            else hoursTextBuilder.append(hoursText);
        }
        itemHoursText.setText(Html.fromHtml(hoursTextBuilder.toString()));

        final RatingBar ratingBar = view.findViewById(R.id.serviceProviderRating);

        database.child("ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float ratingScore = 0;
                float ratingCount = 0;
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                        if ((ratingSnapshot.getValue(Rating.class).getServiceProvider().getName()).equals(searchResults.get(position).getName())) {
                            ratingCount++;
                            ratingScore += ratingSnapshot.getValue(Rating.class).getRating();
                        }
                    }
                    if (ratingCount > 0) {
                        float average = ratingScore / ratingCount;
                        ratingBar.setRating(average);

                    }

                    else ratingBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ImageButton bookButton = view.findViewById(R.id.bookButton);
        ImageButton rateButton = view.findViewById(R.id.rateButton);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookService(v, searchResults.get(position));
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateService(v, searchResults.get(position));
            }
        });


        return view;
    }

    public void bookService(View v, final ServiceProvider serviceProvider) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.home_owner_book_service_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner serviceSpinner = dialogView.findViewById(R.id.serviceSpinner);
        ArrayList<String> serviceList = serviceProvider.getServices();
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, serviceList);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);

        final Spinner timeSpinner = dialogView.findViewById(R.id.timeSpinner);
        ArrayList<String> timeList = new ArrayList<>();
        final ArrayList<AvailableTime> availableTimeList = serviceProvider.getAvailableTimes();
        for (AvailableTime availableTime : availableTimeList) {
            timeList.add(availableTime.getDay() + ", " + availableTime.getStartTime() + "-" + availableTime.getEndTime());
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, timeList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);


        dialogBuilder.setTitle("Book");
        dialogBuilder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String selectedService = serviceSpinner.getSelectedItem().toString();
                AvailableTime availableTime = availableTimeList.get(timeSpinner.getSelectedItemPosition());

                final Booking booking = new Booking(serviceProvider, selectedService, availableTime, username);

                database.child("bookings").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().push().setValue(booking, new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {

                                System.out.println("Value was set. Error = " + error);
                                if (error == null) {
                                    showToast("Booked service");
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

    public void filter(String q, String selectedType) {
        final String query = q.toLowerCase(Locale.getDefault());
        searchResults.clear();
        serviceProviderSet.clear();
        notifyDataSetChanged();

        if (query.length() == 0) {
            for (ServiceProvider serviceProvider : serviceProviders) {
                searchResults.add(serviceProvider);
                serviceProviderSet.add(serviceProvider.getName());
            }
        } else if (selectedType.equals("Service")) {
            for (ServiceProvider serviceProvider : serviceProviders) {
                if (serviceProviderSet.contains(serviceProvider.getName())) continue;
                ArrayList<String> services = serviceProvider.getServices();

                for (String service : services) {
                    if (service.toLowerCase(Locale.getDefault()).contains(query)) {
                        searchResults.add(serviceProvider);
                        serviceProviderSet.add(serviceProvider.getName());
                        break;
                    }
                }
            }
        }
        else if (selectedType.equals("Time")) {
            String queryDay = query.split(" ")[0];
            int queryTime;
            if (query.split(" ").length > 1) {
                String timeString = query.split(" ")[1];

                int hours;
                String hoursString = timeString.split(":")[0];
                if (hoursString.matches("\\d+")) hours = Integer.parseInt(hoursString);
                else return;

                int minutes = 0;
                if (timeString.split(":").length > 1) {
                    String minutesString = timeString.split(":")[1];
                    if (minutesString.matches("\\d+")) minutes = Integer.parseInt(minutesString);
                    else return;
                }


                if (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59)
                    queryTime = hours * 100 + minutes;
                else return;

            } else return;

            for (ServiceProvider serviceProvider : serviceProviders) {
                if (serviceProviderSet.contains(serviceProvider.getName())) continue;
                ArrayList<AvailableTime> availableTimes = serviceProvider.getAvailableTimes();

                for (AvailableTime availableTime : availableTimes) {
                    String day = availableTime.getDay();
                    int start = Integer.parseInt(availableTime.getStartTime().split(":")[0]) * 100 + Integer.parseInt(availableTime.getStartTime().split(":")[1]);
                    int end = Integer.parseInt(availableTime.getEndTime().split(":")[0]) * 100 + Integer.parseInt(availableTime.getEndTime().split(":")[1]);

                    if (queryDay.toLowerCase().equals(day.toLowerCase()) && start <= queryTime && queryTime <= end) {
                        searchResults.add(serviceProvider);
                        serviceProviderSet.add(serviceProvider.getName());
                        break;
                    }
                }
            }
        }
        else if (selectedType.equals("Rating")) {
            for (final ServiceProvider serviceProvider : serviceProviders) {
                if (serviceProviderSet.contains(serviceProvider.getName())) continue;

                database.child("ratings").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        float ratingScore = 0;
                        float ratingCount = 0;
                        if (dataSnapshot.exists()) {
                            for (final DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                                if (ratingSnapshot.getValue(Rating.class).getServiceProvider().getName().equals(serviceProvider.getName())) {
                                    ratingCount++;
                                    ratingScore += ratingSnapshot.getValue(Rating.class).getRating();
                                }
                            }
                        }

                        float average = ratingScore/ratingCount;

                        System.out.println(serviceProvider.getName() + ": " + average);

                        try {
                            float queryFloat = Float.parseFloat(query);

                            if (Float.compare(average, queryFloat) == 0) {
                                searchResults.add(serviceProvider);
                                serviceProviderSet.add(serviceProvider.getName());
                                notifyDataSetChanged();
                            }
                        }
                        catch (NumberFormatException nfe) {
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }

            notifyDataSetChanged();
    }
}