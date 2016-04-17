import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alexabraham on 4/17/16.
 */
public class LyftAPI {

    public static List<Estimate> getPriceEstimates(float start_lat, float start_long, float end_lat, float end_long) throws Exception {

        List<Estimate> estimates = getEstimates(start_lat, start_long, end_lat, end_long);

        addETA(estimates, start_lat, start_long);

        return estimates;
    }

    private static List<Estimate> getEstimates(float start_lat, float start_long, float end_lat, float end_long) throws Exception {
        String url = Config.Lyft.PRICE_URL;

        url += "?start_lat=" + start_lat;
        url += "&start_lng=" + start_long;
        url += "&end_lat=" + end_lat;
        url += "&end_lng=" + end_long;

        String response = HttpRequest.GET(url);

        List<Estimate> estimates = new ArrayList<Estimate>();

        Map<String, Object> data = HttpRequest.parseJson(response);

        ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) data.get("cost_estimates");

        for (LinkedTreeMap ltm : list) {
            Estimate e = new Estimate();

            e.service = "Lyft";
            e.type = ltm.get("ride_type").toString();

            e.lowEstimate = (int) Double.parseDouble(ltm.get("estimated_cost_cents_min").toString());
            e.lowEstimate = e.lowEstimate/100;

            e.highEstimate = (int) Double.parseDouble(ltm.get("estimated_cost_cents_max").toString());
            e.highEstimate = e.highEstimate/100;

            e.timeEstimate = (int) Double.parseDouble(ltm.get("estimated_duration_seconds").toString());

            estimates.add(e);
        }



        return estimates;
    }

    private static void addETA(List<Estimate> estimates, float lat, float lng) throws Exception {
        String url = Config.Lyft.ETA_URL;
        url += "?lat=" + lat;
        url += "&lng=" + lng;
        String response = HttpRequest.GET(url);
        Map<String, Object> data = HttpRequest.parseJson(response);
        ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) data.get("eta_estimates");

        for (LinkedTreeMap ltm : list) {

            String type = ltm.get("ride_type").toString();

            for (Estimate e : estimates) {
                if (e.type.equals(type)) {
                    e.eta = (int) Double.parseDouble(ltm.get("eta_seconds").toString());
                }
            }
        }
    }
}
