package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ServiceProviderProfileFragment extends Fragment {

    ArrayList<Service> services;
    ArrayList<String> profileServices;
    ArrayList<AvailableTime> availableTimes;

    EditText nameBox;
    EditText phoneNumberBox;
    EditText addressBox;
    EditText descriptionBox;
    Spinner licenseDropdown;
    String username;
    LinearLayout serviceLayout;
    TextView addServiceView;
    ImageView deleteServiceButton;
    Button editSaveButton;

    LinearLayout availabilityLayout;
    TextView addAvailabilityView;

    boolean editState;
    int loadServiceCount;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_provider_profile, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        licenseDropdown = view.findViewById(R.id.licenseSpinner);
        String[] items = new String[]{"Yes", "No"};
        ArrayAdapter<String> licenseAdapter = new ArrayAdapter<>(view.getContext(), R.layout.service_provider_spinner_item, items);
        licenseDropdown.setAdapter(licenseAdapter);

        nameBox = view.findViewById(R.id.companyName);
        phoneNumberBox = view.findViewById(R.id.phoneNumber);
        addressBox = view.findViewById(R.id.address);
        descriptionBox = view.findViewById(R.id.description);
        editSaveButton = view.findViewById(R.id.profileButton);
        serviceLayout = view.findViewById(R.id.serviceLayout);
        addServiceView = view.findViewById(R.id.add);

        availabilityLayout = view.findViewById(R.id.timeTable);
        addAvailabilityView = view.findViewById(R.id.add2);

        loadServiceCount = 0;

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sharedPref.getString("username", null);

        loadProfile(view);

        addServiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editState) {
                    createServiceItem(v);
                }
            }
        });


        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editState) {
                    saveProfile();
                } else {
                    enableEditing(true);
                    editState = true;
                }
            }
        });

        addAvailabilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editState) {
                    addAvailability(v);
                }
            }
        });
    }

    public void createServiceItem(View v) {
        LinearLayout serviceItem = new LinearLayout(v.getContext());
        serviceItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        serviceItem.setOrientation(LinearLayout.HORIZONTAL);

        Spinner spinner = new Spinner(v.getContext());
        spinner.setEnabled(editState);
        serviceItem.addView(spinner);

        final ImageView deleteButton = new ImageView(v.getContext());
        deleteButton.setImageResource(R.drawable.ic_delete);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        deleteButton.setClickable(true);
        deleteButton.setFocusable(true);
        deleteButton.setEnabled(editState);
        serviceItem.addView(deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editState) {
                    LinearLayout spinnerParent = (LinearLayout) deleteButton.getParent();
                    ((LinearLayout) spinnerParent.getParent()).removeView(spinnerParent);
                }
            }
        });


        serviceLayout.addView(serviceItem);
        addServicesToDropdown(v, spinner);


    }

    public void addAvailability(final View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_availability_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner dayDropdown = dialogView.findViewById(R.id.daySpinner);
        String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(v.getContext(), R.layout.service_provider_spinner_item, days);
        dayDropdown.setAdapter(daysAdapter);

        String[] hours = new String[]{"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

        final Spinner startDropdown = dialogView.findViewById(R.id.startSpinner);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(v.getContext(), R.layout.service_provider_spinner_item, hours);
        startDropdown.setAdapter(timeAdapter);

        final Spinner endDropdown = dialogView.findViewById(R.id.endSpinner);
        endDropdown.setAdapter(timeAdapter);

        dialogBuilder.setTitle("Add Timeslot");
//        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String day = dayDropdown.getSelectedItem().toString();
                String start = startDropdown.getSelectedItem().toString();
                String end = endDropdown.getSelectedItem().toString();
//
//                ValidationHelper validationHelper = new ValidationHelper();
//
//                String nameValidation = validationHelper.validateServiceName(name);
//                String rateValidation = validationHelper.validateHourlyRate(rate);
//
//                if (nameValidation != null) {
//                    showToast(nameValidation);
//                    return;
//                }
//
//                if (rateValidation != null) {
//                    showToast(rateValidation);
//                    return;
//                }

                final AvailableTime availableTime = new AvailableTime(day, start, end);
                availableTimes.add(availableTime);
                createAvailableTimeItem(v, availableTime);

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

    public void createAvailableTimeItem(final View view, AvailableTime availableTime) {
        TextView dayView = new TextView(view.getContext());
        dayView.setText(availableTime.getDay() + ": " + availableTime.getStartTime() + " - " + availableTime.getEndTime());
        dayView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        availabilityLayout.addView(dayView);
    }

    public void loadProfile(final View view) {
        database.child("service_providers").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    editState = false;
                    enableEditing(false);

                    for (DataSnapshot serviceProviderSnapshot : dataSnapshot.getChildren()) {
                        ServiceProvider serviceProvider = serviceProviderSnapshot.getValue(ServiceProvider.class);
                        System.out.println("Selected provider: " + serviceProvider.getName());

                        nameBox.setText(serviceProvider.getName());
                        addressBox.setText(serviceProvider.getAddress());
                        phoneNumberBox.setText(serviceProvider.getPhoneNumber());
                        descriptionBox.setText(serviceProvider.getDescription());
                        profileServices = serviceProvider.getServices();
                        availableTimes = serviceProvider.getAvailableTimes();

                        if (profileServices != null) {
                            for (int i = 0; i < profileServices.size(); i++) {
                                createServiceItem(view);
                            }
                        }
                        else profileServices = new ArrayList<>();
                        if (availableTimes != null) {
                            for (int i = 0; i < availableTimes.size(); i++) {
                                createAvailableTimeItem(view, availableTimes.get(i));
                            }
                        }
                        else availableTimes = new ArrayList<>();
                    }
                } else {
                    editState = true;
                    enableEditing(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addServicesToDropdown(final View view, final Spinner dropdown) {
        if (services == null) services = new ArrayList<>();
        database.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (services.size() == 0) {
                    for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                        Service service = serviceSnapshot.getValue(Service.class);
                        services.add(service);
                    }
                }

                ArrayList<String> serviceText = new ArrayList<>();
                for (Service service : services) {
                    String name = service.getName();
                    String rate = "$" + String.format(Locale.getDefault(), "%.2f", service.getRate()) + "/hour";
                    serviceText.add(name + " - " + rate);
                }
                ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(view.getContext(), R.layout.service_provider_spinner_item, serviceText);
                dropdown.setAdapter(serviceAdapter);

                if (loadServiceCount < profileServices.size()) {
                    for (int j = 0; j < services.size(); j++) {
                        String name = services.get(j).getName();
                        String rate = "$" + String.format(Locale.getDefault(), "%.2f", services.get(j).getRate()) + "/hour";
                        String serviceStr = name + " - " + rate;

                        System.out.println("COMPARING: " + profileServices.get(loadServiceCount) + " WITH: " + serviceText);

                        if (profileServices.get(loadServiceCount).equals(serviceStr)) {
                            System.out.println("MATCH");
                            System.out.println("Selecting: " + dropdown.getItemAtPosition(j).toString());
                            dropdown.setSelection(j, true);
                        }
                    }
                    loadServiceCount++;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveProfile() {
        String name = nameBox.getText().toString();
        String address = addressBox.getText().toString();
        String phoneNumber = phoneNumberBox.getText().toString();
        String description = descriptionBox.getText().toString();
        boolean licensed = licenseDropdown.getSelectedItem().toString().equals("Yes");

        ArrayList<String> selectedServices = new ArrayList<>();
        final int childCount = serviceLayout.getChildCount();
        HashSet<String> serviceVisited = new HashSet<>();
        for (int i = 0; i < childCount; i++) {
            View v = serviceLayout.getChildAt(i);
            String serviceText = ((Spinner) ((LinearLayout) v).getChildAt(0)).getSelectedItem().toString();
            if (!serviceVisited.contains(serviceText)) {
                selectedServices.add(serviceText);
                serviceVisited.add(serviceText);
            } else {
                showToast("You can't add the same service twice.");
                return;
            }
        }

        final ServiceProvider serviceProvider = new ServiceProvider(address, phoneNumber, name, description, licensed, username, selectedServices, availableTimes);

        database.child("service_providers").orderByChild("username").equalTo(serviceProvider.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot serviceProviderSnapshot : dataSnapshot.getChildren()) {
                        serviceProviderSnapshot.getRef().setValue(serviceProvider, new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                System.out.println("Value was set. Error = " + error);
                                if (error == null) {
                                    showToast("Saved changes");
                                    enableEditing(false);
                                    editState = false;
                                } else {
                                    showToast("Error: " + error);
                                }
                            }
                        });
                    }
                } else {
                    database.child("service_providers").push().setValue(serviceProvider, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            System.out.println("Value was set. Error = " + error);
                            if (error == null) {
                                showToast("Saved changes");
                                enableEditing(false);
                                editState = false;
                            } else {
                                showToast("Error: " + error);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void enableEditing(boolean enable) {
        nameBox.setEnabled(enable);
        phoneNumberBox.setEnabled(enable);
        addressBox.setEnabled(enable);
        descriptionBox.setEnabled(enable);
        licenseDropdown.setEnabled(enable);

        for (int i = 0; i < serviceLayout.getChildCount(); i++) {
            View v = ((LinearLayout) serviceLayout.getChildAt(i)).getChildAt(0);
            v.setEnabled(enable);
            v = ((LinearLayout) serviceLayout.getChildAt(i)).getChildAt(1);
            v.setEnabled(enable);
        }
        addServiceView.setEnabled(enable);
        addAvailabilityView.setEnabled(enable);

        if (enable) {
            addAvailabilityView.setVisibility(View.VISIBLE);
            addServiceView.setVisibility(View.VISIBLE);
            editSaveButton.setText("Save");
        } else {
            addAvailabilityView.setVisibility(View.GONE);
            addServiceView.setVisibility(View.GONE);
            editSaveButton.setText("Edit");
        }

    }

    public void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), message, duration);
        toast.show();
    }


}