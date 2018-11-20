package com.example.aaron.groupprojectseg;

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

    EditText nameBox;
    EditText phoneNumberBox;
    EditText addressBox;
    EditText descriptionBox;
    Spinner licenseDropdown;
    String username;
    Spinner serviceDropdown;
    LinearLayout serviceLayout;
    TextView addView;

    Button button;

    boolean editState;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_provider_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        licenseDropdown = view.findViewById(R.id.licenseSpinner);
        String[] items = new String[]{"Yes", "No"};
        ArrayAdapter<String> licenseAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        licenseDropdown.setAdapter(licenseAdapter);

        serviceDropdown = view.findViewById(R.id.serviceSpinner);
        addServicesToDropdown(view, serviceDropdown);

        nameBox = view.findViewById(R.id.companyName);
        phoneNumberBox = view.findViewById(R.id.phoneNumber);
        addressBox = view.findViewById(R.id.address);
        descriptionBox = view.findViewById(R.id.description);
        button = view.findViewById(R.id.profileButton);
        serviceLayout = view.findViewById(R.id.serviceLayout);
        addView = view.findViewById(R.id.add);


        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sharedPref.getString("username",null);


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
                    }
                }
                else {
                    editState = true;
                    enableEditing(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editState) {
                    Spinner spinner = new Spinner(getActivity());
                    addServicesToDropdown(v, spinner);
                    serviceLayout.addView(spinner);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editState) {
                    saveProfile();
                }
                else {
                    enableEditing(true);
                    editState = true;
                }
            }
        });
    }

    public void addServicesToDropdown(final View view, final Spinner dropdown) {
        services = new ArrayList<>();
        database.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot serviceSnapshot: dataSnapshot.getChildren()) {
                    Service service = serviceSnapshot.getValue(Service.class);
                    services.add(service);
                }

                ArrayList<String> serviceText = new ArrayList<>();
                for (Service service : services) {
                    String name = service.getName();
                    String rate = "$" + String.format(Locale.getDefault(), "%.2f", service.getRate()) + "/hour";
                    serviceText.add(name + " - " + rate);
                }
                ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, serviceText);
                dropdown.setAdapter(serviceAdapter);

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
            String serviceText = ((Spinner)v).getSelectedItem().toString();
            if (!serviceVisited.contains(serviceText)) {
                selectedServices.add(serviceText);
                serviceVisited.add(serviceText);
            }
            else {
                showToast("You can't add the same service twice.");
                return;
            }
        }

        final ServiceProvider serviceProvider = new ServiceProvider(address, phoneNumber, name, description, licensed, username, selectedServices);

        database.child("service_providers").orderByChild("username").equalTo(serviceProvider.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
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
                }
                else {
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
            View v = serviceLayout.getChildAt(i);
            v.setEnabled(enable);
        }
        addView.setEnabled(enable);
        if (enable) button.setText("Save");
        else button.setText("Edit");

    }

    public void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), message, duration);
        toast.show();
    }




}