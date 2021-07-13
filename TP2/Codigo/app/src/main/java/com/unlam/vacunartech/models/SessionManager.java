package com.unlam.vacunartech.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.unlam.vacunartech.LoginActivity;
import com.unlam.vacunartech.SMSActivity;

import java.util.HashMap;

public class SessionManager {
    //private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    //Shared Preferences file name
    private static final String PREFERENCE_NAME = "SessionManager";
    // Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    //Email Address
    public static final String KEY_EMAIL = "email";

    public static final String USER_TOKEN = "user_token";


    //Constructor
    //Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_MULTI_PROCESS);
        editor = pref.edit();
    }

    /**
     * Function to save auth token
     */
    public void saveAuthToken(String token) {
        editor.putString(USER_TOKEN, token);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.apply();
    }

    /**
     * Function to fetch auth token
     * @return
     */
    public String getAuthToken() {
        return pref.getString(USER_TOKEN, null);
    }

    //Create Login session
    public void createUserLoginSession(String email) {

        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        //Storing email in pref
        editor.putString(KEY_EMAIL, email);
        //editor.putBoolean(IS_USER_LOGIN,true);
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */

    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, SMSActivity.class);

            // Staring Login Activity
            context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);

        // Staring Login Activity
        context.startActivity(i);
    }

    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
