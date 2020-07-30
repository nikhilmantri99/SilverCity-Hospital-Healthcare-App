package com.example.SilverCity.DoctorUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.SilverCity.R;
import com.example.SilverCity.models.Consultation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddConsultationDialog extends AppCompatDialogFragment {
    private static final int pic_id = 123;
    private EditText disease, price, date, prescription;
    private Uri mImageUri=null;
    private Button addConsultationButton,prescription_upload_button;
    StorageReference mStorageReference;
    private String doctorName, doctorEmail, patientEmail;
    Intent prescriptionimage=null;
    public byte[] data1;
    String prescription_image_name=null;

    private String getExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public AddConsultationDialog(String doctorName, String doctorEmail, String patientEmail)
    {
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_consultation_dialog, null);
        disease = view.findViewById(R.id.disease);
        price = view.findViewById(R.id.price);
        date = view.findViewById(R.id.date);
        prescription = view.findViewById(R.id.prescription);

        prescription_upload_button = (Button)view.findViewById(R.id.prescription_upload_button);
        prescription_upload_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
//                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(camera_intent, pic_id);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Log.d("debug0","selecting from gallery");
                startActivityForResult(intent, pic_id);

//                Intent camera_intent = new Intent();
//                camera_intent.setType("image/*");
//                camera_intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //camera_intent.setType("image/*");

                // Start the activity with camera_intent,
                // and request pic idi

//                startActivityForResult(camera_intent, pic_id);
            }
        });

        addConsultationButton = view.findViewById(R.id.addConsultationButton);
        addConsultationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(prescription.getText())){
                    prescription.setText("None");
                }
                if (TextUtils.isEmpty(prescription.getText()) || TextUtils.isEmpty(disease.getText()) || TextUtils.isEmpty(price.getText())) {
                    Toast.makeText(getActivity(), "A compulsory field is empty", Toast.LENGTH_SHORT).show();
                } else {
                        if(mImageUri==null){
                            Log.d("mImageUri","is null");
                        }
                        if(mImageUri!=null){
                            Log.d("mImageUri","not null");
                            mStorageReference = FirebaseStorage.getInstance().getReference("Prescription_uploads");
//                            prescription_image_name=doctorEmail+"."+patientEmail+date.getText().toString().replaceAll("/","_")+price.getText().toString().replaceAll(" ","_")+".png";
                            prescription_image_name=System.currentTimeMillis()+"."+getExtension(mImageUri);
                            Log.d("prescription_image_name",prescription_image_name);
                            StorageReference ref1 = mStorageReference.child(prescription_image_name);
                            ref1.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("debug4","just uploaded");
                                    //Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    Consultation consultation = new Consultation(doctorName, doctorEmail, patientEmail, disease.getText().toString(), date.getText().toString(),
                            price.getText().toString()+" Rs", prescription.getText().toString());
                    if(prescription_image_name!=null){
                        consultation.set_prescription_image_name(prescription_image_name);
                    }

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Consultations");
                    ref.push().setValue(consultation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Add consultation");
                                alertDialog.setContentText("Consultation added successfully !");
                                alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    });
                }

            }
        });
        builder.setView(view);
        return builder.create();
    }

    // This method will help to retrieve the image
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        // Match the request 'pic id with requestCode
        if (requestCode == pic_id   && data != null) {

            // BitMap is data structure of image file
            // which stores the image in memory
//            Bitmap photo = (Bitmap)data.getExtras().get("data");
//            int nh = (int) ( photo.getHeight() * (512.0 / photo.getWidth()) );
//            photo = Bitmap.createScaledBitmap(photo, 512, nh, true);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            data1 = baos.toByteArray();
//            prescription_upload_button.setText("Photo Taken.");
//            Log.d("here now","data1 assigned ");
            mImageUri=data.getData();
            if(mImageUri==null){
                Log.d("MIMAGEURI","is null");
            }
            else{
                Log.d("MIMAGEURI","is not a null value");
            }
            Log.d("here now","mImageUri assigned ");
            prescription_upload_button.setText("Photo Taken.");
            prescription_upload_button.setEnabled(false);
        }
    }
}
