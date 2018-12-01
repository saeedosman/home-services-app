package com.example.aaron.groupprojectseg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private EditText usernameBox;
    private EditText passwordBox;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameBox = (EditText) findViewById(R.id.username);
        passwordBox = (EditText) findViewById(R.id.password);
    }


    public void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void login(View view) {
        System.out.println("Logging in...");

        if (usernameBox.getText().toString().length()==0) {
            showToast("Username field is empty");
            return;
        }
        if (passwordBox.getText().toString().length()==0) {
            showToast("Password field is empty");
            return;
        }
        //after the password has been shown not to be empty
        //has it so it can be verified against the hashed password save on the database
        //password encryption after it has been validated
        SHAHashing stringHash = new SHAHashing();

        try {
            password = stringHash.hashPassword(passwordBox.getText().toString());
        } catch(NoSuchAlgorithmException e){
            System.out.println(e);
        }

        database.child("accounts").orderByChild("username").equalTo(usernameBox.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    for (DataSnapshot accountSnapshot: dataSnapshot.getChildren()) {
                        Account account = accountSnapshot.getValue(Account.class);
                        System.out.println(account.getUsername());
                        System.out.println(account.getPassword());
                        if (account.getPassword().equals(password)) {
                            successfulLogin(account);
                        }
                        else {
                            System.out.println("Wrong password.");
                            showToast("Wrong password");
                        }

                    }
                }

                else
                {
                    System.out.println("Username doesn't exist.");
                    showToast("Username doesn't exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void successfulLogin(Account account) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("firstName", account.getFirstName());
        editor.putString("lastName", account.getLastName());
        editor.putString("username", account.getUsername());
        editor.putString("password", account.getPassword());
        editor.putString("email", account.getEmail());
        editor.putString("accountType", account.getType());
        editor.apply();

        showToast("Logged in successfully");

        Intent intent;

        if (account.getType().equals("Admin")) intent = new Intent(this, AdminMainActivity.class);
        else if (account.getType().equals("Service Provider")) intent = new Intent(this, ServiceProviderMainActivity.class);
        else intent = new Intent(this, HomeOwnerMainActivity.class);

        this.startActivity(intent);
        finish();
    }

    public void clickCreateNewAccount(View view){
        Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(createAccountIntent);
    }
}
