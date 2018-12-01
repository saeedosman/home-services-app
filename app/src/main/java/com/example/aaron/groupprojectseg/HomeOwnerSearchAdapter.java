package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public HomeOwnerSearchAdapter(ArrayList<ServiceProvider> serviceProviders, Context context) {
        this.serviceProviders = serviceProviders;
        this.context = context;
        this.searchResults = new ArrayList<>();
        this.searchResults.addAll(serviceProviders);
        this.serviceProviderSet = new HashSet<>();
        for (ServiceProvider serviceProvider : serviceProviders) serviceProviderSet.add(serviceProvider.getName());
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

        TextView itemNameText = (TextView) view.findViewById(R.id.item_name);
        itemNameText.setText(searchResults.get(position).getName());

        ArrayList<String> services = searchResults.get(position).getServices();

        TextView itemServiceText = (TextView) view.findViewById(R.id.item_services);
        StringBuilder serviceTextBuilder = new StringBuilder();
        serviceTextBuilder.append("<b>Services:</b> ");
        for (int i=0; i<services.size(); i++) {
            String service = services.get(i).split(Pattern.quote(" - $"))[0];
            if (i<services.size() - 1) serviceTextBuilder.append(service + ", ");
            else serviceTextBuilder.append(service);
        }
        itemServiceText.setText(Html.fromHtml(serviceTextBuilder.toString()));

        ArrayList<AvailableTime> hours = searchResults.get(position).getAvailableTimes();

        TextView itemHoursText = (TextView) view.findViewById(R.id.item_hours);
        StringBuilder hoursTextBuilder = new StringBuilder();
        hoursTextBuilder.append("<b>Hours:</b> ");
        for (int i=0; i<hours.size(); i++) {
            String hoursText = hours.get(i).getDay() + " " + hours.get(i).getStartTime() + "-" + hours.get(i).getEndTime();
            if (i<hours.size() - 1) hoursTextBuilder.append(hoursText + ", ");
            else hoursTextBuilder.append(hoursText);
        }
        itemHoursText.setText(Html.fromHtml(hoursTextBuilder.toString()));




        return view;
    }


    public void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void filter(String query, String selectedType) {
        query = query.toLowerCase(Locale.getDefault());
        searchResults.clear();
        serviceProviderSet.clear();
        notifyDataSetChanged();

        if (query.length() == 0) {
            for (ServiceProvider serviceProvider : serviceProviders) {
                searchResults.add(serviceProvider);
                serviceProviderSet.add(serviceProvider.getName());
            }
        }
        else if (selectedType.equals("Service")){
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
                    queryTime = hours*100 + minutes;
                else return;

            }
            else return;

            for (ServiceProvider serviceProvider : serviceProviders) {
                if (serviceProviderSet.contains(serviceProvider.getName())) continue;
                ArrayList<AvailableTime> availableTimes = serviceProvider.getAvailableTimes();

                for (AvailableTime availableTime : availableTimes) {
                    String day = availableTime.getDay();
                    int start = Integer.parseInt(availableTime.getStartTime().split(":")[0])*100 + Integer.parseInt(availableTime.getStartTime().split(":")[1]);
                    int end = Integer.parseInt(availableTime.getEndTime().split(":")[0])*100 + Integer.parseInt(availableTime.getEndTime().split(":")[1]);

                    if (queryDay.toLowerCase().equals(day.toLowerCase()) && start <= queryTime && queryTime <= end) {
                        searchResults.add(serviceProvider);
                        serviceProviderSet.add(serviceProvider.getName());
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}