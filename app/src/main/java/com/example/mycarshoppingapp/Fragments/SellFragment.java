package com.example.mycarshoppingapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mycarshoppingapp.ModelClasses.CarDataModel;
import com.example.mycarshoppingapp.ModelClasses.UserModel;
import com.example.mycarshoppingapp.OfflineDatabase.DtHandler;
import com.example.mycarshoppingapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellFragment extends Fragment {
    Button uploadButton;
    ImageView uploadImage;
    EditText model, enginecapacity, bodytype, registered_in, color, price;
    ProgressBar progressBar;
    Uri selectedImageUri;  // Variable to store selected image URI
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cars");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String userPhoneNumber;
    String userId;

    DtHandler dtHandler;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        uploadButton = view.findViewById(R.id.saveButton);
        uploadImage = view.findViewById(R.id.Uploadimage);
        model = view.findViewById(R.id.editTextModel);
        enginecapacity = view.findViewById(R.id.editTextEngineCapacity);
        bodytype = view.findViewById(R.id.editTextBodyType);
        registered_in = view.findViewById(R.id.editTextRegisteredIn);
        color = view.findViewById(R.id.editTextColor);
        progressBar = view.findViewById(R.id.progress_bar);
        price = view.findViewById(R.id.editTextPrice);
        progressBar.setVisibility(View.INVISIBLE);
        dtHandler = new DtHandler(getContext());





        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.getUid())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserModel userModel = document.toObject(UserModel.class);
                                if (userModel != null) {
                                    userPhoneNumber = userModel.getPhone();
                                    userId=userModel.getUserId();
                                }
                            }
                        }
                    });
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            Intent data = o.getData();
                            Uri imageUri = data.getData();
                            selectedImageUri = imageUri; // Set the selected image URI
                            displaySelectedImage(imageUri); // Display the selected image on ImageView
                        } else {
                            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent();
                photopicker.setAction(Intent.ACTION_GET_CONTENT);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    uploadImage(); // Call method to upload the image
                } else {
                    Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);
        String fileExtension = getFileExtension(selectedImageUri);
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + fileExtension);


        fileRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Image uploaded, proceed to create CarDataModel and save to database
                                saveCarDataToDatabase(downloadUri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot snapshot) {
                        // Handle progress if needed
                    }
                });
    }

    private void saveCarDataToDatabase(String imageUrl) {
        String modelText = model.getText().toString();
        double engineCapacityValue = Double.parseDouble(enginecapacity.getText().toString());
        String bodyTypeText = bodytype.getText().toString();
        String registeredInText = registered_in.getText().toString();
        String colorText = color.getText().toString();
        String priceEntered = price.getText().toString(); // Get the price entered by the user

        // Assuming you have a CarDataModel constructor that accepts image URL and phone number
        CarDataModel carData = new CarDataModel(modelText, engineCapacityValue, bodyTypeText,
                registeredInText, colorText, imageUrl, priceEntered, userPhoneNumber,userId);

        databaseReference.push().setValue(carData).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                sendNotification("New Car Added", "A new car has been added to the inventory.");
                dtHandler.addCar(carData);
                clearInputsAndUI();
            } else {
                Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void showLocalNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "channel_id")
                .setSmallIcon(R.drawable.my_cars)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(1, builder.build());
    }

    private void sendNotification(String title, String message) {
        // Create the notification payload
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("body", message);

        // Get the FCM token of the device
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String fcmToken = task.getResult();

                        // Send the notification to the device with the FCM token
                        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(fcmToken)
                                .setMessageId(UUID.randomUUID().toString())
                                .setData(notificationData)
                                .build());

                        // Optionally, you can also display a local notification
                        showLocalNotification(title, message);
                    }
                });
    }

    private String getFileExtension(Uri fileuri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileuri));
    }

    private void clearInputsAndUI() {
        model.setText(null);
        enginecapacity.setText(null);
        bodytype.setText(null);
        registered_in.setText(null);
        color.setText(null);
        price.setText(null);
        uploadImage.setImageResource(R.drawable.upload_video); // Set a placeholder image
        selectedImageUri = null; // Clear the selected image URI
    }

    private void displaySelectedImage(Uri imageUri) {
        uploadImage.setImageURI(imageUri);
    }
}