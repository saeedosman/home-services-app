package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AdminServiceListFragment extends Fragment {

    ListView listView;
    ArrayList<Service> services;
    AdminServiceListAdapter adapter;
    FloatingActionButton fab;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_service_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fab = view.findViewById(R.id.fab);
        listView = (ListView) view.findViewById(R.id.serviceList);
        listServices();

    }

    public void listServices() {
        services = new ArrayList<>();
        database.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot serviceSnapshot: dataSnapshot.getChildren()) {
                    Service service = serviceSnapshot.getValue(Service.class);
                    services.add(service);
                }
                System.out.println(services.toString());
                adapter = new AdminServiceListAdapter(services, getActivity());
                listView.setAdapter(adapter);

                fab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        adapter.addService(v);
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        Service selectedService = (Service) adapter.getItemAtPosition(position);
                        String name = selectedService.getName();

                        database.child("services").orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    System.out.println(dataSnapshot.toString());
                                    for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                                        Service service = serviceSnapshot.getValue(Service.class);
                                        System.out.println("Selected service: " + service.getName());
                                    }
                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}