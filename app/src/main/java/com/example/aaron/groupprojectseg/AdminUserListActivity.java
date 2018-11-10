package com.example.aaron.groupprojectseg;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminUserListActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

        TextView firstNameView = findViewById(R.id.firstNameView);
        TextView typeView = findViewById(R.id.typeView);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String firstName = sharedPref.getString("firstName", null);
        String accountType = sharedPref.getString("accountType", null);

        firstNameView.setText(firstName);
        typeView.setText(accountType);

        if (accountType.equals("Admin")) {
            LinearLayout homeBox = findViewById(R.id.homeBox);
            TextView accountViewTitle = new TextView(this);
            String text = "List of Registered Accounts:";
            accountViewTitle.setText(text);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,10);
            accountViewTitle.setLayoutParams(params);
            homeBox.addView(accountViewTitle);

            getAccounts();
        }

    }

    public void getAccounts() {
        database.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    for (DataSnapshot accountSnapshot: dataSnapshot.getChildren()) {
                        Account account = accountSnapshot.getValue(Account.class);
                        addAccountToList(account);
                    }
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

    public void addAccountToList(Account account) {
        LinearLayout homeBox = findViewById(R.id.homeBox);
        TextView accountView = new TextView(this);
        String text = account.getFirstName() + " " + account.getLastName() + " (" + account.getUsername() + ") - " + account.getType();
        accountView.setText(text);
        accountView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        homeBox.addView(accountView);

    }

}
