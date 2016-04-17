import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by alexabraham on 4/17/16.
 */
public class HttpRequest {

    private static Gson gson = new Gson();

    public static String GET(String url) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("Authorization", "Bearer " + Config.Lyft.TOKEN);

        HttpResponse httpResponse = httpClient.execute(get);
        InputStream is = httpResponse.getEntity().getContent();


        BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
        StringBuffer response = new StringBuffer();

        String line = null;
        try {
            line = buffReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            response.append(line);
            response.append('\n');
            try {
                line = buffReader.readLine();
            } catch (IOException e) {
                System.out.println(" IOException: " + e.getMessage());
                e.printStackTrace();
            }
        }
        try {
            buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    public static Map<String, Object> parseJson(String json) {

        Gson gson = new Gson();
        Map<String,Object> map = new HashMap<String,Object>();
        map = (Map<String,Object>) gson.fromJson(json, map.getClass());
        return map;

    }
}
