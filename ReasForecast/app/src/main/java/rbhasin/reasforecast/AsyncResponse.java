package rbhasin.reasforecast;

import org.json.JSONObject;

/**
 * Interface used so each fragment can process their own json response.
 */
public interface AsyncResponse {
     void processResponse(JSONObject response);
 }