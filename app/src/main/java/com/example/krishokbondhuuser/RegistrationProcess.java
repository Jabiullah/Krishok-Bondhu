package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegistrationProcess extends AppCompatActivity {
    Button NidPictureTake;
    String newUserPhone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_process);

        NidPictureTake = findViewById(R.id.ImageCapturePage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newUserPhone = bundle.getString("phone");
            //Toast.makeText(getApplicationContext(),""+newUserPhone+"\n",Toast.LENGTH_SHORT).show();
        }

        NidPictureTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleNIDRead = new Bundle();
                bundleNIDRead.putString("phone",newUserPhone);

                Intent intentImageTakeProcess = new Intent(RegistrationProcess.this, NIDImageTake.class);
                intentImageTakeProcess.putExtras(bundleNIDRead);
                startActivity(intentImageTakeProcess);
                finish();
            }
        });
    }

}