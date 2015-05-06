package rbhasin.reasforecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Fragment to display hourly weather conditions.
 */
public class HourlyWeatherFragment extends Fragment implements AsyncResponse {

    /**
     * Text view to display the current status, i.e. whether data is being fetched, zipcode
     * returned no data, etc.
     */
    TextView status;

    /**
     * Table Layout element that is the root of the table.
     */
    TableLayout parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        //Find elements
        status = (TextView)rootView.findViewById(R.id.status);
        parent = (TableLayout)rootView.findViewById(R.id.hourly);

        //Fetch data as an async task
        new WeatherService(getActivity(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"hourly", ((WeatherActivity) getActivity()).zipcode);

        return rootView;
    }

    /**
     * Parses the response to get the hourly weather data.
     * @param response The json response returned from the request
     */
    @Override
    public void processResponse(JSONObject response) {

        //Check for a valid response
        if (response == null || !response.has("hourly_forecast"))
        {
            status.setText(getActivity().getString(R.string.data_error));
            return;
        }

        try {
            JSONArray forecastArray = response.getJSONArray("hourly_forecast");

            //Build child rows
            for (int i = 0; i < forecastArray.length(); i++)
            {
                JSONObject forecastHour = forecastArray.getJSONObject(i);

                //Create child table row
                TableRow child = new TableRow(getActivity());
                child.setPadding(16,16,16,16);
                child.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                //Add Hour Text View
                TextView hour = new TextView(getActivity());
                hour.setPadding(16,16,16,16);
                hour.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                //Get hour information
                JSONObject date = forecastHour.getJSONObject("FCTTIME");
                hour.setText(date.getString("civil"));
                child.addView(hour);

                //Add Weather Icon Web View
                ImageView icon = new ImageView(getActivity());
                icon.setPadding(16,16,16,16);
                icon.setLayoutParams(new TableRow.LayoutParams(100, 100));

                //Get weather icon
                String url = forecastHour.getString("icon_url");
                new ImageService(getActivity(), this, icon).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                child.addView(icon);

                //Add Temp Text View
                TextView temp = new TextView(getActivity());
                temp.setPadding(16,16,16,16);
                temp.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                //Get temp information
                JSONObject tempObject = forecastHour.getJSONObject("temp");
                temp.setText(tempObject.getString("english") + getString(R.string.fahrenheit));
                child.addView(temp);

                parent.addView(child);
            }

            status.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}