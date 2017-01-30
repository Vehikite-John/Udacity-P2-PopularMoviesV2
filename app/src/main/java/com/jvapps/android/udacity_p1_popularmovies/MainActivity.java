package com.jvapps.android.udacity_p1_popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import utilities.NetworkUtils;
import utilities.NumColsUtil;
import utilities.TheMovieDbJsonUtils;

/**
 * Created by John Vehikite on 12/30/16
 * Controller for the Main UI
 * Udacity Sunshine app code referenced
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_NAME = "MOVIE";

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;
    private Menu menu;
    private MenuItem mostPopular;
    private MenuItem topRated;

    // since app starts with movies sorted by most popular, set to true
    // used to display correct option
    private static boolean isMostPopularSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Get references of RecyclerView, TextView, and ProgressBar
        with findViewById()
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*
        LayoutManager "manages" how item views will be positioned and
        when views are recycled
         */
        int numCols = NumColsUtil.calculateNoOfColumns(this);

        /*
        LayoutManager "manages" how item views will be positioned and
        when views are recycled.
         */
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, numCols);

        /*
        When layout manager is instantiated and set, it is not necessary
        to input it in movie_item.xml
         */
        mRecyclerView.setLayoutManager(layoutManager);

        /*
        If child layout size doesn't change, use this setting to improve performance
         */
        mRecyclerView.setHasFixedSize(true);

        /*
        The MovieAdapter links Movie data to the item views
         */
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        /*
        Once the views are setup, load the movie data
         */
        loadMovieData();
    }

    /**
     * This method calls showMovieInfoView() and FetchMovieInfoTask()
     * to start API call and show RecyclerView with movie posters
     */
    private void loadMovieData() {
        showMoviePosterView();
        new FetchMovieInfoTask().execute();
    }

    /**
     * Creates an intent, puts Movie object as extra,
     * and starts MovieDetail activity
     * @param movie
     */
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent intentToStartMovieDetail = new Intent(context, destinationClass);

        /*
        Since Movie implements Parcelable, we can pass a Movie object
        to the MovieDetail activity
        */
        intentToStartMovieDetail.putExtra(EXTRA_NAME, movie);
        startActivity(intentToStartMovieDetail);
    }

    /**
     * Shows RecyclerView with movie posters and hides TextView
     * with error message
     */
    private void showMoviePosterView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Shows TextViw with error message and hides RecyclerView;
     * called when FetchMovieInfoTask fails (usually for connectivity
     * issues
     */
    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Background task to make API call to themoviedb to fetch
     * movie information
     */
    public class FetchMovieInfoTask extends AsyncTask<Void, Void, Movie[]> {

        /*
        Make the progress bar visible right before API call begins
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        /*
        Make appropriate API call based on value of isMostPopularSelected;
        return an array of Movie objects if API call was successful
         */
        @Override
        protected Movie[] doInBackground(Void... voids) {
            URL moviesUrl;
            if (isMostPopularSelected) {
                moviesUrl = NetworkUtils.buildPopularMoviesUrl();
            }
            else {
                moviesUrl = NetworkUtils.buildTopRatedMoviesUrl();
            }
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                Movie[] simpleJsonMovieInfo = TheMovieDbJsonUtils.getMoviesFromJson(MainActivity.this, jsonResponse);
                return simpleJsonMovieInfo;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /*
        After API call, hide progress bar and either call showMoviePosterView()
        or showErrorMessage()
         */
        @Override
        protected void onPostExecute(Movie[] movieInfo) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieInfo != null) {
                showMoviePosterView();
                mMovieAdapter.setMovieData(movieInfo);
            }
            else {
                showErrorMessage();
            }
        }
    }

    /**
     * Overridden method
     * Shows appropriate option based on current sort
     * e.g., if movies are alredy sorted by top rated, menu will
     * only show "Sort by most popular" option
     * @param menu
     * @return true when method call finishes
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        mostPopular = menu.findItem(R.id.action_sort_popular);
        topRated = menu.findItem(R.id.action_sort_top_rated);
        if (isMostPopularSelected) {
            mostPopular.setVisible(false);
            topRated.setVisible(true);
        }
        else {
            mostPopular.setVisible(true);
            topRated.setVisible(false);
        }
        return true;
    }

    /**
     * Overriden method; when option is selected, hide that option
     * and show other option; also update isMostPopularSeleected
     * @param item
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_popular) {
            isMostPopularSelected = true;
            loadMovieData();
            item.setVisible(false);
            topRated = menu.findItem(R.id.action_sort_top_rated);
            topRated.setVisible(true);
        }
        if (id == R.id.action_sort_top_rated) {
            isMostPopularSelected = false;
            loadMovieData();
            item.setVisible(false);
            mostPopular = menu.findItem(R.id.action_sort_popular);
            mostPopular.setVisible(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
