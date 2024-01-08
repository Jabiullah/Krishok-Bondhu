package com.example.krishokbondhuuser;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import Connection.RequestHandler;
import Connection.URLs;
import Model.User;
import session.SharedPrefManager;

public class Auth extends AppCompatActivity {
    TextView showMsg,TimeOTP;
    PinView pinView;
    Button btnRegProcess_Home;
    FirebaseAuth mAuth;
    String verification;
    String prevPagePhoneNumber;

    SessionTimeOut sessionTimeOut ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        // jump to Homepage if sharedPref had
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, homePage.class));
            return;
        }

        showMsg = (TextView)findViewById(R.id.phnMessage);
        pinView = findViewById(R.id.PinView);
        btnRegProcess_Home = findViewById(R.id.VerifyCode);
        TimeOTP     = (TextView)findViewById(R.id.timeCountDown);

        //prevData display
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            prevPagePhoneNumber = bundle.getString("phone");
            String msg = showMsg.getText().toString().trim();
            showMsg.setText(prevPagePhoneNumber+" "+msg);
        }
        //otp auto read purpose
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(prevPagePhoneNumber);


        btnRegProcess_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pinView.getText().toString().length()!=6){
                    pinView.setError("অনুগ্রহ করে সম্পূর্ণ ওটিপি দিন");
                    pinView.requestFocus();
                }
                else if (pinView.getText().toString().length()==6){ // other phone to otp write
                    verifyCode(pinView.getText().toString().trim());
                }
            }
        });
        sessionTimeOut = new SessionTimeOut(120000, 1000);
        sessionTimeOut.start();
    }
    public class SessionTimeOut extends CountDownTimer {
        public SessionTimeOut(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/1000);
            TimeOTP.setText(progress+" সেকেন্ড এর মধ্যে sms পাঠানো হবে ");
        }
        @Override
        public void onFinish() {
            finish();
            //Toast for time up
            TimeOTP.setText(" আমরা কি আপনার ফোনে আবার ওটিপি পাঠাতে পারি ? ");
        }

    }

    private void sendVerificationCode(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(120L,TimeUnit.SECONDS)// (optional) Activity for callback binding
                        .setActivity(this)
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            verifyCode(code);
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // Show a message and update the UI
            // Intent intentOtp = new Intent(Auth.this, OtpPage.class);
            // startActivity(intentOtp);
        }
        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
              super.onCodeSent(verificationId,token);
              verification = verificationId;
        }
    };

    private void verifyCode(String Code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification,Code);
        signInByCredentials(credential);
        //pinFillUp(Code);
    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // OTP matched with firebase ! now have to check new user or old user !
                    // check old user and new user
                    authenticationUser();
                }else{
                    // for some reason task is not completed at all.
                    Intent intentBackOTP = new Intent(Auth.this, OtpPage.class);
                    startActivity(intentBackOTP);
                    finish();
                }
            }
        });
    }

    private void authenticationUser() {
        final String p = prevPagePhoneNumber;
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
                        if(user_message.contains("old")){
                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("user");
                            //creating a new user object
                            User user = new User(
                                    userJson.getInt("user_id"),
                                    userJson.getString("user_phone"),
                                    userJson.getString("user_name"),
                                    userJson.getString("user_nid"),
                                    userJson.getString("user_birthPlace"),
                                    userJson.getString("user_address")
                            );

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            //starting the profile activity
                            finish();

                            Intent intent_home = new Intent(getApplicationContext(), homePage.class);
                            Toast.makeText(getApplicationContext(), "স্বাগতম পুনরায় লগইন করার জন্য", Toast.LENGTH_SHORT).show();
                            startActivity(intent_home);

                        }else {
                            // if new user
                            Bundle bundleRegProcess = new Bundle();
                            bundleRegProcess.putString("phone",p);

                            Intent intentRegProcess = new Intent(Auth.this, RegistrationProcess.class);
                            intentRegProcess.putExtras(bundleRegProcess);
                            startActivity(intentRegProcess);
                            finish();

                            Toast.makeText(getApplicationContext(), "স্বাগতম আপনাকে আমাদের প্লাটফর্মে", Toast.LENGTH_SHORT).show();
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
                params.put("user_phone", p);
                System.out.println(p);
                return requestHandler.sendPostRequest(URLs.URL_USER_CHECK, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }


}