package utilities;

import android.content.Context;

import com.jvapps.android.udacity_p1_popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by John Vehikite on 12/29/2016.
 * Class that holds utilities for parsing JSON
 * Udacity Sunshine app code referenced
 */

public class TheMovieDbJsonUtils {
    /**
     * This method parses JSON from a web response and returns an array of Movie objects
     *
     * @param movieJsonStr JSON response from server
     *
     * @return Array of Movie objects
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getMoviesFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String TMDB_RESULTS = "results";
        final String TMDB_TITLE = "title";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_POSTER = "poster_path";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_PLOT = "overview";

        /* String array to hold each movies output String */
        Movie[] parsedMovieData = null;

        // create a JSON object from JSON data from API call
        JSONObject movieJson = new JSONObject(movieJsonStr);

        // create a JSON array from JSON object
        JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

        // initiate Movie object array
        parsedMovieData = new Movie[movieArray.length()];

        // loop through JSON array to populate Movie object array
        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String releaseDate;
            String posterPath;
            double voteAverage;
            String plot;

            /* Get the JSON object representing the movie at
             * index i
            */
            JSONObject currentMovieObject = movieArray.getJSONObject(i);

            // populate movie data
            title = currentMovieObject.getString(TMDB_TITLE);
            releaseDate = currentMovieObject.getString(TMDB_RELEASE_DATE);
            posterPath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/" + currentMovieObject.getString(TMDB_POSTER).substring(1);
            voteAverage = currentMovieObject.getDouble(TMDB_VOTE_AVERAGE);
            plot = currentMovieObject.getString(TMDB_PLOT);

            // initialize Movie object at current index
            parsedMovieData[i] = new Movie(title, releaseDate, posterPath, voteAverage, plot);
        }
        return parsedMovieData;
    }
}
