package com.example.aaron.groupprojectseg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class AdminServiceListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Service> services = new ArrayList<Service>();
    private Context context;



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
            String rate = "$" + String.format(Locale.getDefault() ,"%.2f", service.getRate()) + "/hour";
            serviceText.add(name + " - " + rate);
        }

        //Handle TextView and display string from your services
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(serviceText.get(position));

        //Handle buttons and add onClickListeners
        ImageView deleteBtn = (ImageView)view.findViewById(R.id.delete_btn);
        ImageView editBtn = (ImageView)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                System.out.println("removed item: " + getItem(position));
//                services.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
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