package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ValidityCheckNID extends AppCompatActivity {

    TextView NidVal,NameVal,BirthPlaceVal,PostCodeVal,AddressVal;
    Button SuccessReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validity_check_nid);

        NidVal          = findViewById(R.id.NidValue);
        NameVal         = findViewById(R.id.NameValue);
        BirthPlaceVal   = findViewById(R.id.BirthPlaceValue);
        PostCodeVal     = findViewById(R.id.PostCodeValue);
        AddressVal      = findViewById(R.id.AddressValue);

        SuccessReg      = findViewById(R.id.DoneNid);

        SuccessReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onSuccess = new Intent(ValidityCheckNID.this,SuccessRegistration.class);
                startActivity(onSuccess);
                finish();
            }
        });


    }
}