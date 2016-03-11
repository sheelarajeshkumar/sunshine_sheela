package rajesh.app.com.sunshine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.*;
import android.os.Process;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   // private ShareActionProvider mShareActionProvider;
//  private Intent mShareIntent;
    String city="";
    ListView lv;
    Spinner sp;
    ImageView img;
    TextView tt;
    ArrayAdapter<String> adapter;
  //  CustomListAdapter c_adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    CustomAdapter cst=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);
        sp = (Spinner) findViewById(R.id.spinner);
        img = (ImageView) findViewById(R.id.imageView);
        tt = (TextView) findViewById(R.id.textView);


        //Share Menu
        /*mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");*/

        //custom title bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.title);
        //actionBar.setTitle("Weather Report");



        //setting Image
        //img.setImageResource(R.drawable.one_d);


        cst= new CustomAdapter(this,new ArrayList<String>(),new ArrayList<Integer>(),new ArrayList<String>());
        // Inserting data into listview
       // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>());

        lv.setAdapter(cst);
        //lv.setAdapter(adapter);

        // Spinner code for all list of city's
        AllCitynames at = new AllCitynames(sp, this);
        at.execute();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                city = arg0.getItemAtPosition(arg2).toString();
                Constants ct;
                tt.setText(city);
                if (city.equals("---- Select City ----")) {
                    city="India";
                    tt.setText("India");
                    ct = new Constants("India");
                    DataHttpToJson dbj = new DataHttpToJson(ct.getAPI(), lv, getApplicationContext(), cst, img);
                    dbj.execute();
                } else {
                    ct = new Constants(city);
                    //  Log.e("TEST", "onItemSelected: "+ct.getAPI() );
                    DataHttpToJson dbj = new DataHttpToJson(ct.getAPI(), lv, getApplicationContext(), cst, img);
                    dbj.execute();
                }

                // Toast.makeText(getBaseContext(), "You have selected item : "+city, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final Intent it=new Intent(this,Screen2.class);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lv.getItemAtPosition(position);

                it.putExtra("text",itemValue);
                it.putExtra("city",city);
                it.putExtra("position",position);
                startActivity(it);
                // Show Alert
                Toast.makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_SHORT).show();

            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //notigication
        Notify("weather report","Check Sunshine app for latest weather report");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
       /* MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Connect the dots: give the ShareActionProvider its Share Intent
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(mShareIntent);
        }*/

        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            android.os.Process.killProcess(Process.myPid());
        }
        if (id == R.id.refresh) {
            AllCitynames al= new AllCitynames(sp,this);
            al.execute();
            Constants ct = new Constants(tt.getText().toString());
            DataHttpToJson dbj = new DataHttpToJson(ct.getAPI(), lv, getApplicationContext(), cst, img);
            dbj.execute();
        }

        if (id == R.id.settings) {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if(id==R.id.menu_item_share){
            //mShareIntent.putExtra(Intent.EXTRA_TEXT, "Weather Forecast for coming 7 days of city " + tt.getText().toString());
            /*RelativeLayout L1=(RelativeLayout) findViewById(R.id.rec);
            View v1 = L1.getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bm = v1.getDrawingCache();
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bm);*/

            takeScreenshot();
            //mShareIntent.putExtra(Intent.EXTRA_STREAM,bitmapDrawable);
            //startActivity(mShareIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void Notify(String notificationTitle, String notificationMessage){
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification noti = new Notification.Builder(this)
                .setTicker(notificationTitle)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSmallIcon(R.drawable.one_d)
                .setContentIntent(pIntent).getNotification();
        noti.flags=Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/Pictures/Screenshots/" + now + ".jpg";

            // create bitmap screen capture
//            RelativeLayout L1=(RelativeLayout) findViewById(R.id.rec);
//            View v1 = L1.getRootView();
           View v1 = getWindow().getDecorView().getRootView();

            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://rajesh.app.com.sunshine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://rajesh.app.com.sunshine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
