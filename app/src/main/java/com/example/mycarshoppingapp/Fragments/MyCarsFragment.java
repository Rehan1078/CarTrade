package com.example.mycarshoppingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.OfflineDatabase.DtHandler;
import com.example.mycarshoppingapp.R;

import java.util.List;

public class MyCarsFragment extends Fragment {

    private ListView listViewCars;
    private DtHandler dtHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cars, container, false);

        listViewCars = view.findViewById(R.id.listViewCars);
        dtHandler = new DtHandler(getContext());

        // Fetch data from the database
        List<CarDataModel> carList = dtHandler.getAllCars();

        // Check if there are cars in the list
        if (carList.isEmpty()) {
            Toast.makeText(getActivity(), "No cars found in the database", Toast.LENGTH_SHORT).show();
        } else {
            // Create an ArrayAdapter to populate the ListView
            Toast.makeText(getActivity(), "Yes Cars found in the database", Toast.LENGTH_SHORT).show();
            ArrayAdapter<CarDataModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, carList);
            listViewCars.setAdapter(adapter);
        }

        return view;
    }
}