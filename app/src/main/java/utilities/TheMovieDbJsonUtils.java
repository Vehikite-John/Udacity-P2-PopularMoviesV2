package utilities;

import android.content.Context;

import com.example.android.udacity_p1_popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jdavet on 12/29/2016.
 */

public class TheMovieDbJsonUtils {
    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param movieJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getSimpleWeatherStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String TMDB_RESULTS = "results";
        final String TMDB_TITLE = "title";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_POSTER = "poster_path";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_PLOT = "overview";

        /* String array to hold each movies output String */
        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        // TODO: Handle connection errors

        JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);
        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String releaseDate;
            String posterPath;
            double voteAverage;
            String plot;

            /* Get the JSON object representing the day */
            JSONObject currentMovieObject = movieArray.getJSONObject(i);

            title = currentMovieObject.getString(TMDB_TITLE);
            releaseDate = currentMovieObject.getString(TMDB_RELEASE_DATE);
            posterPath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/" + currentMovieObject.getString(TMDB_POSTER).substring(1);
            voteAverage = currentMovieObject.getDouble(TMDB_VOTE_AVERAGE);
            plot = currentMovieObject.getString(TMDB_PLOT);

            parsedMovieData[i] = new Movie(title, releaseDate, posterPath, voteAverage, plot);
        }

        return parsedMovieData;
    }
}
