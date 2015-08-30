package com.example.surun.suruninfocore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class JSONParser {

    static InputStream is;
    static JSONObject jObj;
    static String json;
    static JSONArray jarr;
    private static final String TAG = "myAppSurun";
    public static String alternateJSONString = "";
    public static JSONArray alternateJSONArray = null;
    // constructor
    public JSONParser() {
        is = null;
        jObj = null;
        json = "";
        jarr = null;
    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        // Making HTTP request
        try {

            // check for request method
            if(method.equalsIgnoreCase("POST")){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                Log.v(TAG, "Param String :" + url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                Log.v(TAG,"To the End of IF");

            }else if(method.equalsIgnoreCase("GET")){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                Log.v(TAG,"Param String :"+url);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                Log.v(TAG,"To the End of ELSE");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.v(TAG,"JS_PARSER : Server Response -"+json);
        } catch (Exception e) {
            Log.v(TAG,"Error in getting json string from response : A further try will be given to use json string "+e.toString());
        }
        // try parse the string to a JSON object/Array And String
        try {

            if(json != null) {
                alternateJSONString = json;
                jObj = new JSONObject(json);
                Log.v(TAG,"JS_PARSER : JSON Object Created Successfully Use Object instance");
            }
            else
            {
                Log.v(TAG,"JS_PARSER : JSON String is blank trying one more time to parse JSON String");
            }
        }
        catch (Exception e) {
            try{
            if(json != null) {
                Log.v(TAG,"JS_PARSER : Object Parsing failed Trying for Array if no more logs followed by \" JS_PARSER \" use JSONParser.alternateJSONArray for JSON instance. ");
                alternateJSONArray = new JSONArray(json);
            }
            else {
                Log.v(TAG,"JS_PARSER : JSON String is blank trying one last time to parse JSON String if no more logs followed by \" JS_PARSER \" use JSONParser.alternateJSONString for JSON instance");
            }
            }
            catch(Exception e1)
            {
                if(json !=null) {
                    Log.v(TAG, "JS_PARSER : Array Parsing also failed if no more logs followed by \" JS_PARSER \" use JSONParser.alternateJSONString for backup JSON string for results");
                }
                else {
                    Log.v(TAG, "JS_PARSER : Final try also failed Confirm server log for request error or follow following log trail"+e1);
                }
            }
        }
        // return JSON String
        return jObj;


    }
}