package com.example.mycarshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mycarshoppingapp.ModelClasses.UserModel;

public class DetailsofCar extends AppCompatActivity {

    private TextView detailTitle, detailDesc;
    private ImageView detailImage;

    UserModel model;
    String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsof_car);
        detailTitle = findViewById(R.id.detailTitle);
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String imageUrl = intent.getStringExtra("imageUrl");
            String description = intent.getStringExtra("description");
            phoneno=intent.getStringExtra("phoneno");

            detailTitle.setText(title);
            detailDesc.setText(description);
            Glide.with(this).load(imageUrl).into(detailImage);
        }

        ImageView img = findViewById(R.id.fabCallSeller);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneno)); // Phoneno is the phone number of the seller
                startActivity(callIntent);
            }
        });



    }

}