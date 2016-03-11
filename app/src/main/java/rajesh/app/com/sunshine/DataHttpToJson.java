package rajesh.app.com.sunshine;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rajeshkumarsheela on 3/3/16.
 */
public class DataHttpToJson extends AsyncTask<String,String,Void>{

    //private ArrayAdapter<String> adapter;
    private List<String> days;
    private List<String> cash;
    private CustomAdapter adapter;
    private String api;
    private List<String> st;
    private List<Integer> imd_list;
    private ListView lv;
    private Context context;
    private ImageView img;
    public DataHttpToJson(String api,ListView lv,Context context, CustomAdapter adapter,ImageView img){
        this.api=api;
        this.lv=lv;
        this.context=context;
        this.adapter=adapter;
        this.img=img;
        this.cash=new ArrayList<String>();
    }
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;

    @Override
    protected Void doInBackground(String... params) {
        try {
            // System.out.println(api);
            URL url = new URL(api);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // System.out.println("!@#$%^&*()_");

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
               // return "Stream problem";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                //return "Connected but no data";
            }
            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = null;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        JsonToView();
        return null;
    }

    String wet="";
    SimpleDateFormat ft;
    Date someDate = new Date();
    Date dayAfter;

    List<String> JsonToView(){
        int ig=0;
        ft= new SimpleDateFormat ("E dd");

        imd_list= new ArrayList<>();
        days=new ArrayList<>();
        JSONArray list;
        List<String> temp_arrayList= new ArrayList<>();
        try {
          //  System.out.println(forecastJsonStr);
            JSONObject reader = new JSONObject(forecastJsonStr);
            list  = reader.getJSONArray("list");
            //System.out.println("!!!!!!!!!!!!!!!!  "+list.length());
            for(int i=0;i<list.length();i++){
                JSONObject temp=list.getJSONObject(i);
                //System.out.println(temp.getString("dt")+" @@@@@@@@@@");
                JSONObject main=temp.getJSONObject("main");
                JSONArray weather=temp.getJSONArray("weather");
                String we="";
               for (int x=0;x<weather.length();x++){
                    JSONObject weee=weather.getJSONObject(x);
                   we=weee.getString("main");
               }
                if (ig++==0){
                    wet=we;
                    days.add(ft.format(someDate) + "\n" + we);
                }else{
                    days.add(ft.format(dayAfter) + "\n" + we);
                }
                dayAfter = new Date(someDate.getTime()+(24*60*60*1000));
                someDate=dayAfter;
                imd_list.add(imagGet(we));

                cash.add("Max:@"+main.getString("temp_max")+"@Min:@"+main.getString("temp_min")+"@Pressure:@"+main.getString("pressure")+"@Humidity:@"+main.getString("humidity"));
               /* System.out.println(main.getString("temp")+"  Main Temperature");
                System.out.println(main.getString("temp_min"));
                System.out.println(main.getString("temp_max"));
                System.out.println();*/
                temp_arrayList.add("Max:- "+main.getString("temp")+"\nMin:- "+main.getString("temp_min"));

            }
        } catch (JSONException e) {e.printStackTrace();}
return temp_arrayList;
    }


    @Override
    protected void onPostExecute(Void aVoid) {

        List<String> list=JsonToView();

        File myfile= new File(context.getCacheDir(),"mydata");
        ObjectOutputStream ob=null;
        try {
            ob = new ObjectOutputStream(new FileOutputStream(myfile));
            ob.writeObject(cash);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                ob.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (wet.equalsIgnoreCase("clear")){
            img.setImageResource(R.drawable.one_d);
        }else if (wet.equalsIgnoreCase("clouds")){
            img.setImageResource(R.drawable.two_d);
        }else if (wet.equalsIgnoreCase("scattered clouds")){
            img.setImageResource(R.drawable.three_d);
        }else if (wet.equalsIgnoreCase("broken clouds")){
            img.setImageResource(R.drawable.four_d);
        }else if (wet.equalsIgnoreCase("shower rain")){
            img.setImageResource(R.drawable.nine_d);
        }else if (wet.equalsIgnoreCase("rain")){
            img.setImageResource(R.drawable.ten_d);
        }else if (wet.equalsIgnoreCase("thunderstorm")){
            img.setImageResource(R.drawable.eleven_d);
        }else if (wet.equalsIgnoreCase("snow")){
            img.setImageResource(R.drawable.onethree_d);
        }else if (wet.equalsIgnoreCase("mist")){
            img.setImageResource(R.drawable.fivezero_d);
        }
        adapter.clear();
        adapter= new CustomAdapter(context,days,imd_list,list);
        lv.setAdapter(adapter);
    }

    Integer imagGet(String wet){
        Integer ir=0;
        if (wet.equalsIgnoreCase("clear")){
            ir=R.drawable.one_d;
        }else if (wet.equalsIgnoreCase("clouds")){
            ir=R.drawable.two_d;
        }else if (wet.equalsIgnoreCase("scattered clouds")){
            ir=R.drawable.three_d;
        }else if (wet.equalsIgnoreCase("broken clouds")){
            ir=R.drawable.four_d;
        }else if (wet.equalsIgnoreCase("shower rain")){
            ir=R.drawable.nine_d;
        }else if (wet.equalsIgnoreCase("rain")){
            ir=R.drawable.ten_d;
        }else if (wet.equalsIgnoreCase("thunderstorm")){
            ir=R.drawable.eleven_d;
        }else if (wet.equalsIgnoreCase("snow")){
            ir=R.drawable.onethree_d;
        }else if (wet.equalsIgnoreCase("mist")){
            ir=R.drawable.fivezero_d;
        }
        return ir;
    }

}
