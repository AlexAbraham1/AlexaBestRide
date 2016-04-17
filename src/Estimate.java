/**
 * Created by alexabraham on 4/17/16.
 */
public class Estimate {
    String service;
    String type;

    int lowEstimate;
    int highEstimate;

    int timeEstimate;

    int eta;

    @Override
    public String toString() {
        return "{service=" + service + ", " +
                "type=" + type + ", " +
                "lowEstimate= " + lowEstimate + ", " +
                "highEstimate=" + highEstimate + ", " +
                "timeEstimate=" + timeEstimate + ", " +
                "eta=" + eta + "}";
    }
}
