package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import Connection.RequestHandler;
import Connection.URLs;
import Model.User;
import Model.User_nid;
import session.SharedPrefManager;

public class SuccessRegistration extends AppCompatActivity {

    private String phone="";
    private String nidNo="";

    private String w1="";
    private String w2="";

    private String w3="";

    TextView name, ph, nid, address, birthplace, postocde;

    Button btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_registration);

        name = findViewById(R.id.userName);
        ph = findViewById(R.id.user_phone);
        nid = findViewById(R.id.user_nid);
        address = findViewById(R.id.user_address);
        birthplace = findViewById(R.id.user_birthplace);
        postocde    = findViewById(R.id.user_postCode);
        btnReg      = findViewById(R.id.btnDone);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleRegDone = new Bundle();
                bundleRegDone.putString("phone",phone);
                bundleRegDone.putString("name",w1);
                bundleRegDone.putString("nid",nidNo);
                bundleRegDone.putString("address",w2);
                bundleRegDone.putString("placeOf",w3);


                Intent intentRegProcess = new Intent(SuccessRegistration.this, final_intent.class);
                intentRegProcess.putExtras(bundleRegDone);
                startActivity(intentRegProcess);
                finish();

            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            nidNo = bundle.getString("nidNo");
        }
        //System.out.println(nidNo);
        //Toast.makeText(getApplicationContext(),""+nidNo+"\n Phone no."+phone,Toast.LENGTH_SHORT).show();
        authenticationUser();
    }
    private void authenticationUser() {
        final String p = nidNo;
        class UserLogin extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    System.out.println(obj);
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        final String user_message = obj.getString("message");
                        if(user_message.contains("new")){
                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("user");
                            //creating a new user object
                            User_nid user_nid = new User_nid(
                                    userJson.getString("name"),
                                    userJson.getString("nidNo"),
                                    userJson.getString("address"),
                                    userJson.getString("birthPlace"),
                                    userJson.getString("postCode")
                            );

                            //SharedPrefManager.getInstance(getApplicationContext()).userReg(user_nid);
                            //starting the profile activity
                            //finish();

                            name.setText(" আপনার নাম : "+user_nid.getName());
                            ph.setText(" আপনার ফোন  : "+phone);
                            nid.setText(" আপনার এনআইডি : "+user_nid.getNidNo());
                            address.setText(" আপনার ঠিকানা : \n"+user_nid.getAddress());
                            birthplace.setText(" আপনার জন্মস্থান : "+user_nid.getBirthPlace());
                            postocde.setText("আপনার পোস্টকোড : "+user_nid.getPostCode());

                            w1 =user_nid.getName();
                            w2 = user_nid.getAddress();
                            w3 = user_nid.getBirthPlace();
                        }else {
                            // if new user
//                            Bundle bundleRegProcess = new Bundle();
//                            bundleRegProcess.putString("phone",p);
//
//                            Intent intentRegProcess = new Intent(Auth.this, RegistrationProcess.class);
//                            intentRegProcess.putExtras(bundleRegProcess);
//                            startActivity(intentRegProcess);
//                            finish();
//
//                            Toast.makeText(getApplicationContext(), "স্বাগতম আপনাকে আমাদের প্লাটফর্মে", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "অচল ফোন নম্বর", Toast.LENGTH_SHORT).show();
                        //back to otp intent

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_nid", nidNo);
                System.out.println(nidNo);
                return requestHandler.sendPostRequest(URLs.URL_USER_NID_DISPLAY, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

}