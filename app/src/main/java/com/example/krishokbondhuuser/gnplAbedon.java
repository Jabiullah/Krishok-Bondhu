package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.GNPLPaymentHistoryAdapter;
import Adapter.PaymentHistoryAdapter;
import Connection.URLs;
import Model.GNPLPaymentHistoryModel;
import Model.PaymentHistoryModel;
import Model.User;
import session.SharedPrefManager;


public class gnplAbedon extends AppCompatActivity {
    private RecyclerView recyclerView;

    List<GNPLPaymentHistoryModel> GNPLpaymentHistoryList;
    User user = SharedPrefManager.getInstance(this).getUser();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnpl_abedon);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGNPLHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GNPLpaymentHistoryList = new ArrayList<>();
        load_History_list();
    }
    private void load_History_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GNPL_PAYMENT_HISTORY+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                GNPLpaymentHistoryList.add(new GNPLPaymentHistoryModel(
                                        n.getString("date_history"),
                                        n.getString("product_name"),
                                        n.getString("product_quantity"),
                                        n.getString("product_mainValue"),
                                        n.getString("total_cost"),
                                        n.getString("field_value"),
                                        n.getString("gnpl_status"),
                                        n.getString("gnpl_payment")
                                ));
                            }
                            GNPLPaymentHistoryAdapter adapter = new GNPLPaymentHistoryAdapter(gnplAbedon.this, GNPLpaymentHistoryList);
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