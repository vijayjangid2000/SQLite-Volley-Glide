package com.vijayjangid.cubereum;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyProfileFragment extends Fragment {

    TextView nameTV, phoneAddressTV, addressTV, emailTV, passwordTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

        nameTV = root.findViewById(R.id.nameTV);
        phoneAddressTV = root.findViewById(R.id.phoneTV);
        addressTV = root.findViewById(R.id.addressTV);
        emailTV = root.findViewById(R.id.emailTV);
        passwordTV = root.findViewById(R.id.passwordTV);

        DatabaseHelperClass databaseHelper = new DatabaseHelperClass(getContext());
        String[] data = databaseHelper.getUserDetails(getContext().getSharedPreferences("userStatus", Context.MODE_PRIVATE).getString("email",""));

        nameTV.setText("Name - " + capitalize(data[0]) + " " + capitalize(data[1]));
        phoneAddressTV.setText("Phone +91 " + data[2]);
        addressTV.setText("Address - " + data[3]);
        emailTV.setText("Email - " + data[4]);
        passwordTV.setText("Password - " + data[5]);


        return root;
    }

    String capitalize(String string) {
        // to capitalize the first letter of the name
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}