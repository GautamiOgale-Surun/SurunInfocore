package com.example.surun.suruninfocore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.Animation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Login extends ActionBarActivity {


    final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    private static final String TAG = "myAppSurun";

    //Drawer
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    //End Drawer
    private Button createacc;
    private TextView trouble;
    private EditText uname;
    private EditText pass;

    //Asynchronous task variable
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    private static String url_all_login = "http://surun.co/demo/rest/login";


    //Globalstring
    String username =null;
    String password = null;

    //Global Variable for login  state checking
    public static boolean loginflag = false;
//    RelativeLayout relativeLayout=new RelativeLayout(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Surun Support");

        // Start Drawer Settings
        mDrawerList = (ListView) findViewById(R.id.navListlog);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_login);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F58634")));
        // End of Drawer Settings

        //Click Text Animation
        final Animation myanim, imganim;
        myanim = AnimationUtils.loadAnimation(this, R.anim.link_text_anim);
        imganim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        //End of animation

        //Initialize the component
        final TextView txtview = (TextView) findViewById(R.id.Login);
        TextView create=(TextView)findViewById(R.id.createacc);
        uname = (EditText) findViewById(R.id.edituser);
        uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                validation.isValid(uname, ".+@.+\\.[a-z]+", "Invalid UserName", true);

            }
        });
        pass = (EditText) findViewById(R.id.editpassword);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validation.isValid(pass, "[0-9]{10}", "Invalid Mobile No", true);
            }
        });
        final ImageView ivlogo = (ImageView) findViewById(R.id.imageView2);

        trouble = (TextView) findViewById(R.id.trouble_login);
        //end of initializing component

        //Creating underlined text
        String udata = "Create Account";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        create.setText(content);


        //end of creating underlined text
        Toast.makeText(getApplicationContext(), "Create", Toast.LENGTH_LONG).show();
        Log.v("TEST", "TEST");
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Create Click", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Registration_user.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);


            }
        });//End of On click for button

        //starting font settings this has prone to error try catch is mandatory while setting font(Overriding native font interface).
       try {
            Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/robotoregular.ttf");
            create.setTypeface(myTypeface);
        } catch (Exception e) {
            Log.v(TAG, "Exception " + e);
        }
        //End of font settings

        //Initializing shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //load preferences if exist
        String shareduser = sharedpreferences.getString("User", "");
        String sharedpass = sharedpreferences.getString("Password", "");
        if ((shareduser.length() > 0) || (sharedpass.length() > 0)) {
            //Navigating to main page
            Intent i = new Intent(getApplicationContext(), UserLogedIn.class);
            i.putExtra("user", shareduser);
            i.putExtra("pass", sharedpass);
            //Starting An Activity
            startActivity(i);
            //finish();
        } else {
         txtview.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 if (uname.getText().length() <= 0 || pass.getText().length() <= 0) {
                     Toast toast = Toast.makeText(getApplicationContext(), "All Fields Are Mandatory", Toast.LENGTH_SHORT);
                     toast.show();
                     trouble.setVisibility(View.VISIBLE);
                     if (pass.getText().length() > 10) {
                         Toast toast1 = Toast.makeText(getApplicationContext(), "Four Characters Only...", Toast.LENGTH_SHORT);
                         toast.show();
                         trouble.setVisibility(View.VISIBLE);
                         //Log.v(TAG,"Not Valid");

                     }
                 } else {
                     //Animate Button load animation from anim/rotate.xml
                     ivlogo.startAnimation(imganim);
                     username = uname.getText().toString();
                     password = pass.getText().toString();
                     //Sending Login Request To Server for validation Using Asynchronus Tasks where username and password as a parameter to method
                     Log.v(TAG, "Excuting check detail");
                     new CheckDetail().execute();
                     //Creating Shared Preferences


                 }//end of else_if fields are valid


             }
         });//End of On click for button
        }//If no shared preferences found

    }//End of onCreate function

    /* public void startregistr(View v){
         Log.v("IN CREATE", "TEST");
          sharedpreferences=getSharedPreferences( MyPREFERENCES, Context.MODE_PRIVATE);
          String name=sharedpreferences.getString("name","");
          String email=sharedpreferences.getString("email","");
          String mobile=sharedpreferences.getString("mobile","");
          Intent i = new Intent(getApplicationContext(), Registration_user.class);
          startActivity(i);
          finish();
          Toast.makeText(getApplicationContext(),"Click on Create",Toast.LENGTH_LONG).show();
          Log.v(TAG,"Error Occured");
      }*/
    private void addDrawerItems() {
        String[] menuitem = {"Home", "NewTicket", "MyTickets", "About Us", "Logout"};
        Integer[] imgid = {
                R.drawable.pc1,
                R.drawable.pc2,
                R.drawable.pc3,
                R.drawable.pc4,
                R.drawable.pc5,

        };
        CustomListAdapter adapter = new CustomListAdapter(this, menuitem, imgid);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {

                }
            }
        });
    }//End of Add Drawer Items

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Dashboard!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Surun Infocore");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }//End of SetupDrawer

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }//End od OnPostCreate

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }//End of onConfigurationChanged

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_home, menu);
        return true;
    }//End of onCreateOptionMenu

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

        //Remove Following Comment To Enable Drawer Toggling On Login Page

        /*// Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }//End of onOptionItemSelected

    /**
     * Background Async Task to Login by making HTTP Request
     */

    class CheckDetail extends AsyncTask<String,String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            try {
                Log.v(TAG, "In Do in Background");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", username));
                params.add(new BasicNameValuePair("pwd", password));
                // getting JSON string from URL
                JSONObject json =jParser.makeHttpRequest(url_all_login, "POST", params);
                //Object Parsing Failed here hence by using trail guide using JSONParser.alternateJSONArray to parse user data.
                //Hence not using json instance of object using a BACKUP static variable of Parser class for proccessing.
                //To use this backup utility theme the process should be standard and return unique or two out put only or ether way use three logical step
                if(json != null) {
                    // As if login fails it returns object handling fail logic here.
                    //We can make it general by sending array from server side so we can only use alternateJSONArray variable
                }
                if(JSONParser.alternateJSONArray != null)
                {
                   Log.v(TAG,"USING BACKUP ARRAY");
                   //Check your log cat for JSON futher details
                    for (int jsonArrayElementIndex=0; jsonArrayElementIndex < JSONParser.alternateJSONArray.length(); jsonArrayElementIndex++) {
                        JSONObject jsonObjectAtJsonArrayElementIndex = JSONParser.alternateJSONArray.getJSONObject(jsonArrayElementIndex);
                        if(jsonObjectAtJsonArrayElementIndex.getString("email").equals(username) && jsonObjectAtJsonArrayElementIndex.getString("mobile").equals(password))
                        {
                            Log.v(TAG,"Login Successful Now setting loginflag true");
                            loginflag = true;
                        }
                    }
                }
                else
                {
                    //JSON is null ether no data or 204 returned by server
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.v(TAG, "Exception at end :" + e.toString());
                //Log.e("TAG", "Error......!RecoverIt");
            }

            return null ;
        }


        protected void onPostExecute(String result)
        {
            // dismiss the dialog after getting all products
             //super.onPostExecute();
           // pDialog.dismiss();

            if(loginflag==true)
            {
                Log.v(TAG, "Executing Shared Preferences...");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("User", uname.getText().toString());
                editor.putString("Password", pass.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(), "Login Succeed",Toast.LENGTH_SHORT).show();
                android.util.Log.v(TAG, "Login Succeed");
                Intent i = new Intent(getApplicationContext(), UserLogedIn.class);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Login failed,Invalid Details...!",Toast.LENGTH_LONG).show();
                trouble.setVisibility(View.VISIBLE);
            }
            pDialog.dismiss();
            }

        }

        String getMD5(String pass) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(pass.getBytes());
                BigInteger number = new BigInteger(1, messageDigest);
                String hashtext = number.toString(16);
                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                return hashtext;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
   /* private int returnParsedJsonObject(String result) {

            JSONObject resultObject = null;

            int returnedResult = 0;

            try {

                resultObject = new JSONObject(result);

                returnedResult = resultObject.getInt("success");

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return returnedResult;

        }*/
    }

