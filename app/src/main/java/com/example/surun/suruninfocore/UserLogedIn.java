package com.example.surun.suruninfocore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ViewFlipper;
import android.view.GestureDetector;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;

import android.graphics.Bitmap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class UserLogedIn extends ActionBarActivity {

    private static final String TAG = "myAppSurun";
    // Global Component
    private TextView appstrip;
    SharedPreferences sharedspreference;
    SharedPreferences.Editor editor;
    //Drawer Variables
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    //End of Drawer Variable
    //Start of View Flipper Variable
    private static final int SWIPE_MIN_DISTANCE = 10;
    private static final int SWIPE_THRESHOLD_VELOCITY = 20;
    private ViewFlipper mViewFlipper;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    //End of View Flipper Variable
    //Create ticket activity variable
    Spinner problemspn;
    EditText probdesc;
    private Uri fileUri;
    private static final int CAMERA_REQUEST = 100;
    public static final int MEDIA_TYPE_IMAGE = 3;
    ImageView iv,iv1,iv2;
    int counter=1;
    Button btn;
    //Offer slider component
    Button load_img;
    ImageView imageOfferOne,imageOfferTwo,imageOfferThree;
    Bitmap bitmap;
    ProgressDialog pDialog;
    //Text View for image sizes
    TextView imgsiz1,imgsiz2,imgsiz3;
    TextView imgno1,imgno2,imgno3;
    //Submit Buttom from child 2
    Button submitData;
    String ba1,ba2,ba3;
    String problemType,description;

    //Url for file upload
    public static String URL = "http://surun.co/demo/rest/createTicket";

    //Global Offercount.
    public int offerCount=1;
    public boolean globalExcutionFlag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loged_in);
        Log.v(TAG, "Admin Logged_in Created");
        getSupportActionBar().setTitle("Surun Infocore");
        appstrip=(TextView)findViewById(R.id.app_strip);
        // Start Drawer Settings
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        //Child 1 variable
        //Load image from server variable(child1)
        load_img = (Button) findViewById(R.id.refreshimage);

        View offerViewOne = findViewById(R.id.offerNumberOne);
        View offerViewTwo = findViewById(R.id.offerNumberTwo);
        View offerViewThree = findViewById(R.id.offerNumberThree);

        imageOfferOne = (ImageView)offerViewOne.findViewById(R.id.ofr_img);
        imageOfferTwo = (ImageView)offerViewTwo.findViewById(R.id.ofr_img);
        imageOfferThree = (ImageView)offerViewThree.findViewById(R.id.ofr_img);
        //End of Child 1 variable

        //Child 2 variable
        //camera capture variable(child2)
        View pic1=findViewById(R.id.imgvw1);
        View pic2=findViewById(R.id.imgvw2);
        View pic3=findViewById(R.id.imgvw3);

        iv=(ImageView)pic1.findViewById(R.id.camcap);
        imgsiz1=(TextView)pic1.findViewById(R.id.sizeofimage);
        imgno1=(TextView)pic1.findViewById(R.id.imgnumber);
        imgno1.setText("1");

        iv1=(ImageView)pic2.findViewById(R.id.camcap);
        imgsiz2=(TextView)pic2.findViewById(R.id.sizeofimage);
        imgno2=(TextView)pic2.findViewById(R.id.imgnumber);
        imgno2.setText("2");

        iv2=(ImageView)pic3.findViewById(R.id.camcap);
        imgsiz3=(TextView)pic3.findViewById(R.id.sizeofimage);
        imgno3=(TextView)pic3.findViewById(R.id.imgnumber);
        imgno3.setText("3");

        submitData=(Button)findViewById(R.id.submitDataTicket);
        probdesc=(EditText)findViewById(R.id.case_description);

        //End of child 2 variable

        //Basic Drawer Settings
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F58634")));
        //End of Drawer Settings

        //Start View Flipper Settings and Functionality
        mViewFlipper = (ViewFlipper)findViewById(R.id.MenuFlipperr);
        Log.v(TAG, "Setting On touch listener is Ready...");
        mViewFlipper.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                //Log.v(TAG, "Swipe Event is Executing...");
                return true;
            }

        });

        load_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //Replace these links to load respective order.
                //While uploading image from server these name and format should be followed.
                //for(offerCount=1; offerCount <= 3; offerCount++) {
                    new LoadImage().execute("http://www.surun.co/tuljaifilms/images/SS4.jpg");
                }
                /*
                    Log.v(TAG,"OfferCounter :"+offerCount);
                    new LoadImage().execute("http://www.surun.co/tuljaifilms/images/SS4.jpg");
                    offerCount++;
                 */


                //}

        });


        //Adding value to problem Spinner-child 2
        problemspn=(Spinner)findViewById(R.id.problemspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.topics, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        problemspn.setAdapter(adapter);

        btn=(Button)findViewById(R.id.camera);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=1;
                do {
                    Log.v(TAG, "Before Intent");
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    Log.v(TAG, "Intent Created");
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);// set the image file name
                    startActivityForResult(intent, CAMERA_REQUEST);
                    count++;
                }while(count!=3);
            }
        });
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Write the code for remaining two image files this is a code for
                Log.v(TAG,"Starting Upload Process..!");
                Bitmap bm = BitmapFactory.decodeFile("\\Phone\\Pictures\\myAppSurun/IMG_one.jpg");
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                byte[] ba = bao.toByteArray();
                ba1 = Base64.encodeBytes(ba);
                Log.v(TAG,"File Found successfully..");
                Log.v(TAG, "-----" + ba1);

                Bitmap bm1 = BitmapFactory.decodeFile("\\Phone\\Pictures\\myAppSurun/IMG_two.jpg");
                ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bao1);
                byte[] baon = bao1.toByteArray();
                ba2 = Base64.encodeBytes(baon);
                Log.v(TAG,"File Found successfully..");
                Log.v(TAG, "-----" + ba2);

               Bitmap bm2 = BitmapFactory.decodeFile("/phone/IMG_three.jpg");
                ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bao2);
                byte[] batw = bao2.toByteArray();
                ba3 = Base64.encodeBytes(ba);
                Log.v(TAG,"File Found successfully..");
                Log.v(TAG, "-----" + ba3);
                // Upload image to server
                new uploadToServer().execute();
            }
        });
        //Setting Images if exists
        File imgFile1 = new File("\\Phone\\Pictures\\myAppSurun/IMG_one.jpg");
        File imgFile2=new File("\\Phone\\Pictures\\myAppSurun/IMG_two.jpg");
        File imgFile3=new File("\\Phone\\Pictures\\myAppSurun/IMG_three.jpg");
        if (imgFile1.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
            long sizeoffile=imgFile1.length();
            float sizeinkb=((sizeoffile/1024));
            float sizeinmb=((sizeinkb/1024));
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 50, 50, true);
            iv.setImageBitmap(myBitmap);
            String s=String.format("%.1f",sizeinmb);
            imgsiz1.setText("("+s+"MB)");
            Log.v(TAG, "IMG_ONE Settled...");
        }
        if (imgFile2.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
            long sizeoffile=imgFile2.length();
            float sizeinkb=((sizeoffile/1024));
            float sizeinmb=((sizeinkb/1024));
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 50, 50, true);
            iv1.setImageBitmap(myBitmap);
            Log.v(TAG, "IMG_TWO Settled...");
            String s=String.format("%.1f",sizeinmb);
            imgsiz2.setText("("+s+"MB)");
        }
        if (imgFile3.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());
            long sizeoffile=imgFile3.length();
            float sizeinkb=((sizeoffile/1024));
            float sizeinmb=((sizeinkb/1024));
            myBitmap = Bitmap.createScaledBitmap(myBitmap, 50, 50, true);
            iv2.setImageBitmap(myBitmap);
            Log.v(TAG, "IMG_THREE Settled...");
            String s=String.format("%.1f",sizeinmb);
            imgsiz3.setText("("+s+"MB)");
        }

    }

    private void addDrawerItems() {
        String[] menuitem = { "Home", "NewTicket", "MyTickets", "About Us", "Logout" };
        Integer[] imgid={
                R.drawable.pc1,
                R.drawable.pc2,
                R.drawable.pc3,
                R.drawable.pc4,
                R.drawable.pc5,

        };

        CustomListAdapter adapter=new CustomListAdapter(this, menuitem, imgid);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child1)));
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    appstrip.setText("Create new ticket, Get in touch with us with grace");
                }
                if(position==1)
                {
                    Toast.makeText(UserLogedIn.this, "Clicked On New Ticket", Toast.LENGTH_SHORT).show();
                    mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.child2)));
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    appstrip.setText("Create new ticket, Get in touch with us with grace");
                }
            }
        });
    }

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
    }

    private static Uri getOutputMediaFileUri(int type){
        Log.v(TAG, "Creating URI Stuff....");
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),TAG);
        Log.v(TAG,"Directory creation");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.v(TAG, "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {

            File imgFile = new File("\\Phone\\Pictures\\myAppSurun/IMG_one.jpg");
            File imgFile1=new File("\\Phone\\Pictures\\myAppSurun/IMG_two.jpg");
            File imgFile2=new File("\\Phone\\Pictures\\myAppSurun/IMG_three.jpg");
            if (!imgFile.exists()) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_one" + ".jpg");
                Log.v(TAG, "IMG_ONE created...");
            }
            else if(imgFile1.exists())
            {
                mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG_three"+".jpg");
                Log.v(TAG, "IMG_THREE created...");
            }
            else if(imgFile.exists() && !imgFile1.exists())
            {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_two" + ".jpg");
                Log.v(TAG, "IMG_TWO created...");
            }
            else
            {
                mediaFile=new File(mediaStorageDir.getPath()+ File.separator+"IMG_one"+".jpg");
                Log.v(TAG, "IMG_ONE ELSE  created...");
            }

        }
        else
        {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "Before request...");
      //  int count=1;

              if (requestCode == CAMERA_REQUEST) {
                  Log.v(TAG, "Camera request iff...");
                  if (resultCode == RESULT_OK) {
                      Log.v(TAG, "Result Ok pre...");

                      try {
                          File imgFile = new File("\\Phone\\Pictures\\myAppSurun/IMG_one.jpg");
                          if (imgFile.exists()) {
                              Log.v(TAG, "IMG_ONE File Exists...");
                              Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                              myBitmap = Bitmap.createScaledBitmap(myBitmap, 50, 50, true);
                              iv.setImageBitmap(myBitmap);
                              long filesize = imgFile.length();
                              long sizeinmb = ((filesize / 1024) / 1024);
                              imgsiz1.setText("" + sizeinmb);

                          }


                          File imgFile1 = new File("\\Phone\\Pictures\\myAppSurun/IMG_two.jpg");
                          if (imgFile1.exists()) {
                              Log.v(TAG, "IMG_TWO File Exists...");
                              Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
                              myBitmap = Bitmap.createScaledBitmap(myBitmap, 50, 50, true);
                              iv1.setImageBitmap(myBitmap);
                              long filesize = imgFile1.length();
                              long sizeinmb = ((filesize / 1024) / 1024);
                              imgsiz1.setText("" + sizeinmb);

                          }


                          File imgFile2 = new File("\\Phone\\Pictures\\myAppSurun/IMG_three.jpg");
                          if (imgFile2.exists()) {
                              Log.v(TAG, "IMG_THREE File Exists...");
                              Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
                              myBitmap = Bitmap.createScaledBitmap(myBitmap, 50, 50, true);
                              iv2.setImageBitmap(myBitmap);
                              long filesize = imgFile2.length();
                              long sizeinmb = ((filesize / 1024) / 1024);
                              imgsiz1.setText("" + sizeinmb);
                          }

                          // List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);


                      } catch (Exception e) {
                          Log.v(TAG, "Exception " + e);
                      }
                  } else if (resultCode == RESULT_CANCELED) {
                      Toast.makeText(this, "Image save canceled", Toast.LENGTH_LONG).show();
                  } else {
                      Toast.makeText(this, "Image save failed", Toast.LENGTH_LONG).show();
                  }
              }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_loged_in, menu);
        return true;
    }


    public void onItemStateChanged(MenuItem menu)
    {
        SharedPreferences.Editor editor=sharedspreference.edit();
        if (editor!=null)
        {
           // editor.clear();
            editor.commit();

            setContentView(R.layout.activity_login);
            Intent i = new Intent(UserLogedIn.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SwipeGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out));
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_out));
                    mViewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }//End Of Inner class SwipeGestureDetector

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v(TAG, "In onPreExecute of the loading offers page.");
            pDialog = new ProgressDialog(UserLogedIn.this);
            pDialog.setMessage("Loading Offers ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                Log.v(TAG, "In doInBackground of the loading offers page. ");
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                bitmap = Bitmap.createScaledBitmap(bitmap, 270, 375, true);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

                if (image != null) {
                    if(offerCount == 1)
                        imageOfferOne.setImageBitmap(image);
                    else if(offerCount == 2)
                        imageOfferTwo.setImageBitmap(image);
                    else if (offerCount == 3)
                        imageOfferThree.setImageBitmap(image);
                    pDialog.dismiss();
                    offerCount++;
                    if(offerCount < 4)
                    new LoadImage().execute("http://www.surun.co/tuljaifilms/images/SS4.jpg");
                } else {
                    pDialog.dismiss();
                    Toast.makeText(UserLogedIn.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

                }
        }
    }//End of Inner class Load image

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(UserLogedIn.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            Log.v(TAG, "On pre executing process..!");
            pd.show();
            Log.v(TAG, "Showing process dialog..!");
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            problemType=problemspn.getSelectedItem().toString();
            description=probdesc.getText().toString();

            //Image One....
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            nameValuePairs.add(new BasicNameValuePair("desc",description));
            nameValuePairs.add(new BasicNameValuePair("probtype",problemType));
             nameValuePairs.add(new BasicNameValuePair("userid","1"));

           //Image Two....
            nameValuePairs.add(new BasicNameValuePair("base64", ba2));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            nameValuePairs.add(new BasicNameValuePair("desc",description));
            nameValuePairs.add(new BasicNameValuePair("probtype",problemType));
            nameValuePairs.add(new BasicNameValuePair("userid","1"));

            // Image Three....
            nameValuePairs.add(new BasicNameValuePair("base64", ba3));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            nameValuePairs.add(new BasicNameValuePair("desc",description));
            nameValuePairs.add(new BasicNameValuePair("probtype",problemType));
            nameValuePairs.add(new BasicNameValuePair("userid","1"));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v(TAG, "In the try Loop" + st);
                Log.v(TAG,"Ticket Created");

            } catch (Exception e) {
                Log.v(TAG, "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
}
