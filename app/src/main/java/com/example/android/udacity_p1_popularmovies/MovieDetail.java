package com.example.android.udacity_p1_popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by John Vehikite on 12/30/2016.
 * Controller for Movie Details screen
 */

public class MovieDetail extends AppCompatActivity {
    private static final String EXTRA_NAME = "MOVIE";
    private TextView mMovieTitleView;
    private ImageView mMoviePosterView;
    private TextView mMovieYearView;
    private TextView mMovieVoteAverageView;
    private TextView mMoviePlotView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mMovieTitleView = (TextView) findViewById(R.id.tv_detail_display_title);
        mMoviePosterView = (ImageView) findViewById(R.id.iv_detail_poster);
        mMovieYearView = (TextView) findViewById(R.id.tv_detail_display_year);
        mMovieVoteAverageView = (TextView) findViewById(R.id.tv_detail_vote_average);
        mMoviePlotView = (TextView) findViewById(R.id.tv_detail_display_plot);

        Intent intentThatStartedActivity = getIntent();

        /*
        It's good practice to check to see if intent is not null before
        working on it
         */
        if (intentThatStartedActivity != null) {

            /*
            It's als good practice to check to see if intent has extras before
            trying to get extras
             */
            if (intentThatStartedActivity.hasExtra(EXTRA_NAME)) {

                // get Movie object passed from MainActivity.java
                Movie movie = intentThatStartedActivity.getParcelableExtra(EXTRA_NAME);

                // populate views with Movie object data
                mMovieTitleView.setText(movie.title);
                Picasso.with(this).load(movie.poster).into(mMoviePosterView);
                mMovieYearView.setText(movie.releaseDate.substring(0, 4));
                mMovieVoteAverageView.setText(movie.voteAverage + "/10");
                mMoviePlotView.setText(movie.plotSynopsis);
            }
        }
    }
}
