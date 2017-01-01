package com.example.android.udacity_p1_popularmovies;

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
    private static boolean isMostPopularSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int numCols = NumColsUtil.calculateNoOfColumns(this);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, numCols);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData();
    }


    private void loadMovieData() {
        showMovieInfoView();
        new FetchMovieInfoTask().execute();
    }

    public void onClick(Movie movieInfo) {
        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent intentToStartMovieDetail = new Intent(context, destinationClass);
        intentToStartMovieDetail.putExtra(EXTRA_NAME, movieInfo);
        startActivity(intentToStartMovieDetail);
    }

    private void showMovieInfoView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public class FetchMovieInfoTask extends AsyncTask<Void, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

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
                Movie[] simpleJsonMovieInfo = TheMovieDbJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonResponse);
                return simpleJsonMovieInfo;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieInfo) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieInfo != null) {
                showMovieInfoView();
                mMovieAdapter.setMovieData(movieInfo);
            }
            else {
                showErrorMessage();
            }
        }
    }

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
