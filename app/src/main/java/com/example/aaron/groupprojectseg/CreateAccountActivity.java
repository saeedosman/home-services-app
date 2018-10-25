package com.example.aaron.groupprojectseg;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText firstNameBox;
    private EditText lastNameBox;
    private EditText usernameBox;
    private EditText passwordBox;
    private EditText emailBox;
    private RadioGroup radioGroup;
    private Button createButton;


    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firstNameBox = findViewById(R.id.firstName);
        lastNameBox = findViewById(R.id.lastName);
        usernameBox = findViewById(R.id.username);
        passwordBox = findViewById(R.id.password);
        emailBox = findViewById(R.id.email);
        radioGroup = findViewById(R.id.accountType);
        createButton = findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAccount();
            }
        });
    }

    public void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    public void newAccount() {
        String email = emailBox.getText().toString();
        String username = usernameBox.getText().toString();
        String password = passwordBox.getText().toString();
        String firstName = firstNameBox.getText().toString();
        String lastName = lastNameBox.getText().toString();

        ValidationHelper validationHelper = new ValidationHelper();

        String firstNameValidation = validationHelper.validateFirstName(firstName);
        String lastNameValidation = validationHelper.validateLastName(lastName);
        String emailValidation = validationHelper.validateEmail(email);
        String usernameValidation = validationHelper.validateUsername(username);
        String passwordValidation = validationHelper.validatePassword(password);

        if (firstNameValidation != null) {
            showToast(firstNameValidation);
            return;
        }
        if (lastNameValidation != null) {
            showToast(lastNameValidation);
            return;
        }
        if (emailValidation != null) {
            showToast(emailValidation);
            return;
        }
        if (usernameValidation != null) {
            showToast(usernameValidation);
            return;
        }
        if (passwordValidation != null) {
            showToast(passwordValidation);
            return;
        }

        int selectId = radioGroup.getCheckedRadioButtonId();
        RadioButton selected = (RadioButton) radioGroup.findViewById(selectId);
        String selectedType = (String) selected.getText();

        final Account account = new Account(firstName, lastName, email, username, password, selectedType);

        if (selectedType.equals("Admin")) {
            database.child("accounts").orderByChild("type").equalTo("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        System.out.println("Admin account already exists");

                        showToast("Error: Admin account already exists");
                    }
                    else {
                        System.out.println("Admin account doesn't exist.");

                        database.child("accounts").orderByChild("username").equalTo(usernameBox.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    showToast("Error: Account with that username exists.");
                                }
                                else {
                                    database.child("accounts").push().setValue(account, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                                            System.out.println("Value was set. Error = "+error);


                                            if (error == null) {
                                                showToast("Account created");
                                                finish();
                                            }
                                            else {
                                                showToast("Account creation error: " + error);
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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else {
            database.child("accounts").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        showToast("Error: Account with that username exists.");
                    }
                    else {
                        database.child("accounts").push().setValue(account, new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                System.out.println("Value was set. Error = "+error);


                                if (error == null) {
                                    showToast("Account created");
                                    finish();
                                }
                                else {
                                    showToast("Account creation error: " + error);
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


}
