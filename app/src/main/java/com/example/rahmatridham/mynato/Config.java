package com.example.rahmatridham.mynato;

/**
 * Created by PERSONAL on 7/20/2016.
 */
public class Config {
    //URL to our loginBut.php file
    public static final String MAIN_URL = "http://scripthink.com/mynato/api/";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means loginBut is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String USERID_SHARED_PREF = "USERID";
    public static final String NIPEG_SHARED_PREF = "NIPEG";
    public static final String NAMA_SHARED_PREF = "NAMA";
    public static final String KODEPOSISI_SHARED_PREF = "kode_posisi";
    public static final String ROLE_SHARED_PREF = "ROLE";
    public static final String JABATAN_SHARED_PREF = "JABATAN";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
