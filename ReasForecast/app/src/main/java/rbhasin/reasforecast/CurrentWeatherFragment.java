package rbhasin.reasforecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment to display current weather conditions.
 */
public class CurrentWeatherFragment extends Fragment implements AsyncResponse {

    /**
     * Text view to display the current status, i.e. whether data is being fetched, zipcode
     * returned no data, etc.
     */
    TextView status;

    ImageView iconURL;
    TextView location;
    TextView weatherConditions;
    TextView temperature;
    TextView feelsLike;
    TextView wind;
    TextView humidity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_weather, container, false);

        //Fetch data as an async task
        new WeatherService(getActivity(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "conditions", ((WeatherActivity) getActivity()).zipcode);

        //Find elements in layout
        status = (TextView)rootView.findViewById(R.id.status);
        iconURL = (ImageView)rootView.findViewById(R.id.icon);
        location = (TextView)rootView.findViewById(R.id.location);
        weatherConditions = (TextView)rootView.findViewById(R.id.conditions);
        temperature = (TextView)rootView.findViewById(R.id.temperature);
        feelsLike = (TextView)rootView.findViewById(R.id.feels_like);
        wind = (TextView)rootView.findViewById(R.id.wind);
        humidity = (TextView)rootView.findViewById(R.id.humidity);

        return rootView;
    }

    /**
     * Parses the response to get the current weather data.
     * @param response The json response returned from the request
     */
    @Override
    public void processResponse(JSONObject response) {

        //Check for valid response
        if (response == null || !response.has("current_observation"))
        {
            status.setText(getActivity().getString(R.string.data_error));
            return;
        }

        try {
            JSONObject currentConditions = response.getJSONObject("current_observation");

            //Get Location information to display
            JSONObject locationInfo = currentConditions.getJSONObject("display_location");
            location.setText(locationInfo.getString("full"));

            //Get current weather condition
            weatherConditions.setText(currentConditions.getString("weather"));

            //Get current temp in fahrenheit
            temperature.setText(currentConditions.getString("temp_f") + getString(R.string.fahrenheit));

            //Get humidity
            humidity.setText(getString(R.string.humidity_label) + currentConditions.getString("relative_humidity"));

            //Get wind information
            wind.setText( getString(R.string.wind_label) + currentConditions.getString("wind_string"));

            //Get feels like temp in fahrenheit
            feelsLike.setText(getString(R.string.feels_like_label) + currentConditions.getString("feelslike_f") + getString(R.string.fahrenheit));

            //Get weather icon
            String icon = currentConditions.getString("icon_url");

            //Fetch image as an async task
            new ImageService(getActivity(), this, iconURL).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, icon);

            status.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}