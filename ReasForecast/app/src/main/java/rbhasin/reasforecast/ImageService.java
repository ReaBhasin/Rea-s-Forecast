package rbhasin.reasforecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class handles all async calls made to retrieve images.
 */
public class ImageService extends AsyncTask<String, Void, Bitmap> {

    public Context context;
    public AsyncResponse asyncResponse;
    public WeakReference<ImageView> imageViewWeakReference;

    public ImageService(Context context, AsyncResponse asyncResponse, ImageView imageView)
    {
        this.context = context;
        this.asyncResponse = asyncResponse;
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        URL url;
        JSONObject jobject;
        InputStream inStream = null;

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(inStream);
            return bmp;
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
    protected void onPostExecute(Bitmap result) {
        if (imageViewWeakReference != null && result != null) {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}

