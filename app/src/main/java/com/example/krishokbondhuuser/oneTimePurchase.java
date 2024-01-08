package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Connection.RequestHandler;
import Connection.URLs;
import Model.PaymentModel;
import Model.User;
import session.SharedPrefManager;

public class oneTimePurchase extends AppCompatActivity {
    EditText quantity, phoneNumber, pin;
    ImageView   bkash, nagad, bkashOK, nagadOK;


    TextView TotalMoney, name, Value,paymentStructure;

    Button btnBuy;
    String productQuantity="";

    //bundle value
    String b = "", product_price="", product_name="", main_value="";
    Boolean nagadCheck = false;
    Boolean bkashCheck = false;
    String pay="এককালীন ক্রয়কৃত";

    private static final Map<Character, Character> digitMapping = createDigitMapping();
    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_purchase);

        quantity            = findViewById(R.id.amountOfProduct);
        phoneNumber         = findViewById(R.id.PaymentPhone);
        pin                 = findViewById(R.id.PaymentPhonePin);

        bkash               = findViewById(R.id.bkash);
        bkashOK             = findViewById(R.id.bkashTik);
        nagad               = findViewById(R.id.nagad);
        nagadOK             = findViewById(R.id.nagadTik);

        TotalMoney          = findViewById(R.id.product_value_payment);

        btnBuy              = findViewById(R.id.btn_buy);

        name                = findViewById(R.id.product_name_payment);
        paymentStructure    = findViewById(R.id.product_time_payment);
        Value               = findViewById(R.id.product_value_payment);

        bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bkashOK.setVisibility(View.VISIBLE);
                nagadOK.setVisibility(View.GONE);
                bkashCheck = true;
                nagadCheck = false;
                productQuantity = quantity.getText().toString();
                if (TextUtils.isEmpty(productQuantity)) {
                    quantity.setError("পরিমান লিখুন");
                    quantity.requestFocus();
                    return;
                }
                //open Buying Parameter
                openAll();


            }
        });
        nagad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bkashOK.setVisibility(View.GONE);
                nagadOK.setVisibility(View.VISIBLE);
                bkashCheck = false;
                nagadCheck = true;
                productQuantity = quantity.getText().toString();
                if (TextUtils.isEmpty(productQuantity)) {
                    quantity.setError("পরিমান লিখুন");
                    quantity.requestFocus();
                    return;
                }
                //open Buying Parameter
                openAll();

            }

        });
        //btnBuyPress
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_payment();
            }
        });

        //prev Page Data.
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            b = bundle.getString("check");
            product_name = bundle.getString("p_p_name_2");
            product_price = bundle.getString("p_p_total_price_2");

            main_value = product_price;

            if(b.equals("false")){
                name.setText(product_name);
                paymentStructure.setText("এককালীন ক্রয়কৃত");
                Value.setText(product_price);
            }
        }

    }
    private void openAll(){
        phoneNumber.setVisibility(View.VISIBLE);
        pin.setVisibility(View.VISIBLE);
        btnBuy.setVisibility(View.VISIBLE);

        phoneNumber.requestFocus();
        //calculations
        productQuantity = quantity.getText().toString();
        int i = Integer.valueOf(productQuantity);
        int j = Integer.valueOf(main_value);
        int k = i*j;
        String finalValue = ""+k;
        Value.setText(convertToBanglaNumber(finalValue)+" টাকা");
        quantity.setText(convertToBanglaNumber(productQuantity));
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
    private void add_payment() {
        User user = SharedPrefManager.getInstance(this).getUser();

        String info    = "("+name.getText().toString().trim()+") কিনেছেন "+convertToBanglaNumber(productQuantity)+"(কেজি)";

        String pay_value = Value.getText().toString().trim(); // ekhon tk deche
        String paymentProc="";
        if(bkashCheck==true && nagadCheck==false){
            paymentProc = "বিকাশ";
        }
        if(bkashCheck==false && nagadCheck==true){
            paymentProc = "নগদ";
        }
        final String p = paymentProc;
        String farmer_serial = String.valueOf(user.getId());

        String transaction_id;
        String phone    = phoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            phoneNumber.setError("পরিমান লিখুন");
            phoneNumber.requestFocus();
            return;
        }
        transaction_id="220HUvGaQJVFIIRij"+phone.substring(phone.length() - 2)+"AA";
        //Toast.makeText(getApplicationContext()," "+info+"\n"+pay_value+"\n"+farmer_serial+"\n"+sts+"\n"+"\n"+transaction_id, Toast.LENGTH_LONG).show();
        class input_payment extends AsyncTask<Void, Void, String> {
            //private ProgressBar progressBar;
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("info", info);
                params.put("pay_value", pay_value);
                params.put("pay_process", p);
                params.put("transaction_id",transaction_id);
                params.put("buy_structure",pay);
                params.put("farmer_serial",farmer_serial);

                return requestHandler.sendPostRequest(URLs.URL_PAYMENT, params);
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
                        PaymentModel payment = new PaymentModel(
                                paymentJson.getString("info_history"),
                                paymentJson.getString("payment_value"),
                                paymentJson.getString("payment_process"),
                                paymentJson.getString("farmer_no"),
                                paymentJson.getString("transaction_id"),
                                paymentJson.getString("buy_structure")
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
        startActivity(new Intent(getApplicationContext(),PaymentProgress.class));
    }

}