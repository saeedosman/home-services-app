package com.example.aaron.groupprojectseg;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView firstNameView = findViewById(R.id.firstNameView);
        TextView typeView = findViewById(R.id.typeView);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String firstName = sharedPref.getString("firstName", null);
        String accountType = sharedPref.getString("accountType", null);

        firstNameView.setText(firstName);
        typeView.setText(accountType);
    }
}