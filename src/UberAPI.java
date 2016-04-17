
import com.uber.sdk.rides.auth.OAuth2Credentials;
import com.uber.sdk.rides.client.Response;
import com.uber.sdk.rides.client.Session;
import com.uber.sdk.rides.client.UberRidesServices;
import com.uber.sdk.rides.client.UberRidesSyncService;
import com.uber.sdk.rides.client.model.PriceEstimate;
import com.uber.sdk.rides.client.model.PriceEstimatesResponse;
import com.uber.sdk.rides.client.model.TimeEstimate;
import com.uber.sdk.rides.client.model.TimeEstimatesResponse;
import com.victorsima.uber.UberClient;
import retrofit.RestAdapter;


import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Exchanger;

public class UberAPI {


    public static List<Estimate> getPriceEstimates(float start_lat, float start_long, float end_lat, float end_long) throws Exception {

        Session session = new Session.Builder()
                .setServerToken(Config.Uber.TOKEN)
                .setEnvironment(Session.Environment.PRODUCTION)
                .build();

        UberRidesSyncService service = UberRidesServices.createSync(session);

        PriceEstimatesResponse per = service.getPriceEstimates(start_lat, start_long, end_lat, end_long).getBody();

        List<PriceEstimate> pel = per.getPrices();

        List<Estimate> estimates = new ArrayList<Estimate>();

        for (PriceEstimate pe : pel) {

            if (pe.getHighEstimate() > 0) {
                Estimate e = new Estimate();
                e.service = "Uber";
                e.type = pe.getDisplayName();

                e.lowEstimate = pe.getLowEstimate();
                e.highEstimate = pe.getHighEstimate();
                e.timeEstimate = pe.getDuration();
                estimates.add(e);
            }

        }

        TimeEstimatesResponse ter = service.getPickupTimeEstimates(start_lat, start_long, null).getBody();

        List<TimeEstimate> tel = ter.getTimes();

        for (TimeEstimate te : tel) {
            for (Estimate e : estimates) {
                if (te.getDisplayName().equals(e.type)) {
                    e.eta = te.getEstimate();
                }
            }
        }


        return estimates;
    }
}
