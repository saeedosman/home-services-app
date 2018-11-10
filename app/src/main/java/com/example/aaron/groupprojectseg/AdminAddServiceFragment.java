package com.example.aaron.groupprojectseg;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAddServiceFragment extends Fragment {

    private EditText serviceNameBox;
    private EditText rateBox;
    private Button addButton;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_add_service, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        serviceNameBox = view.findViewById(R.id.serviceName);
        rateBox = view.findViewById(R.id.rate);
        addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService();
            }
        });
    }

    public void showToast(String message) {
        Context context = this.getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    public void addService() {
        String name = serviceNameBox.getText().toString();
        String rate = rateBox.getText().toString();

        final Service service = new Service(name, Double.parseDouble(rate));

        database.child("services").orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    showToast("Error: Service with that name exists.");
                }
                else {
                    database.child("services").push().setValue(service, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            String key = ref.getKey();

                            System.out.println("Value was set. Error = "+error);
                            if (error == null) {
                                showToast("Created service");
                                AdminMainActivity adminMainActivity = (AdminMainActivity) getActivity();
                                adminMainActivity.selectDrawerItem(adminMainActivity.nvDrawer.getMenu().getItem(0));
                            }
                            else {
                                showToast("Service creation error: " + error);
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
}