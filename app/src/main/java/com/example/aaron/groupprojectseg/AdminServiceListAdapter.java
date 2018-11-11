package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AdminServiceListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Service> services = new ArrayList<Service>();
    private Context context;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public AdminServiceListAdapter(ArrayList<Service> services, Context context) {
        this.services = services;
        this.context = context;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int pos) {
        return services.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.admin_service_list_item, null);
        }

        ArrayList<String> serviceText = new ArrayList<>();
        for (Service service : services) {
            String name = service.getName();
            String rate = "$" + String.format(Locale.getDefault(), "%.2f", service.getRate()) + "/hour";
            serviceText.add(name + " - " + rate);
        }

        //Handle TextView and display string from your services
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(serviceText.get(position));

        //Handle buttons and add onClickListeners
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.delete_btn);
        ImageView editBtn = (ImageView) view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                deleteService(v);

            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            editService(v);
                System.out.println("modified item: " + getItem(position));
            }
        });

        return view;
    }

    public void deleteService(View v) {
        View parentRow = (View) v.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        Service service = (Service) getItem(position);

        database.child("services").orderByChild("name").equalTo(service.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println(dataSnapshot.toString());
                    for (final DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                        final Service serviceToDelete = serviceSnapshot.getValue(Service.class);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setTitle("Delete");
                        builder.setMessage("Are you sure you want to delete this service?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                serviceSnapshot.getRef().removeValue();
                                services.remove(position);
                                notifyDataSetChanged();
                                System.out.println("Deleted service: " + serviceToDelete.getName());
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editService(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.admin_edit_service_dialog, null);
        dialogBuilder.setView(dialogView);

        View parentRow = (View) v.getParent().getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        final Service service = (Service) getItem(position);

        final EditText nameBox = (EditText) dialogView.findViewById(R.id.name);
        final EditText rateBox = (EditText) dialogView.findViewById(R.id.rate);

        nameBox.setText(service.getName());
        rateBox.setText(String.format(Locale.getDefault(), "%.2f", service.getRate()));

        dialogBuilder.setTitle("Edit");
//        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = nameBox.getText().toString();
                String rate = rateBox.getText().toString();

                ValidationHelper validationHelper = new ValidationHelper();

                String nameValidation = validationHelper.validateServiceName(name);
                String rateValidation = validationHelper.validateHourlyRate(rate);

                if (nameValidation != null) {
                    showToast(nameValidation);
                    return;
                }

                if (rateValidation != null) {
                    showToast(rateValidation);
                    return;
                }

                final Service updatedService = new Service(name, Double.parseDouble(rate));

                services.set(position, updatedService);

                database.child("services").orderByChild("name").equalTo(service.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            System.out.println(dataSnapshot.toString());
                            for (final DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                                serviceSnapshot.getRef().setValue(updatedService);
                                notifyDataSetChanged();
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
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void addService(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.admin_add_service_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText nameBox = (EditText) dialogView.findViewById(R.id.name);
        final EditText rateBox = (EditText) dialogView.findViewById(R.id.rate);

        dialogBuilder.setTitle("Add");
//        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = nameBox.getText().toString();
                String rate = rateBox.getText().toString();

                ValidationHelper validationHelper = new ValidationHelper();

                String nameValidation = validationHelper.validateServiceName(name);
                String rateValidation = validationHelper.validateHourlyRate(rate);

                if (nameValidation != null) {
                    showToast(nameValidation);
                    return;
                }

                if (rateValidation != null) {
                    showToast(rateValidation);
                    return;
                }

                final Service service = new Service(name, Double.parseDouble(rate));


                database.child("services").orderByChild("name").equalTo(service.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            showToast("Error: Service with that name exists.");
                        }
                        else {
                            database.child("services").push().setValue(service, new DatabaseReference.CompletionListener() {
                                public void onComplete(DatabaseError error, DatabaseReference ref) {
                                    services.add(service);
                                    notifyDataSetChanged();

                                    System.out.println("Value was set. Error = "+error);
                                    if (error == null) {
                                        showToast("Created service");
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
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}