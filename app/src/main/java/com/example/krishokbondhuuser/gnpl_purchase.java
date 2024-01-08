package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connection.RequestHandler;
import Connection.URLs;
import Model.GNPL_Apply;
import Model.PaymentModel;
import Model.User;
import session.Appdata;
import session.SharedPrefManager;

public class gnpl_purchase extends AppCompatActivity {

    private TextView TotalMoney, name, GNPL_Time, Value;
    private EditText amount;
    private Button btnBuy;
    private String productQuantity="";
    private String product_time="", product_name="", main_value="", product_price="";
    private Spinner spinner;

    private static final Map<Character, Character> digitMapping = createDigitMapping();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnpl_purchase);

        name       = findViewById(R.id.product_name);
        GNPL_Time  = findViewById(R.id.product_time);
        Value      = findViewById(R.id.product_value);

        amount     = findViewById(R.id.amountProd);

        btnBuy     = findViewById(R.id.btn_submit);
        spinner    = findViewById(R.id.fieldSelect);

        String fieldData = Appdata.getInstance(gnpl_purchase.this).isData();
        System.out.println(fieldData+"new");

        List<String> categories = new ArrayList<String>();

        categories.add("জমি সিলেক্ট করুন");
        categories.add("(৮১৪২৩) ~ ৬৮৯/১ কুমিল্লা - কুমিল্লা");
        categories.add("(৮১৪২৪) ~ ৬৮৯/১ কুমিল্লা - কুমিল্লা");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //spinner.getSelectedItem();
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = amount.getText().toString();
                String temp = String.valueOf(spinner.getSelectedItem());
                if (TextUtils.isEmpty(data)) {
                    amount.setError("পরিমান লিখুন");
                    amount.requestFocus();
                    return;
                }

                if (temp.contains("জমি সিলেক্ট করুন")) {
                    spinner.requestFocus();
                    return;
                }

                int i = Integer.valueOf(amount.getText().toString());
                int j = Integer.valueOf(main_value);
                int re= i*j;
                Value.setText(convertToBanglaNumber(""+re)+" টাকা");

                spinner.requestFocus();
                btnBuy.setEnabled(true);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = amount.getText().toString();
                String temp = String.valueOf(spinner.getSelectedItem());
                if (TextUtils.isEmpty(data)) {
                    amount.setError("পরিমান লিখুন");
                    amount.requestFocus();
                    return;
                }
                if (temp.contains("জমি সিলেক্ট করুন")) {
                    spinner.requestFocus();
                    return;
                }
                    int i = Integer.valueOf(amount.getText().toString());
                    int j = Integer.valueOf(main_value);
                    int re= i*j;

                    Value.setText(convertToBanglaNumber(""+re)+" টাকা");

                    add_payment();

            }
        });
        //prev Page Data.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product_name = bundle.getString("p_p_name");
            product_time = bundle.getString("p_p_time");
            product_price = bundle.getString("p_p_total_price");

            main_value = product_price;

            String a = product_time; // Example input string
            // Extract the number from the string
            String numberString = a.split(" ")[0];
            int number = Integer.parseInt(numberString);
            // Convert the number to its Bengali representation
            String banglaNumber = convertToBangla(number);
            // Concatenate the Bengali number with the remaining string
            String banglaString = banglaNumber + " মাস ";

            name.setText(product_name);
            GNPL_Time.setText(banglaString);
            Value.setText(product_price);
            }
        }
    // Method to convert a number to its Bengali representation
    private static String convertToBangla(int number) {
        String[] banglaNumbers = {
                "০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "১০", "১১", "১২"
        };

        return banglaNumbers[number];
    }

    private static Map<Character, Character> createDigitMapping() {
        Map<Character, Character> mapping = new HashMap<>();
        mapping.put('0', '০');
        mapping.put('1', '১');
        mapping.put('2', '২');
        mapping.put('3', '৩');
        mapping.put('4', '৪');
        mapping.put('5', '৫');
        mapping.put('6', '৬');
        mapping.put('7', '৭');
        mapping.put('8', '৮');
        mapping.put('9', '৯');
        return mapping;
    }
    private static String convertToBanglaNumber(String englishNumber) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < englishNumber.length(); i++) {
            char digit = englishNumber.charAt(i);
            if (digitMapping.containsKey(digit)) {
                builder.append(digitMapping.get(digit));
            } else {
                builder.append(digit);
            }
        }
        return builder.toString();
    }
    private void add_payment(){
        User user           = SharedPrefManager.getInstance(this).getUser();
        String product_n    = product_name.toString();
        String product_q    = amount.getText().toString();
        String product_m    = main_value;
        String product_t    = String.valueOf(Integer.parseInt(product_q) * Integer.parseInt(product_m));
        String field_n      = String.valueOf(spinner.getSelectedItem());
        String farmer_serial= String.valueOf(user.getId());

        class input_payment extends AsyncTask<Void, Void, String> {
            //private ProgressBar progressBar;
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("product_name", product_n);
                params.put("product_quantity", product_q);
                params.put("product_mainValue", product_m);
                params.put("total_cost",product_t);
                params.put("field_value",field_n);
                params.put("farmer_serial",farmer_serial);

                return requestHandler.sendPostRequest(URLs.URL_GNPL_APPLY, params);
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject paymentJson = obj.getJSONObject("payment");
                        GNPL_Apply payment = new GNPL_Apply(
                                paymentJson.getString("product_name"),
                                paymentJson.getString("product_quantity"),
                                paymentJson.getString("product_mainValue"),
                                paymentJson.getString("total_cost"),
                                paymentJson.getString("field_value"),
                                paymentJson.getString("farmer_serial")
                        );

                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        input_payment ip = new input_payment();
        ip.execute();
        finish();
        startActivity(new Intent(getApplicationContext(),gnpl_apply.class));
    }
}