package com.example.aaron.groupprojectseg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

        loadServiceCount = 0;

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        username = sharedPref.getString("username",null);

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
                }
                else {
                    enableEditing(true);
                    editState = true;
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

//    public void getServices(final View view) {
//        services = new ArrayList<>();
//        database.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot serviceSnapshot: dataSnapshot.getChildren()) {
//                    Service service = serviceSnapshot.getValue(Service.class);
//                    services.add(service);
//                }
//
//                loadProfile(view);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

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

                        for (int i=0; i<profileServices.size();i++) {
                            createServiceItem(view);
//                            for (int j=0; j<services.size();j++) {
//                                String name = services.get(j).getName();
//                                String rate = "$" + String.format(Locale.getDefault(), "%.2f", services.get(j).getRate()) + "/hour";
//                                String serviceText = name + " - " + rate;
//
//                                System.out.println("COMPARING: " + profileServices.get(i) + " WITH: " + serviceText);
//
//                                if (profileServices.get(i).equals(serviceText)) {
//                                    System.out.println("MATCH");
//                                    System.out.println("Selecting: " + dropdown.getItemAtPosition(j).toString());
//                                    dropdown.setSelection(j);
//                                }
//                            }
                        }
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
                    for (int j=0; j<services.size();j++) {
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
            String serviceText = ((Spinner)((LinearLayout)v).getChildAt(0)).getSelectedItem().toString();
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
            View v = ((LinearLayout) serviceLayout.getChildAt(i)).getChildAt(0);
            v.setEnabled(enable);
            v = ((LinearLayout) serviceLayout.getChildAt(i)).getChildAt(1);
            v.setEnabled(enable);
        }
        addServiceView.setEnabled(enable);

        if (enable) {
            addServiceView.setVisibility(View.VISIBLE);
            editSaveButton.setText("Save");
        }
        else {
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