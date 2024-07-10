package com.example.mycarshoppingapp.Fragments;

import static java.security.AccessController.getContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.mycarshoppingapp.Adapters.CarAdapter;
import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BuyFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<CarDataModel> carList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBuyFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext()));

        carList = new ArrayList<>();
        carAdapter = new CarAdapter(getContext(), carList);
        recyclerView.setAdapter(carAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Cars");
        fetchCarData();

        return view;
    }

    private void fetchCarData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CarDataModel car = dataSnapshot.getValue(CarDataModel.class);
                    carList.add(car);
                }
                carAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

}