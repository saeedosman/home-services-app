package com.example.aaron.groupprojectseg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView firstNameView = view.findViewById(R.id.fragmentFirstNameView);
        TextView typeView = view.findViewById(R.id.fragmentTypeView);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String firstName = sharedPref.getString("firstName", null);
        String accountType = sharedPref.getString("accountType", null);

        firstNameView.setText(firstName);
        typeView.setText(accountType);

    }


}