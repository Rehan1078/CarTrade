package com.example.mycarshoppingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mycarshoppingapp.DetailsofCar;
import com.example.mycarshoppingapp.MainActivity;
import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.ModelClasses.UserModel;
import com.example.mycarshoppingapp.R;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Context context;
    private List<CarDataModel> carList;

    public CarAdapter(Context context, List<CarDataModel> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_cars_in_buy_frag, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarDataModel car = carList.get(position);
        holder.recTitle.setText(car.getModel());
        holder.recDesc.setText(car.getPrice());

        // Use Glide to load images
        Glide.with(context)
                .load(car.getImageUrl())
                .into(holder.recImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsofCar.class);
            intent.putExtra("title", car.getModel());
            intent.putExtra("imageUrl", car.getImageUrl());
            intent.putExtra("description", car.getColor() + "\n " + car.getPrice() + "\n" + car.getRegisteredIn());
            intent.putExtra("phoneno", car.getPhoneno());
            context.startActivity(intent);
        });

        MainActivity mainActivity = (MainActivity) context;

        // Set initial state of favorite icon
        if (mainActivity.isFavorite(car)) {
            holder.recFavourite.setImageResource(R.drawable.added_to_favourite);
        } else {
            holder.recFavourite.setImageResource(R.drawable.add_to_favourite);
        }

        holder.recFavourite.setOnClickListener(v -> {
            if (mainActivity.isFavorite(car)) {
                mainActivity.removeFromFavorites(car);
                holder.recFavourite.setImageResource(R.drawable.add_to_favourite);
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                mainActivity.addToFavorites(car);
                holder.recFavourite.setImageResource(R.drawable.added_to_favourite);
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage, recFavourite;
        TextView recTitle, recDesc;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            recFavourite = itemView.findViewById(R.id.imageviewfavourite);
        }
    }
}