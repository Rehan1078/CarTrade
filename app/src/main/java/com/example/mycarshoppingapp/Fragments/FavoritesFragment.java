package com.example.mycarshoppingapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycarshoppingapp.Adapters.FavoritesAdapter;
import com.example.mycarshoppingapp.MainActivity;
import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.R;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private List<CarDataModel> favoriteList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            favoriteList = mainActivity.getFavoriteList();
        }

        adapter = new FavoritesAdapter(getContext(), favoriteList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateFavorites(List<CarDataModel> favorites) {
        favoriteList.clear();
        favoriteList.addAll(favorites);
        adapter.notifyDataSetChanged();
    }
}