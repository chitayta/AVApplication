package com.tma.tctay.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tma.tctay.activity.AirVantageLoginActivity;
import com.tma.tctay.models.AccessToken;

import java.util.HashMap;

/**
 * Created by tctay on 6/30/2017.
 */

public class UserSessionManager {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Context appContext;

    int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "AirVantageUserCredentials";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_SYSTEM_UID = "systemUid";
    public static final String KEY_CLIENT_ID = "clientId";
    public static final String KEY_CLIENT_SECRET = "clientSecret";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_EXPIRES_IN = "expires_in";

    public UserSessionManager(Context context)
    {
        this.appContext = context;
        sharedPreferences = this.appContext.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createUserLoginSession(String email, String systemUid, String clientId, String clientSecret, String accessToken, String refreshToken, String tokenType, Integer expiresIn)
    {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_SYSTEM_UID, systemUid);
        editor.putString(KEY_CLIENT_ID, clientId);
        editor.putString(KEY_CLIENT_SECRET, clientSecret);
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        editor.putInt(KEY_EXPIRES_IN, expiresIn);

        editor.commit();
    }

    public void refreshAccessToken(String accessToken, String refreshToken, String tokenType, Integer expiresIn)
    {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        editor.putInt(KEY_EXPIRES_IN, expiresIn);

        editor.commit();
    }

    public boolean checkLogin()
    {
        if (!this.isUserLoggedIn())
        {
            return true;
        }
        else
        {
//            Intent intent = new Intent(appContext, MainTabDataViewActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            appContext.startActivity(intent);


            return false;
        }
    }

    public HashMap<String, String> getUserCredentials()
    {

        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));
        user.put(KEY_SYSTEM_UID, sharedPreferences.getString(KEY_SYSTEM_UID, null));
        user.put(KEY_CLIENT_ID, sharedPreferences.getString(KEY_CLIENT_ID, null));
        user.put(KEY_CLIENT_SECRET, sharedPreferences.getString(KEY_CLIENT_SECRET, null));

        return user;
    }

    public AccessToken getAccessToken()
    {
        //HashMap<String, AccessToken> sharedPreferenceAccessToken = new HashMap<String, AccessToken>();
        AccessToken accessToken = new AccessToken(sharedPreferences.getString(KEY_ACCESS_TOKEN, null),
                                                    sharedPreferences.getString(KEY_TOKEN_TYPE, null),
                                                    sharedPreferences.getString(KEY_REFRESH_TOKEN, null),
                                                    sharedPreferences.getInt(KEY_EXPIRES_IN, 0));
        return accessToken;
        //sharedPreferenceAccessToken.put("accessToken", accessToken);
        //return sharedPreferenceAccessToken;
    }

    public void logoutUser()
    {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(appContext, AirVantageLoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        appContext.startActivity(intent);

    }

    public boolean isUserLoggedIn()
    {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }

}
