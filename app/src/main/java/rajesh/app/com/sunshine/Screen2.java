package rajesh.app.com.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class Screen2 extends AppCompatActivity {

    ImageView imgset;
    TextView day,t1,t2;

    int position=0;
    String city="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        imgset=(ImageView)findViewById(R.id.imageView3);
        day=(TextView) findViewById(R.id.textView4);
        t1=(TextView) findViewById(R.id.textView5);
        t2=(TextView) findViewById(R.id.textView6);

        position=getIntent().getExtras().getInt("position");
        city=getIntent().getExtras().getString("city");

        String text=getIntent().getExtras().getString("text");
        String[] array=text.split("\n");

        //t1.setText(position+"\n"+city);
        day.setText(array[0]);
        imgset.setImageResource(imagGet(array[1]));
        Fechdata(position);
    }


    void Fechdata(int pos){
        File myfile= new File(getCacheDir(),"mydata");
        ObjectInputStream ob=null;
        List<String> ls=null;
        try {
            ob= new ObjectInputStream(new FileInputStream(myfile));
            ls= (List<String>) ob.readObject();
            ob.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String[] ar=ls.get(pos).split("@");
        for(int i=0;i<ar.length;i++){
            if(i%2==0){
                t1.append(ar[i]+"\n");
            }else{
                t2.append(ar[i]+"\n");
            }
        }

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
