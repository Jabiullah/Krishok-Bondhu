package com.example.krishokbondhuuser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NIDImageTake extends AppCompatActivity {

    private TextView textVmsgFront,textVmsgBack;
    private ImageView FrontImg,BackImg;
    private Button btnProgressInfoNID;

    private static final int CAMERA_REQUEST_CODE = 1;
    private File imageFile;
    private Uri imageUri;
    private Boolean checkFront = true , checkBack=true;

    String getFrontValue=" ", getBackValue =" ";
    String nidNo = "";
    String phone = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nidimage_take);

        textVmsgFront  = findViewById(R.id.msg);
        textVmsgBack   = findViewById(R.id.msg2);

        FrontImg = findViewById(R.id.NID_front);
        BackImg  = findViewById(R.id.NID_back);

        btnProgressInfoNID = findViewById(R.id.NIDInfoProgress);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");

        }

        //permission
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);


        btnProgressInfoNID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),""+nidNo+"\n Phone no."+phone,Toast.LENGTH_SHORT).show();
                Bundle bundleReg = new Bundle();
                bundleReg.putString("phone",phone);
                bundleReg.putString("nidNo",nidNo);


                Intent intentReg = new Intent(NIDImageTake.this, SuccessRegistration.class);
                intentReg.putExtras(bundleReg);
                startActivity(intentReg);
                finish();
            }
        });
    }
    private String TextProcessing(Bitmap bitmap){
        try {
            // String stringFileName = "/storage/emulated/0/Download/test.jpg";
            // bitmap = BitmapFactory.decodeFile(stringFileName);
            // imgView.setImageBitmap(bitmap);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
            Frame frameImage = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frameImage);
            String stringImage = "";
            for(int i=0 ; i<textBlockSparseArray.size();i++){
                TextBlock textBlock = textBlockSparseArray.get(textBlockSparseArray.keyAt(i));
                stringImage = stringImage + " "+ textBlock.getValue();
            }
            return stringImage; // output of Text
        }catch (Exception e){
            return ""+e;        // error !
        }
    }


    public void ImageTake(View view) {
        // camera intent search
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create a file to store the image
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // The image is saved in the file specified by imageFile
            if (imageFile != null) {
                // Load the full-sized image into the ImageView
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                if(checkFront==true){
                    FrontImg.setImageBitmap(bitmap);
                    FrontImg.setEnabled(false); // disable on touch function
                    textVmsgFront.setText("");  // clean msg

                    checkFront=false;

                    getFrontValue = TextProcessing(bitmap);

                }else{
                    BackImg.setImageBitmap(bitmap);
                    BackImg.setEnabled(false); // disable on touch function
                    textVmsgBack.setText("");  // clean msg

                    getBackValue = TextProcessing(bitmap);

                    checkBack=false;
                }
                if(checkFront == false && checkBack == false){
                    // code for check NID
                    try{
                        String[] parts = getFrontValue.split("\\b(?i)(name|date of birth|nid no)\\b");
                        nidNo = parts[3].trim().replaceAll("[^0-9]", "");
                        if(nidNo.length()>0){
                            btnProgressInfoNID.setEnabled(true);
                            btnProgressInfoNID.requestFocus();
                        }

                    }catch (Exception e){
                        // can not detect text from the image
                        FrontImg.setImageDrawable(null); // clean imageView
                        FrontImg.setEnabled(true); // Enable on touch function
                        BackImg.setImageDrawable(null);
                        BackImg.setEnabled(true); // Enable on touch function

                        textVmsgFront.setText("NID এর সামনের ছবি তুলুন");  // Adding msg
                        textVmsgBack.setText("NID এর পিছনের ছবি তুলুন");  // Adding msg

                        //Displaying Toast with message
                        Toast.makeText(getApplicationContext(),"অনুগ্রহ করে NID-এর ছবিটি আবার তুলুন",Toast.LENGTH_SHORT).show();


                    }
                }
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }


}