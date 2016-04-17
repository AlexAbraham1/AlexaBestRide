import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang.StringEscapeUtils;

import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class GoogleAPI {

    public static float[] getCoordinates(String address) throws Exception {

        address = URLEncoder.encode(address, "UTF-8");

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + Config.Google.API_KEY;
        String json =  HttpRequest.GET(url);

        Map<String, Object> data = HttpRequest.parseJson(json);

        ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) data.get("results");

        float[] coordinates = new float[2];

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        for (LinkedTreeMap l : list) {
            if (l.containsKey("geometry")) {
                LinkedTreeMap l2 = (LinkedTreeMap) l.get("geometry");
                LinkedTreeMap l3 = (LinkedTreeMap) l2.get("location");

                coordinates[0] = Float.parseFloat(df.format(l3.get("lat")));
                coordinates[1] = Float.parseFloat(df.format(l3.get("lng")));
            }
        }

        return coordinates;
    }
}
