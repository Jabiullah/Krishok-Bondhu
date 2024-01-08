package session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.krishokbondhuuser.NavigationActivity;
import com.example.krishokbondhuuser.filedPage;

public class Appdata {
    private static final String SHARED_PREF_NAME = "Appdata";

    private static Appdata mInstance;
    private static Context mCtx;

    private static final String KEY_OK_DEED      = "keyokdeed";

    private Appdata(Context context) {
        mCtx = context;
    }

    public static synchronized Appdata getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Appdata(context);
        }
        return mInstance;
    }
    public void onDeedCheck() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        filedPage f = new filedPage();
        editor.putString(KEY_OK_DEED, f.getOnDeed());
        editor.apply();
    }
    public String isData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY_OK_DEED, "");
        return text;
    }
}
