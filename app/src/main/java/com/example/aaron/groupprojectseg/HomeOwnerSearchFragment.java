package com.example.aaron.groupprojectseg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeOwnerSearchFragment extends Fragment {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ListView list;
    HomeOwnerSearchAdapter adapter;
    ArrayList<ServiceProvider> serviceProviders;
    SearchView search;
    RadioGroup searchGroup;
    RadioButton serviceButton;
    RadioButton timeButton;
    RadioButton ratingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_owner_search, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        serviceProviders = new ArrayList<>();
        search = view.findViewById(R.id.search);
        searchGroup = view.findViewById(R.id.searchGroup);
        serviceButton = view.findViewById(R.id.serviceButton);
        timeButton = view.findViewById(R.id.timeButton);
        ratingButton = view.findViewById(R.id.ratingButton);


        searchGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = view.findViewById(checkedId);

                if (checked.getText().equals("Time")) {
                    search.setQueryHint("Example: Monday 3:00");
                }
                if (checked.getText().equals("Service")) {
                    search.setQueryHint("Example: Flooring");
                }
                if (checked.getText().equals("Rating")) {
                    search.setQueryHint("Example: 5");
                }
            }
        });

        searchGroup.check(R.id.serviceButton);


        database.child("service_providers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot serviceProviderSnapshot: dataSnapshot.getChildren()) {
                    ServiceProvider serviceProvider = serviceProviderSnapshot.getValue(ServiceProvider.class);
                    serviceProviders.add(serviceProvider);
                }

                list = (ListView) view.findViewById(R.id.resultList);
                adapter = new HomeOwnerSearchAdapter(serviceProviders, getActivity());
                list.setAdapter(adapter);

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        int selectId = searchGroup.getCheckedRadioButtonId();
                        RadioButton selected = (RadioButton) view.findViewById(selectId);
                        final String selectedType = (String) selected.getText();

                        adapter.filter(query, selectedType);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), message, duration);
        toast.show();
    }




}