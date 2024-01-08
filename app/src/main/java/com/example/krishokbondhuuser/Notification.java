package com.example.krishokbondhuuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connection.URLs;
import Model.NotificationModel;
import Model.User;
import session.SharedPrefManager;
import Adapter.*;

public class Notification extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    List<NotificationModel> notificationList;

    User user = SharedPrefManager.getInstance(this).getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //bottomNav View
        bottomNavigationView = findViewById(R.id.bottomNavNotification);
        bottomNavigationView.setSelectedItemId(R.id.notification);
        
        //notification display
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotification);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        notificationList = new ArrayList<>();
        
        //notification load
        load_Notification_list();

        //bottomNav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent onHome = new Intent(Notification.this,homePage.class);
                        startActivity(onHome);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        finish();
                        return true;
                    case R.id.notification:
                        return true;
                    case R.id.profile:
                        Intent onProfile = new Intent(Notification.this,Profile.class);
                        startActivity(onProfile);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }
    private void load_Notification_list() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_NOTIFICATION+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject n = array.getJSONObject(i);
                                notificationList.add(new NotificationModel(
                                        n.getString("date"),
                                        n.getString("info")
                                ));
                            }
                            NotificationAdapter adapter = new NotificationAdapter(Notification.this, notificationList);
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