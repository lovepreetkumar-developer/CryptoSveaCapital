package com.svea.app.server;

import android.os.AsyncTask;
import android.util.Log;

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

public class YKHBackup {

    static String server(String url,JSONObject data){
        String res="";
        return res;

    }
    public static void web(final String url1, final String method, final CookieManager cookieManager, final String rawdata, final File ifile[], final File ofile){
        Map<String, Object> myMap = new HashMap<String, Object>() {{
            put("url", url1);
            put("method", method);
            put("cookieManager", cookieManager);
            put("rawdata", rawdata);
            put("ifile", ifile);
            put("ofile", ofile);
        }};
        new WebTask().execute(myMap);
        return;
    }

    public static class WebTask extends AsyncTask<Map<String, Object>, Integer, WebTask.Result> {
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

                String resultString = web((String)map[0].get("url"),(String)map[0].get("method"),(CookieManager) map[0].get("cookieManager"),(String)map[0].get("rawdata"),(File[])map[0].get("ifile"),(File)map[0].get("ofile"));
                if (resultString != null) {
                    result = new Result(resultString);
                } else {
                    throw new IOException("No response received.");
                }
            } catch(Exception e) {
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

        }
        @Override
        protected void onCancelled(Result result) {
        }
        public String web(String url1, String method, CookieManager cookieManager,String rawdata, File ifile[], File ofile) throws IOException {
            //Log.i("ttt","*");

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            InputStream inputStream=null,inputStream1=null;
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024;
            try {
                //conn.setReadTimeout(3000);
                //conn.setConnectTimeout(3000);
                // Creating HTTP client

                conn = (HttpURLConnection) (new URL(url1)).openConnection();
                String params="";
                if(method!="POST"){method="GET";}
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

                if(method=="POST"){
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    //conn.setRequestProperty("Content-Type","multipart/form-data");// "application/x-www-form-urlencoded");


                    if(rawdata!=null) {
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=--Boundary");
                        rawdata="----Boundary\r\nContent-Disposition: form-data; name=\"data\"\r\n\r\n"+rawdata+"\r\n----Boundary--";

                        conn.setRequestProperty("Content-length", rawdata.getBytes().length + "");
                        //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
                        //conn.setChunkedStreamingMode(0);

                        //conn.setRequestProperty("bill", "contact.txt");
                        //String param="param1=" + URLEncoder.encode("value1","UTF-8")+"&param2="+URLEncoder.encode("value2","UTF-8");
                        //conn.setFixedLengthStreamingMode(param.getBytes().length);

                        Log.i("TAG",rawdata);


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
                if(inputStream!=null){inputStream.close();}
                inputStream = conn.getInputStream();
                buffer = new byte[4096];
                //Object result="";

                StringBuilder result=new StringBuilder();
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    result.append(new String(buffer,0,bytesRead,"utf-8"));
                }
                //Log.i("TAG",result.toString());
                // procbatch(result.toString());
            }catch(Exception ex){
                //Log.e("ttt",ex.getMessage());
            } finally {
                // Close Stream and disconnect HTTPS conn.
                if (inputStream != null) {
                    inputStream.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
                if(dos!=null){
                    dos.flush();
                    dos.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
            }
            return null;
        }
    }

    //this will be change
    static JSONObject ajaxJSON(JSONObject object) {
        String url = "https://domain.com/server/";
        try {
            JSONObject jsonObject = new JSONObject(server(url,object));
            String sid = jsonObject.get("sid").toString();
            if (sid != null) {
                if (!sid.equals(PrefUtils.getInstance().getUserId())){
                    PrefUtils.getInstance().setUserId(sid);//don't worry i will do it
                }
                //this is saying sid will no null anytime
                //sir dont just comment , do the code here so that i can understand
                if(jsonObject.get("status").toString().equals("OK")){
                    //here what to do
                    if (jsonObject.get("value") != null){
                       return  (JSONObject) jsonObject.get("value");
                    }

                    if (jsonObject.get("msg") != null)
                    {
                        //Open succ dialog and print jsonObject.get("msg").toString()
                    }
                }else {
                    //here what to do
                    if (jsonObject.get("msg") != null)
                    {
                        //Open eror dialog and print jsonObject.get("msg").toString()
                    }
                }

            } else {
                PrefUtils.getInstance().setUserId(null);//don't worry i will do it
                //startActivity(new Intent(getActivity(), LoginActivity.class));
                //goNextTransition();
                //bjects.requireNonNull(getActivity()).finishAffinity();
                return null;
            }

        } catch (JSONException e) {
            //Open eror dialog and print e.printStackTrace();
            return  null;
        }
        return null;

    }

    static void yks_register(String fname, String lname, String email, String phone, String password, String cpassword) {
/*
        try {
            JSONObject jsonObject = ajaxJSON(String.valueOf((new JSONObject()).put("action", "register").put("param", (new JSONArray()).put(fname).put(lname).put(email).put(phone).put(password).put(cpassword))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        //todo {sid:null|"session id",status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    public static void userLogin(String userid, String password) {
        try {
            JSONObject jsonObject = ajaxJSON((new JSONObject()).put("action", "login")
                    .put("param", (new JSONArray()).put(userid).put(password)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //todo {status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    static void yks_otpverify(String otp) {
        try {
            JSONObject jsonObject = ajaxJSON((new JSONObject()).put("action", "otpverify").put("param", (new JSONArray()).put(otp)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //todo {sid:null|"session id",status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    static void yks_resendotp() {
/*
        try {
            JSONObject jsonObject = ajaxJSON((new JSONObject()).put("action", "resendotp").put("param", (new JSONArray()).put()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        //todo {sid:null|"session id",status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    static void yks_logout() {
/*
        try {
            JSONObject jsonObject = ajaxJSON((new JSONObject()).put("action", "logout").put(param, (new JSONArray()).put()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        //todo {sid:null,status:"OK"|"Error",msg:"Message on Error",value:[]}
    }

    static void yks_viewprofile() {
/*        try {
            JSONObject jsonObject = ajaxJSON((new JSONObject()).put("action", "viewprofile").put("param", (new JSONArray()).put()));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //todo {sid:null,status:"OK"|"Error",msg:"Message on Error",value:[{fname:"fname",lname:"lname",email:"emailid@domain.com",phone:"phoneno"}]}
    }

    static void yks_viewwallet() {
        try {
            JSONObject jsonObject = (new JSONObject()).put("action", "viewwallet").put("param", new JSONArray("[]"));
           // JSONObject jsonObject = ajaxJSON(jsonObject);
            if(jsonObject!=null){
                //jsonObject.get("value").get("wallet")
            }
            //do the same for all

            //todo {sid:null,status:"OK"|"Error",msg:"Message on Error",value:[{wallet:amount}]}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

