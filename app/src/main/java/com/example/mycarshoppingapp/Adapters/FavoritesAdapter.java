package com.example.mycarshoppingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mycarshoppingapp.DetailsofCar;
import com.example.mycarshoppingapp.MainActivity;
import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private Context context;
    private List<CarDataModel> favoriteList;

    public FavoritesAdapter(Context context, List<CarDataModel> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_cars_in_buy_frag, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        CarDataModel car = favoriteList.get(position);
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
            intent.putExtra("description", car.getColor() + ", " + car.getPrice() + ", " + car.getRegisteredIn());
            intent.putExtra("phoneno", car.getPhoneno());
            context.startActivity(intent);
        });

        holder.recImage.setOnClickListener(v -> {
            // Assuming MainActivity implements removeFromFavorites method
            if (context instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.removeFromFavorites(car);
                favoriteList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, favoriteList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recTitle, recDesc;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
        }
    }
}