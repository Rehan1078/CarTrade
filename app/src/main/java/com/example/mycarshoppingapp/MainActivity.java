package com.example.mycarshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.mycarshoppingapp.Fragments.FavoritesFragment;
import com.example.mycarshoppingapp.Fragments.BuyFragment;
import com.example.mycarshoppingapp.Fragments.MyCarsFragment;
import com.example.mycarshoppingapp.Fragments.SellFragment;
import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.OfflineDatabase.DtHandler;
import com.example.mycarshoppingapp.Registerationprocess.EnterNumber;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MeowBottomNavigation bnv;
    private List<CarDataModel> favoriteList = new ArrayList<>();
    private FavoritesFragment favoritesFragment;
    private BuyFragment buyFragment;
    private SellFragment sellFragment;

    DtHandler dtHandler = new DtHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadFavoritesFromSharedPreferences();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new BuyFragment();
                } else if (item.getItemId() == R.id.nav_share) {
                   shareApp();
                   return  true;
                } else if(item.getItemId() == R.id.nav_mycars){
//                    Toast.makeText(MainActivity.this, "Mycars", Toast.LENGTH_SHORT).show();
                    selectedFragment = new MyCarsFragment();
                }



                else if (item.getItemId() == R.id.nav_about) {
                    // Show about us dialog
                    showAboutUsDialog();
                    return true; // Return true to prevent loading a fragment
                } else if (item.getItemId() == R.id.nav_logout) {
                    // Handle logout here
                    showLogoutConfirmationDialog();
                    return true;
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            private void showLogoutConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle logout
                        Intent intent = new Intent(MainActivity.this, EnterNumber.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
                builder.show();
            }


            private void shareApp() {
                // Share text using Intent
                String shareText = "Check out this amazing app!";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                // Create a chooser dialog to let the user choose where to share
                Intent chooserIntent = Intent.createChooser(shareIntent, "Share via");

                // Check if there are apps available to handle the share intent
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooserIntent);
                } else {
                    Toast.makeText(MainActivity.this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }


            private void showAboutUsDialog() {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String aboutUsMessage = "Welcome to JuttAutos! We specialize in " +
                        "buying and selling cars with ease and efficiency. Our mission is " +
                        "to provide you with a seamless and trustworthy experience, ensuring you " +
                        "find the perfect car or sell your vehicle at the best price. Thank you for " +
                        "choosing JuttAutos for all your automotive needs.";
                editor.putString("aboutUsMessage", aboutUsMessage);
                editor.apply();
//                boolean bb = editor.commit();
//                if(bb==true){
//
//                }

                /////////////////////////////////////////////////////////
                String aboutMessage = sharedPreferences.getString("aboutUsMessage", "Default About Us message");
                /////////////////////////////////////////////////////////


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About Us");
                builder.setMessage(aboutMessage);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bnv = findViewById(R.id.bnv);
        bnv.add(new MeowBottomNavigation.Model(1, R.drawable.cart));
        bnv.add(new MeowBottomNavigation.Model(2, R.drawable.add_));
        bnv.add(new MeowBottomNavigation.Model(3, R.drawable.add_to_favourite));

        buyFragment = new BuyFragment();
        sellFragment = new SellFragment();
        favoritesFragment = new FavoritesFragment();

        loadFragment(buyFragment);
        bnv.show(1, true);

        bnv.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment selectedFragment = null;

                switch (model.getId()) {
                    case 1:
                        selectedFragment = buyFragment;
                        break;
                    case 2:
                        selectedFragment = sellFragment;
                        break;
                    case 3:
                        selectedFragment = favoritesFragment;
                        break;
                }

                loadFragment(selectedFragment);
                return null;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, buyFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void saveFavoritesToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        editor.putString("favoriteList", json);
        editor.apply();
    }



    // Add this method to load the favorite list from SharedPreferences
    private void loadFavoritesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favoriteList", null);
        Type type = new TypeToken<ArrayList<CarDataModel>>() {}.getType();
        favoriteList = gson.fromJson(json, type);

        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
    }

    public List<CarDataModel> getFavoriteList() {
        return favoriteList;
    }

    public void addToFavorites(CarDataModel car) {
        if (!favoriteList.contains(car)) {
            favoriteList.add(car);
            saveFavoritesToSharedPreferences(); // Save updated favorite list
            updateFavoritesFragment();
        }
    }

    public void removeFromFavorites(CarDataModel car) {
        if (favoriteList.contains(car)) {
            favoriteList.remove(car);
            saveFavoritesToSharedPreferences(); // Save updated favorite list
            updateFavoritesFragment();
        }
    }

    private void updateFavoritesFragment() {
        if (favoritesFragment != null && favoritesFragment.isAdded()) {
            favoritesFragment.updateFavorites(favoriteList);
        }
    }

    public boolean isFavorite(CarDataModel car) {
        return favoriteList.contains(car);
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}