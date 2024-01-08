package session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.krishokbondhuuser.NavigationActivity;
import com.example.krishokbondhuuser.OtpPage;

import Model.User;
import Model.User_nid;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "KrishokBondhu";
    private static final String KEY_ON_BOARD_STATUS = "False";

    private static final String KEY_ID              = "keyid";
    private static final String KEY_PHONE           = "keyphone";
    private static final String KEY_NAME            = "keyname";
    private static final String KEY_NID             = "keynid";
    private static final String KEY_ADDRESS         = "keyaddress";

    private static final String KEY_BIRTHPLACE      = "keybirthplace";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    //method will check the navigation activity once implement or not ! value replaced here.
    public void onBoardCheck() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        NavigationActivity nav = new NavigationActivity();
        editor.putString(KEY_ON_BOARD_STATUS, nav.getOnBoardStatus());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isDisplayedOrNot() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ON_BOARD_STATUS, null) != null;
    }
    //this method will clean the onBoard Status
    public void cleanAll() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    //method will store the user loggedIn Info
        public void userReg(String id ,String a, String b, String c, String d, String e){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_ID, id);
            editor.putString(KEY_NAME, a);
            editor.putString(KEY_NID, b);
            editor.putString(KEY_PHONE,c);
            editor.putString(KEY_ADDRESS,d);
            editor.putString(KEY_BIRTHPLACE,e);
            editor.apply();
        }
        public void userLogin(User user) {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt(KEY_ID, user.getId());
            editor.putString(KEY_PHONE, user.getPhone());
            editor.putString(KEY_NAME, user.getName());
            editor.putString(KEY_NID,user.getNID());
            editor.putString(KEY_ADDRESS,user.getAddress());
            editor.putString(KEY_BIRTHPLACE,user.getUser_birthPlace());
            editor.apply();
        }
    //this method will give the logged in user
        public User getUser() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return new User(
                    sharedPreferences.getInt(KEY_ID, -1),
                    sharedPreferences.getString(KEY_PHONE, null),
                    sharedPreferences.getString(KEY_NAME, null),
                    sharedPreferences.getString(KEY_NID, null),
                    sharedPreferences.getString(KEY_BIRTHPLACE, null),
                    sharedPreferences.getString(KEY_ADDRESS, null)
                    );
        }
        public boolean isLoggedIn() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_PHONE, null) != null;
        }
    // logout work
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, OtpPage.class));
    }

}
