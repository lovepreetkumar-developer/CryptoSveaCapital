package com.svea.app.server;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.svea.app.activities.HomeActivity;
import com.svea.app.activities.LoginActivity;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApisBackup {

    private String SERVER_URL = "https://ykrox.com/server/";
    private Context context;
    private ResponseInterface listener;

    public ApisBackup(Context context, ResponseInterface listener) {
        this.context = context;
        this.listener = listener;
    }

    private void web(final String method, final CookieManager cookieManager, final String rawdata, final File[] ifile, final File ofile) {
        Map<String, Object> myMap = new HashMap<String, Object>() {{
            put("url", SERVER_URL);
            put("method", method);
            put("cookieManager", cookieManager);
            put("rawdata", rawdata);
            put("ifile", ifile);
            put("ofile", ofile);
        }};
        new WebTask().execute(myMap);
    }

    public class WebTask extends AsyncTask<Map<String, Object>, Integer, WebTask.Result> {
        class Result {
            public String mResultValue;
            public Exception mException;

            public Result(String resultValue) {
                mResultValue = resultValue;
            }

            public Result(Exception exception) {
                mException = exception;
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Result doInBackground(Map<String, Object>... map) {
            Result result = null;

            try {

                String resultString = web((String) map[0].get("url"), (String) map[0].get("method"), (CookieManager) map[0].get("cookieManager"), (String) map[0].get("rawdata"), (File[]) map[0].get("ifile"), (File) map[0].get("ofile"));
                //Log.d("test", resultString);
                //cmd(resultString);
                result.mResultValue=resultString;
            } catch (Exception e) {
                result = new Result(e);
            }

            return result;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //if (values.length >= 2) {
            // mCallback.onProgressUpdate(values[0], values[1]);
            //}
        }

        @Override
        protected void onPostExecute(Result result) {
            cmd(result.mResultValue);//run karo
        }

        @Override
        protected void onCancelled(Result result) {
        }

        public String web(String url1, String method, CookieManager cookieManager, String rawdata, File ifile[], File ofile) throws IOException {
            //Log.i("ttt","*");

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            InputStream inputStream = null, inputStream1 = null;
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            try {
                //conn.setReadTimeout(3000);
                //conn.setConnectTimeout(3000);
                // Creating HTTP client

                conn = (HttpURLConnection) (new URL(url1)).openConnection();
                String params = "";
                if (method != "POST") {
                    method = "GET";
                }
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
                conn.setRequestMethod(method);
                conn.setDoInput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setUseCaches(false);
                /*
                if(cookieManager!=null) {
                    Map<String, List<String>> headerFields = conn.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                    if (cookiesHeader != null) {
                        for (String cookie : cookiesHeader) {
                            cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                        }
                        if (cookieManager.getCookieStore().getCookies().size() > 0) {
                            //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                            String mcookie = TextUtils.join(";", cookieManager.getCookieStore().getCookies());
                            if (!mcookie.equals(_mcookie)) { mcookie = _mcookie;}
                        }
                    }
                    if(mcookie.length() > 0) {conn.setRequestProperty("Cookie", mcookie);}
                }
                */

                if (method == "POST") {
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    //conn.setRequestProperty("Content-Type","multipart/form-data");// "application/x-www-form-urlencoded");


                    if (rawdata != null) {
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=--Boundary");
                        rawdata = "----Boundary\r\nContent-Disposition: form-data; name=\"data\"\r\n\r\n" + rawdata + "\r\n----Boundary--";

                        conn.setRequestProperty("Content-length", rawdata.getBytes().length + "");
                        //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
                        //conn.setChunkedStreamingMode(0);

                        //conn.setRequestProperty("bill", "contact.txt");
                        //String param="param1=" + URLEncoder.encode("value1","UTF-8")+"&param2="+URLEncoder.encode("value2","UTF-8");
                        //conn.setFixedLengthStreamingMode(param.getBytes().length);

                        //Log.i("TAG", rawdata);//show me this valu


                        inputStream = new ByteArrayInputStream(rawdata.getBytes());
                        bytesAvailable = inputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];
                        bytesRead = inputStream.read(buffer, 0, bufferSize);
                        dos = new DataOutputStream(conn.getOutputStream());
                        while (bytesRead > 0) {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = inputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = inputStream.read(buffer, 0, bufferSize);
                        }


                    }
                }


                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                inputStream = conn.getInputStream();
                buffer = new byte[4096];
                //Object result="";

                StringBuilder result = new StringBuilder();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    result.append(new String(buffer, 0, bytesRead, "utf-8"));
                }
                Log.i("TAG", result.toString());
                return result.toString();
            } catch (Exception ex) {
                //Log.e("ttt",ex.getMessage());
            } finally {
                // Close Stream and disconnect HTTPS conn.
                if (inputStream != null) {
                    inputStream.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
                if (dos != null) {
                    dos.flush();
                    dos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return null;
        }
    }

    private boolean isOnline() {
        //call me or fix it
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    static String server(String url, JSONObject data) {
        String res = "";
        return res;

    }


    public void yks_register(String fname, String lname, String email,
                             String phone, String password, String cpassword) {

        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "register").put("param", (new JSONArray()).put(fname).put(lname).put(email).put(phone).put(password).put(cpassword)).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {sid:null|"session id",status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public void yks_login(String userid, String password) {
        try {
            if (isOnline()) {
                web("POST", null,
                        (new JSONObject()).put("action", "login")
                                .put("param", (new JSONArray()).put(userid)
                                        .put(password)).put("sid", PrefUtils.getInstance().getUserId()).toString(),
                        null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            e.printStackTrace();
        }
        //todo {status:"OK"|"Error",msg:"Message on Error",value:[]}
        //return null;//this is giving null
    }

    public void yks_forgotpassword(String userid) {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "forgotpassword").put("param", (new JSONArray()).put(userid)).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public void yks_refresh() {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "refresh").put("param", new JSONArray("[]")).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public void yks_otpverify(String otp) {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "otpverify").put("param", (new JSONArray()).put(otp)).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {sid:null|"session id",status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public void yks_resendotp() {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "resendotp").put("param", new JSONArray("[]")).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {sid:null|"session id",status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public void yks_logout() {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "logout").put("param", new JSONArray("[]")).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {sid:null,status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public void yks_viewprofile() {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "viewprofile").put("param", new JSONArray("[]")).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
        //todo {sid:null,status:"OK"|"Error",msg:"Message on Error",value:[{fname:"fname",lname:"lname",email:"emailid@domain.com",phone:"phoneno"}]}
    }

    public void yks_viewwallet() {
        try {
            if (isOnline()) {
                web("POST", null, (new JSONObject()).put("action", "viewwallet").put("param", new JSONArray("[]")).put("sid", PrefUtils.getInstance().getUserId()).toString(), null, null);
            } else {
                //print internet not found
            }
        } catch (JSONException e) {
            // print error;
            //e.printStackTrace();
        }
    }

    private void cmd(String json) {
        //print
        Log.d("Dfdd", json);
        try {

            JSONObject jsonobj = new JSONObject(json);
            String sid = jsonobj.get("sid").toString();
            String action = jsonobj.get("action").toString();
            //now how to return listener object
            if (sid != null || action != null) {
                if (!sid.equals(PrefUtils.getInstance().getUserId())) {
                    PrefUtils.getInstance().setUserId(sid);
                }
                //this is saying sid will no null anytime
                //sir dont just comment , do the code here so that i can understand
                if (jsonobj.get("status").toString().equals("OK")) {
                    //here what to do
                    if (jsonobj.get("value") != null) {

                        JSONObject value = (JSONObject) jsonobj.get("value");
                        if (value != null) {
                            if (action.equals("register")) {
                                listener.signUp(json);
                            } else if (action.equals("login")) {
                                listener.login(json);
                            } else if (action.equals("forgotpassword")) {
                                listener.forgotPassword(json);
                                Toast.makeText(context, "Email has been sent", Toast.LENGTH_SHORT).show();
                            } else if (action.equals("otpverify")) {
                                Toast.makeText(context, "Otp verified successfully", Toast.LENGTH_SHORT).show();
                                goToHomeScreen();
                                //todo {}
                            } else if (action.equals("resendotp")) {
                                Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                //todo {}
                            } else if (action.equals("refresh")) {
                                //todo {status:0|1} done
                            } else if (action.equals("logout")) {
                                listener.logout(json);
                            } else if (action.equals("viewprofile")) {
                                listener.viewProfile(json);
                            } else if (action.equals("viewwallet")) {
                                listener.viewWallet(json);
                            }
                        }
                    }

                    listener.errorDialog(json);

                } else {
                    listener.errorDialog(json);
                }

            } else {
                PrefUtils.getInstance().setUserId(null);
                PrefUtils.getInstance().setUserData(null);
                context.startActivity(new Intent(context, LoginActivity.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
