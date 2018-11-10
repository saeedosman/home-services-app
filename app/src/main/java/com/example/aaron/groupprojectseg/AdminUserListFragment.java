package com.example.aaron.groupprojectseg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUserListFragment extends Fragment {

    ArrayList<Account> accounts;
    ListView listView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_user_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.userList);
        listAccounts();

    }

    public void listAccounts() {
        accounts = new ArrayList<>();
        database.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot accountSnapshot: dataSnapshot.getChildren()) {
                        Account account = accountSnapshot.getValue(Account.class);
                        accounts.add(account);
                    }

                    System.out.println(accounts.toString());
                    ArrayList<String> accountList = new ArrayList<>();
                    for (Account acc : accounts) {
                        accountList.add(acc.getUsername() + " (" + acc.getType() + ")");
                    }
                    listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, accountList));
                }

                else
                {
                    System.out.println("Username doesn't exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}