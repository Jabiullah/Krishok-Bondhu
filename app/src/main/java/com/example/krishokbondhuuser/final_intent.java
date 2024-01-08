package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Connection.RequestHandler;
import Connection.URLs;
import session.SharedPrefManager;

public class final_intent extends AppCompatActivity {
    private static final int CODE_POST_REQUEST = 1025;
    private static final int CODE_GET_REQUEST = 1024;
    String id="" ,phone ="", nid ="", address ="",place ="",name ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_intent);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            nid = bundle.getString("nid");
            address = bundle.getString("address");
            place = bundle.getString("placeOf");
            name = bundle.getString("name");
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("name",name);
        params.put("nid", nid);
        params.put("phone", phone);
        params.put("address", address);
        params.put("placeof", place);




        PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_USER_NID_DONE , params, CODE_POST_REQUEST);
        request.execute();

        SharedPrefManager.getInstance(getApplicationContext()).userReg(id,name,nid,phone,address,place);
        //starting the profile activity
        finish();

        Intent intent_home = new Intent(getApplicationContext(), homePage.class);
        Toast.makeText(getApplicationContext(), "স্বাগতম লগইন করার জন্য", Toast.LENGTH_SHORT).show();
        startActivity(intent_home);




    }
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
                System.out.println("id "+object.getString("message"));
                id = object.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);
            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);
            return null;
        }
    }
}