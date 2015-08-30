package com.example.surun.suruninfocore;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.HttpResponse;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;


public class Registration_user extends ActionBarActivity{


     Button Submit;
    private ProgressDialog pdialog;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerLayout.DrawerListener mDrawerlistener;
    private ListView mDrawerList;
    private String mActivityTitle;
    final String MyPREFERENCES="mypref";
    SharedPreferences sharedpreferences;
    JSONParser jpar=new JSONParser();
    JSONArray jsonarr;
    JSONObject returnParsedJsonObject;
    public static final String TAG="MYAppSurun";

    EditText name;
    EditText email;
    EditText mobile;
    EditText otpcode;

   // Button reset;
    private static String url_link= "http://surun.co/demo/rest/createUser";
    //private  static String verify_link="http://surun.co/demo/rest/verifyMobile";
    String M_final="Successful Register";
    public static boolean flag1=false;

    protected  void verify_mob()
    {
        String otp= otpcode.getText().toString();
        if (!otp.isEmpty())
        {
            Intent veriI=new Intent(getApplicationContext(),httpservice.class);
            veriI.putExtra("",otp);
            startService(veriI);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Enter OTP",Toast.LENGTH_SHORT).show();
        }
    }


    /*private void setupDrawer() {
        mDrawerToggle =  new android.support.v4.app.ActionBarDrawerToggle(this, mDrawerLayout,R.string.drawer_open, R.string.drawer_close){

            /* Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Dashboard!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Surun Infocore");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }//End of SetupDrawer
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);

        name = (EditText) findViewById(R.id.name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validation.hasText(name);
                validation.isValid(name, "^[a-z_A-Z ]*$", "Only String", true);

            }
        });

        email = (EditText) findViewById(R.id.email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                validation.isValid(email, ".+@.+\\.[a-z]+", "Invalid email_ID", true);
            }
        });
        mobile = (EditText) findViewById(R.id.mobile);
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validation.isValid(mobile, "[7-9][0-9]{9}", "Invalid Mobile No", true);
            }
        });
        TextView signup = (TextView) findViewById(R.id.Submit);
        Button reset=(Button) findViewById(R.id.Reset);
        //pdialog.setMessage("Successfull Register");
        // pdialog.setProgressStyle(pdialog.STYLE_HORIZONTAL);
        // pdialog.show();


    }
   // Submit.OnClickListener(new View.OnClickListener(){
        public void onClick(View view) {
            new newRegister().execute();
        }
  //  });


    class newRegister extends AsyncTask<String,String,String>
    {

        private ProgressDialog pdialog;

        protected void onPreExcute()
        {
            super.onPreExecute();
           /* pdialog=new ProgressDialog(Registration_user.this);
            pdialog.setMessage("Registration User");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(true);
           // pdialog.show();*/
        }
        @Override
        protected String doInBackground(String... arg0) {
            String name1= name.getText().toString();
            String email1=email.getText().toString();
            String mobile1=mobile.getText().toString();

            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name1));
            params.add(new BasicNameValuePair("email", email1));
            params.add(new BasicNameValuePair("mobile", mobile1));
            JSONObject jsonArray =jpar.makeHttpRequest(url_link,"POST",params);
            try
            {
               jpar.toString();
                //String success=jpar.toString();
               // int sc;
               // sc=Integer.parseInt(success);
               // if (sc==1) {
                    Intent i = new Intent(getApplicationContext(),Login.class);
                  //  Toast.makeText(getApplicationContext(), "Create", Toast.LENGTH_LONG).show();
                    android.util.Log.v(TAG, "No device found to connect");
                    startActivity(i);
               // } else {

               // }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                //android.util.Log.v(TAG, "TEST");
            }
            return null;

        }
        protected void onPostExecute(String result)
        {

           // super.onPostExecute(result);
            System.out.println("Resulted Value" + result);
            if(result!=null)
            {
              Submit.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View view) {

                      SharedPreferences.Editor editor = sharedpreferences.edit();
                      editor.putString("name", name.getText().toString());
                      editor.putString("email", email.getText().toString());
                      editor.putString("mobile", mobile.getText().toString());

                      editor.commit();
                      android.util.Log.v(TAG, "SuccessFull Register");
                      // setContentView(R.layout.activity_login);
                      Intent i = new Intent(getApplicationContext(), Login.class);
                      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(i);
                      //finish();

                  }
              });


            }
//            pdialog.dismiss();

            //pdialog= new ProgressDialog();

            //Toast.makeText(getApplicationContext(),"Successfull Register",Toast.LENGTH_LONG).show();
           /*

            */

        }
}
   public void startregistr(View v){
        android.util.Log.v("IN CREATE", "TEST");
        sharedpreferences=getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE);
        String name=sharedpreferences.getString("name","");
        String email=sharedpreferences.getString("email","");
        String mobile=sharedpreferences.getString("mobile","");
      // Intent i = new Intent(getApplicationContext(), Registration_user.class);
        //startActivity(i);
        setContentView(R.layout.verification_step1);
        finish();
        Toast.makeText(getApplicationContext(),"Click on Create",Toast.LENGTH_LONG).show();
        android.util.Log.v(TAG, "Error Occured");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

