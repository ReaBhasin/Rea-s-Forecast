package rbhasin.reasforecast;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
 import android.support.v7.app.ActionBar;
 import android.support.v4.app.Fragment;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.os.Build;
import android.widget.EditText;

/**
 * The settings activity that is started from the settings option in the navigation drawer.
 */
public class SettingsActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "rbhasin.MESSAGE";

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_settings);
         if (savedInstanceState == null) {
             getSupportFragmentManager().beginTransaction()
                     .commit();
         }
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_weather, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();


         return super.onOptionsItemSelected(item);
     }

    /**
     * Passes the zip code information along to the next activity.
     */
    public void sendZipcode(View view) {
        Intent intent = new Intent(this, WeatherActivity.class);
        EditText editText = (EditText)findViewById(R.id.zipcode);
        String zipcode = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, zipcode);
        startActivity(intent);
    }


 }
