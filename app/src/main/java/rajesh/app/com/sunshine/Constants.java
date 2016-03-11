package rajesh.app.com.sunshine;

/**
 * Created by rajeshkumarsheela on 3/3/16.
 */
public class Constants {

    public Constants(String temp){
        this.CITY=temp;
        this.API="http://api.openweathermap.org/data/2.5/forecast?q="+CITY+"&appid=44db6a862fba0b067b1930da0d769e98&mode=json&units=metric&cnt=7";
    }
    private  String CITY;

    public String getAPI() {
        return API;
    }

    private  String API;


}
