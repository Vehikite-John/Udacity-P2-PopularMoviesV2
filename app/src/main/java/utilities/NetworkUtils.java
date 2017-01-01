package utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by John Vehikite on 12/28/2016.
 * Class that holds utilities for API calls
 * Udacity Sunshine app code referenced
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String POPULAR_MOVIES_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=";

    private static final String TOP_RATED_MOVIES_URL =
            "http://api.themoviedb.org/3/movie/top_rated?api_key=";

    // TODO: INPUT THEMOVIEDB API KEY HERE
    private static final String API_KEY = "";

    /**
     * Builds the URL for most popular movies that will be queried
     *
     * @return The most popular movies URL to use to query themoviedb.
     */
    public static URL buildPopularMoviesUrl() {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_URL + API_KEY).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL for top rated movies that will be queried
     *
     * @return The top rated movies URL to use to query themoviedb.
     */
    public static URL buildTopRatedMoviesUrl() {
        Uri builtUri = Uri.parse(TOP_RATED_MOVIES_URL + API_KEY).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
