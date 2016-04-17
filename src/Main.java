import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        try {

            port(4567);

            get("/", (req, res) -> {
                return "Hi Danny";
            });

            get("/uber", "application/json", (request, response) -> {

                float start_lat = Float.parseFloat(request.queryParams("start_lat"));
                float start_long = Float.parseFloat(request.queryParams("start_long"));
                float end_lat = Float.parseFloat(request.queryParams("end_lat"));
                float end_long = Float.parseFloat(request.queryParams("end_long"));

                return UberAPI.getPriceEstimates(start_lat, start_long, end_lat, end_long);
            }, new JsonTransformer());

            get("/lyft", "application/json", (request, response) -> {

                float start_lat = Float.parseFloat(request.queryParams("start_lat"));
                float start_long = Float.parseFloat(request.queryParams("start_long"));
                float end_lat = Float.parseFloat(request.queryParams("end_lat"));
                float end_long = Float.parseFloat(request.queryParams("end_long"));

                return LyftAPI.getPriceEstimates(start_lat, start_long, end_lat, end_long);
            }, new JsonTransformer());

            get("/google/:address", "application/json", (request, response) -> {
                float[] coordinates = GoogleAPI.getCoordinates(request.params(":address"));


                return coordinates;
            }, new JsonTransformer());

            get ("/best", "application/json", (request, response) -> {

                float start_lat = Float.parseFloat(request.queryParams("start_lat"));
                float start_long = Float.parseFloat(request.queryParams("start_long"));
                float end_lat = Float.parseFloat(request.queryParams("end_lat"));
                float end_long = Float.parseFloat(request.queryParams("end_long"));

                List<Estimate> lyftEstimates = LyftAPI.getPriceEstimates(start_lat, start_long, end_lat, end_long);
                List<Estimate> uberEstimates = UberAPI.getPriceEstimates(start_lat, start_long, end_lat, end_long);

                Estimate best = uberEstimates.get(0);

                for (Estimate e : uberEstimates) {
                    if (e.highEstimate < best.highEstimate) best = e;
                }

                for (Estimate e : lyftEstimates) {
                    if (e.highEstimate < best.highEstimate) best = e;
                }

                return best;

            }, new JsonTransformer());


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
