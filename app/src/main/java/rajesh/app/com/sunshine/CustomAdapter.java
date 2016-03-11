package rajesh.app.com.sunshine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rajeshkumarsheela on 3/7/16.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    private List<String> st;
    private List<Integer> imgs;
    private List<String> temps;
    private Context context;
    public CustomAdapter(Context context,List<String> st,List<Integer> imgs,List<String> temps) {
        super(context,R.layout.single_row,R.id.textView2,st);
        this.st=st;
        this.imgs=imgs;
        this.context=context;
        this.temps=temps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row= inf.inflate(R.layout.single_row,parent,false);
        ImageView img=(ImageView) row.findViewById(R.id.imageView2);
        TextView txt=(TextView) row.findViewById(R.id.textView2);
        TextView min_max=(TextView) row.findViewById(R.id.textView3);

        img.setImageResource(imgs.get(position));
        txt.setText(st.get(position));
        min_max.setText(temps.get(position));
        return row;
    }


}
