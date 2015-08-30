package com.example.surun.suruninfocore;

import android.app.Activity;
import android.app.Fragment;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import  com.android.volley.toolbox.StringRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.lang.String;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.example.surun.suruninfocore.httpservice;
import  com.example.surun.suruninfocore.*;
import com.example.surun.suruninfocore.CustomListAdapter;
import com.example.surun.suruninfocore.Registration_user;

import javax.xml.transform.ErrorListener;

 /* Created by USER on 8/3/2015.
 */


public class httpservice extends IntentService {
    EditText editotp;
    Button btnverify;
    JSONParser jpar;
  //  public static final String URL_REQUEST_SMS = "http://192.168.0.101/android_sms/msg91/request_sms.php";
    public static final String URL_VERIFY_OTP ="http://surun.co/demo/rest/verifyMobile";
    Response response;
    private static String TAG = httpservice.class.getSimpleName();

    public httpservice() {
        super(httpservice.class.getSimpleName());
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");

        }
    }
    protected String doInBackground()
    {
        String result1="";
        String otp1=editotp.getText().toString();
        List params=new ArrayList<>();
        params.add(new BasicNameValuePair("",otp1));
      JSONObject jsonArray=jpar.makeHttpRequest(URL_VERIFY_OTP,"POST",params);
        result1=jsonArray.toString();
        try
        {
            jpar.toString();
            String nSuccess=jpar.toString();
            int sc;
            sc=Integer.parseInt(nSuccess);
            Intent i=new Intent(httpservice.this,UserLogedIn.class);
            startActivity(i);
           // (R.layout.verification_step2);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
      return  null;
    }


}

