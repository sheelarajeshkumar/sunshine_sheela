package rajesh.app.com.sunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rajeshkumarsheela on 3/3/16.
 */
public class AllCitynames extends AsyncTask<String,String,String> {

    Spinner sp;
    Context context;
    ArrayAdapter<String> dataAdapter;
    public AllCitynames(Spinner spinner,Context ct){
        this.sp=spinner;
        this.context=ct;
    }
    List<String> categories = new ArrayList<String>();
    HashMap<String,String> mapCity= new HashMap<>();
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;
    @Override
    protected String doInBackground(String... params) {
        try {
            // System.out.println(api);
            URL url = new URL("http://openweathermap.org/help/city_list.txt");

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
                return "Stream problem";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int i=0;
            int count=0;
            while ((line = reader.readLine()) != null) {
                if (i++!=0){
                    String[] args=line.split("\t");
                    mapCity.put(args[0], args[1]);
                    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
                    //System.out.println(context.getResources().getString(R.string.pref_location_value) + " @@@ " + context.getResources().getString(R.string.pref_location_default));
                    String location=pref.getString(context.getResources().getString(R.string.pref_location_key),context.getResources().getString(R.string.pref_location_default));

                    if(location.equalsIgnoreCase(args[4])){
                        count++;
                        if (!args[1].equals("")) {
                            if (args[1].contains(" ")){
                                //System.out.println("!@#$%^&!@#$%^&!@#$%^@#$%^&*!@#$%^&*");
                                String[] removetabspace=args[1].split(" ");
                                    categories.add(removetabspace[0].toUpperCase()+removetabspace[1].toUpperCase());
                            }else{
                                categories.add(args[1].toUpperCase());
                            }
                        }
                    }
                    buffer.append(args[0] + "\n");
                }
            }

           // System.out.println(context.getResources().getString(R.string.pref_location_key) + " @@@ " + context.getResources().getString(R.string.pref_location_default));
           // System.out.println(count+" ############################################");
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return "Connected but no data";
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
        categories.add("---- Select City ----");
        Collections.sort(categories);
        dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Log.d("All list of city's", "doInBackground: "+forecastJsonStr);
        return forecastJsonStr;
    }


    @Override
    protected void onPostExecute(String s) {
        sp.setAdapter(dataAdapter);
    }
}
