package com.svea.app.utils;

import android.os.Environment;

import com.svea.app.R;

public class Constants {

    static final String BASE_URL = "http://techwinlabs.com/ceeca/api/app/";
    public static final String MEDIA_URL = "http://vpnewyork.com/snip/media/";
    static final String USERNAME = "snip";
    static final String PASSWORD = "pinspnytpOjtfw!@#";
    public static final String APP_PASSWORD = "3333";
    public static final int SUCCESS_CODE = 200;
    public static final int NO_CONTENT_CODE = 204;
    public static final String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0"};
    public static final String LOCAL_IMAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/ElllementsExample/";
    public static final String KEY_USERID = "userid";
    public static final String KEY_SESSION = "sessionkey";
    public static final String ENCRYPT_KEY = "c1b232e5058928c7";
    public static final String DEVICE_TYPE = "1";
    public static final String QUOTE_CATEGORY = "love";
    public static boolean SHOW_LOG = true;
    public static String USER ="users";

    public static final String[] heading = {"Sveacoin","Bitcoin","Ethereum","Litecoin","Ripple", "Bitcoin Cash"};
    public static final String[] sub_heading = {"SVC","BTC", "ETH", "LTC", "XRP", "BCH"};
    public static final String[] price = {"131.62", "7500", "241.34", "46.29", "0.04", "7326.93"};
    public static final int[] icons = {R.drawable.image_svea_coin, R.drawable.image_bitcoin,
            R.drawable.image_ethereum, R.drawable.image_litecoin,
            R.drawable.image_ripple, R.drawable.image_bitctcoin_cash};

}
