package com.example.SilverCity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DisplayPrescriptionPhoto extends AppCompatActivity {
    ImageView IV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_prescription_photo);
        IV=(ImageView)findViewById(R.id.display_prescription_imageview);
        Intent intent=getIntent();
        String temp=intent.getStringExtra("temp");
        if(temp!=null){
            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference();
            StorageReference prescriptionref = storageReference1.child("Prescription_uploads").child(temp);
            if(prescriptionref!=null){
                Log.d("temp:",temp);
                Log.d("prescriptionref:","is not null");
                prescriptionref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(IV);
                        //imageView.setImageBitmap(bitmap);
                        Log.d("Test"," Success!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("Test"," Failed!");
                    }
                });
            }
        }
    }
}