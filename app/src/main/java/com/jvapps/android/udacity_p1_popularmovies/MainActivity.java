package com.jvapps.android.udacity_p1_popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Movie[]> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_NAME = "MOVIE";

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;
    private Menu menu;

    private MenuItem mostPopular;
    private MenuItem topRated;

    // uniquely identify the loader
    private static final int MOVIE_LOADER_ID = 0;

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
        LoaderManager.LoaderCallbacks<Movie[]> callbacks = MainActivity.this;

        /*
        This is the 2nd parameter for the initLoader() method below.
        Since no bundle is being used in this current version, the value is null.
         */
        Bundle bundleForLoader = null;

        /*
        initLoader() ensures that a loader is initialized and active. If there
        is one already active, the last created loader is reused. If not, a
        new loader is initialized.
         */
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundleForLoader, this);
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
     * Instantiates and returns a new Loader for the given ID
     *
     * @param id The ID whose loader is to be created.
     * @param loaderArgs Any arguments supplied by the caller.
     *
     * @return Return a new Loader instance that is ready to start loading
     */
    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<Movie[]>(this) {

            // movie data will be cached in this Movie array
            Movie[] mMovieData = null;

            /**
             * loads data if mMovieData is null
             */
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                }
                else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * Subclasses of AsyncTaskLoader are required to override this method
             * Loads and parses JSON data
             *
             * @return An array of Movie objects
             */
            @Override
            public Movie[] loadInBackground() {
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

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(Movie[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load
     *
     * @param loader The Loader that has finished
     * @param movieData The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] movieData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieData != null) {
            showMoviePosterView();
            mMovieAdapter.setMovieData(movieData);
        }
        else {
            showErrorMessage();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
        /*
        Method is not being used, but it is included since it must
        be overriden.
         */
    }
    /*
     * AsyncTask removed and replaced by AsyncTaskLoader
     */

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
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            item.setVisible(false);
            topRated = menu.findItem(R.id.action_sort_top_rated);
            topRated.setVisible(true);
        }
        if (id == R.id.action_sort_top_rated) {
            isMostPopularSelected = false;
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            item.setVisible(false);
            mostPopular = menu.findItem(R.id.action_sort_popular);
            mostPopular.setVisible(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
