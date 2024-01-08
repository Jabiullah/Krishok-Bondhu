package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

public class product_display_info extends AppCompatActivity {

    ImageView product;
    TextView t1,t2,t3;
    Button btn1,btn2;

    String product_name, product_price, product_time, product_description;


    //private static final Map<Character, Character> digitMapping = createDigitMapping();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display_info);

        product = findViewById(R.id.productImage);
        t1 = findViewById(R.id.ProductName);
        t2 = findViewById(R.id.ProductPrice);
        t3 = findViewById(R.id.ProductDescription);

        btn1 = findViewById(R.id.btnGNPL);
        btn2 = findViewById(R.id.btnOneTime);

        //prev Page Data.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product_name        = bundle.getString("p_p_name");
            product_time        = bundle.getString("p_p_time");
            product_price       = bundle.getString("p_p_total_price");
            product_description = bundle.getString("p_p_des");
        }

        t1.setText(product_name);
        t2.setText(product_price);
        t3.setText(product_description);

        String a = product_time; // Example input string
        // Extract the number from the string
        String numberString = a.split(" ")[0];
        int number = Integer.parseInt(numberString);
        // Convert the number to its Bengali representation
        String banglaNumber = convertToBangla(number);
        // Concatenate the Bengali number with the remaining string
        String banglaString = banglaNumber + " মাস";

        btn1.setText("এখন-ই কিনুন "+product_price);
        btn2.setText(""+banglaString+" সময়কাল এর জন্য GNPL আবেদন করুন");

    }

    // Method to convert a number to its Bengali representation
    private static String convertToBangla(int number) {
        String[] banglaNumbers = {
                "০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "১০", "১১", "১২"};

        return banglaNumbers[number];
    }
}