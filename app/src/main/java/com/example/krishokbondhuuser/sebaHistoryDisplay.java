package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import Adapter.NotificationAdapter;
import Adapter.PaymentHistoryAdapter;
import Connection.URLs;
import Model.NotificationModel;
import Model.PaymentHistoryModel;
import Model.PaymentModel;
import Model.User;
import session.SharedPrefManager;

public class sebaHistoryDisplay extends AppCompatActivity {

    private RecyclerView recyclerView;

    private TextView t;
    private ImageView img;
    List<PaymentHistoryModel> paymentHistoryList;

    Button btn1;

    User user = SharedPrefManager.getInstance(this).getUser();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seba_history_display);

        btn1 = findViewById(R.id.gnplBtn);
        //notification display
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //t            = findViewById(R.id.heading_of_history);
        //img          = findViewById(R.id.refreshHistory);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onBoarding = new Intent(sebaHistoryDisplay.this, gnplAbedon.class);
                startActivity(onBoarding);
                finish();
            }
        });
        paymentHistoryList = new ArrayList<>();
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//                    t.setText("সর্বশেষ ডেটা আপডেট করা হয়েছে ~ \n দুপুর ১২.২১ মিনিট " );
//                }
//                // Create a Random object
//                Random random = new Random();
//                // Generate a random number between 0 and 3
//                int randomValue = random.nextInt(4);
//                // Select one of the numbers based on the random value
//                int randomNumber;
//                switch (randomValue) {
//                    case 0:
//                        randomNumber = 90;
//                        break;
//                    case 1:
//                        randomNumber = 180;
//                        break;
//                    case 2:
//                        randomNumber = 270;
//                        break;
//                    case 3:
//                        randomNumber = 360;
//                        break;
//                    default:
//                        randomNumber = 45; // Default value if none of the cases match
//                        break;
//                }
//
//                //animation rotate 90 "sync"
//                final RotateAnimation rotateAnim = new RotateAnimation(0.0f, randomNumber,
//                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//                rotateAnim.setDuration(0);
//                rotateAnim.setFillAfter(true);
//                img.startAnimation(rotateAnim);
//
//                paymentHistoryList.clear();
//                load_History_list();
//            }
//        });
        //notification load

        load_History_list();
    }
    private void load_History_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_PAYMENT_HISTORY+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                paymentHistoryList.add(new PaymentHistoryModel(
                                        n.getString("date_history"),
                                        n.getString("info_history"),
                                        n.getString("payment_value"),
                                        n.getString("payment_process"),
                                        n.getString("transaction_id"),
                                        n.getString("buy_structure")
                                ));
                            }
                            PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(sebaHistoryDisplay.this, paymentHistoryList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}