package rbhasin.reasforecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Fragment to display three day weather forecast.
 */
public class ThreeDayWeatherFragment extends Fragment implements AsyncResponse {

    /**
     * Text view to display the current status, i.e. whether data is being fetched, zipcode
     * returned no data, etc.
     */
    TextView status;

    TableLayout parent;
    ImageView icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three_day_weather, container, false);

        //Find elements
        status = (TextView)rootView.findViewById(R.id.status);
        parent = (TableLayout)rootView.findViewById(R.id.three_day);

        //Fetch data as an async task
        new WeatherService(getActivity(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"forecast", ((WeatherActivity) getActivity()).zipcode);

        return rootView;
    }

    /**
     * Parses the response to get the three day weather data.
     * @param response The json response returned from the request
     */
    @Override
    public void processResponse(JSONObject response) {

        if (response == null || !response.has("forecast"))
        {
            status.setText(getActivity().getString(R.string.data_error));
            return;
        }

        try {
            JSONObject forecast = response.getJSONObject("forecast");
            JSONObject simpleforecast = forecast.getJSONObject("simpleforecast");
            JSONArray forecastArray = simpleforecast.getJSONArray("forecastday");

            for (int i = 0; i < forecastArray.length(); i++)
            {
                JSONObject forecastDay = forecastArray.getJSONObject(i);

                //Create child table row
               // TableRow child = new TableRow(getActivity());
                //child.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));

                //Add Day Text View
                TextView day = new TextView(getActivity());
                day.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                //Get day information
                JSONObject date = forecastDay.getJSONObject("date");
                day.setText(date.getString("weekday"));
                parent.addView(day);

//                //Add Weather Icon Web View
//                icon = new ImageView(getActivity());
//                icon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//                //Get weather icon
//                String url = forecastDay.getString("icon_url");
//                new ImageService(getActivity(), this, icon).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
//                child.addView(icon);

//                //Add High Temp Text View
//                TextView high = new TextView(getActivity());
//                high.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//                //Get high temp information
//                JSONObject highTemp = forecastDay.getJSONObject("high");
//                high.setText(highTemp.getString("fahrenheit"));
//                child.addView(high);

//                //Add Low Temp Text View
//                TextView low = new TextView(getActivity());
//                low.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
//                //Get low temp information
//                JSONObject lowTemp = forecastDay.getJSONObject("low");
//                high.setText(highTemp.getString("fahrenheit"));
//                child.addView(low);

                //parent.addView(child);
            }


            status.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}