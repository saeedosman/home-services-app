package com.example.aaron.groupprojectseg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                System.out.println("modified item: " + getItem(position));
                notifyDataSetChanged();
            }
        });

        return view;
    }
}