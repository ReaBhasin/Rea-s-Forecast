package rbhasin.reasforecast;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * This class handles all async calls made to retrieve data from the weather underground api.
 */
public class WeatherService extends AsyncTask<String, Void, JSONObject> {

    public Context context;
    public AsyncResponse asyncResponse;

    public WeatherService(Context context, AsyncResponse asyncResponse)
    {
        this.context = context;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String action = params[0];
        String zipcode = params[1];
        String urlString = String.format(context.getString(R.string.weather_api), context.getString(R.string.api_key), action, zipcode);

        HttpURLConnection urlConnection = null;
        URL url;
        JSONObject jobject;
        InputStream inStream = null;

        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            StringBuilder json = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            jobject = (JSONObject) new JSONTokener(json.toString()).nextValue();
            return jobject;
        } catch (Exception e) {
            return null;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ioe) {
                    //do nothing
                }
            }

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        asyncResponse.processResponse(result);
        //ideally we'd want to call notifydatasetchanged to refresh the view here, but I have not
        //been able to implement it correctly yet
    }
}

